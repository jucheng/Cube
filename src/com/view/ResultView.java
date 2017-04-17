package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.model.ColorFace;
import com.model.CubeColor;

/**
 * Created by ZhengQinyu on 2016/5/2.
 */
public class ResultView extends ImageView {

    private final String TAG = "zqy:ResultView";
    private Rect resultRect;
    private Rect selectRect;
    private int resultClick = -1;
    private int selectClick = -1;
    private final CubeColor[] selectColor = {
            CubeColor.BLUE, CubeColor.RED, CubeColor.GREEN,
            CubeColor.ORANGE, CubeColor.YELLOW, CubeColor.WHITE
    };
    private ColorFace cubeFace;
    private Paint paint;

    public ResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resultRect = new Rect();
        selectRect = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);
        cubeFace = new ColorFace();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        //计算九宫格的位置
        int min = Math.min(width, height);
        int tLen = (int) (min * 0.7);
        int tW = (min - tLen) / 2;
        resultRect.set(tW, tW, tW + tLen, tW + tLen);
        //计算颜色选择框的位置
        selectRect.set(tW, 2 * tW + tLen, tW + tLen, 2 * tW + (int) (5f / 3 * tLen));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawResultColor(canvas);
        drawSelectColor(canvas);
    }

    public void updateResultColor(ColorFace cubeFace){
        this.cubeFace = cubeFace;
        this.invalidate();
    }


    //画识别结果
    private void drawResultColor(Canvas canvas) {
        int tl = (resultRect.right - resultRect.left) / 3;
        int tx, ty;
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(1);
                tx = resultRect.left + tl * x;
                ty = resultRect.top + tl * y;
                //设置单元格颜色
                paint.setColor(cubeFace.getColor(3 * y + x).getPixel());
                canvas.drawRect(tx, ty, tx + tl, ty + tl, paint);
                //画选中的框
                if(3 * y + x == resultClick){
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.MAGENTA);
                    paint.setStrokeWidth(5);
                    canvas.drawRect(tx, ty, tx + tl, ty + tl, paint);
                }
            }
        }
        //画边框
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(resultRect, paint);
        for (int i = 1; i < 3; ++i) {
            canvas.drawLine(resultRect.left, resultRect.top + i * tl, resultRect.right, resultRect.top + i * tl, paint);
        }
        for (int i = 1; i < 3; ++i) {
            canvas.drawLine(resultRect.left + i * tl, resultRect.top, resultRect.left + i * tl, resultRect.bottom, paint);
        }
    }

    //画颜色选择框
    private void drawSelectColor(Canvas canvas) {
        int tl = (selectRect.right - selectRect.left) / 3;
        int tx, ty;
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(1);
                tx = selectRect.left + tl * x;
                ty = selectRect.top + tl * y;
                //设置单元格颜色
                paint.setColor(selectColor[3 * y + x].getPixel());
                canvas.drawRect(tx, ty, tx + tl, ty + tl, paint);
                //画选中的框
                if(3 * y + x == selectClick){
                    paint.setStrokeWidth(5);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.MAGENTA);
                    canvas.drawRect(tx, ty, tx + tl, ty + tl, paint);
                }
            }
        }
        //画边框
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(selectRect, paint);
        for (int i = 1; i < 2; ++i) {
            canvas.drawLine(selectRect.left, selectRect.top + i * tl, selectRect.right, selectRect.top + i * tl, paint);
        }
        for (int i = 1; i < 3; ++i) {
            canvas.drawLine(selectRect.left + i * tl, selectRect.top, selectRect.left + i * tl, selectRect.bottom, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //屏幕点击事件
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            int clickX = (int)event.getX();
            int clickY = (int)event.getY();
            //检测选中的位置
            if(resultRect.contains(clickX,clickY)){
                int tl = (resultRect.right - resultRect.left) / 3;
                int tx, ty;
                for (int y = 0; y < 3; ++y) {
                    for (int x = 0; x < 3; ++x) {
                        tx = resultRect.left + tl * x;
                        ty = resultRect.top + tl * y;
                        if(clickX >= tx && clickX < tx + tl && clickY >= ty && clickY < ty + tl){
                            resultClick = 3 * y + x;
                            //设置结果颜色
                            if(selectClick != -1) {
                                cubeFace.setColor(resultClick, selectColor[selectClick]);
                            }
                            this.invalidate();
                            return true;
                        }
                    }
                }
            }else if(selectRect.contains(clickX,clickY)){
                int tl = (selectRect.right - selectRect.left) / 3;
                int tx, ty;
                for (int y = 0; y < 2; ++y) {
                    for (int x = 0; x < 3; ++x) {
                        tx =  tl * x + selectRect.left;
                        ty = tl * y + selectRect.top;
                        if(clickX >= tx && clickX < tx + tl && clickY >= ty && clickY < ty + tl){
                            selectClick = 3 * y + x;
                            this.invalidate();
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

}
