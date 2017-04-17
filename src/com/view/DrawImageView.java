package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.activity.R;
import com.util.ViewUtil;

/**
 * Created by ZhengQinyu on 2016/4/22.
 */
public class DrawImageView extends ImageView {

    private final static String TAG = "zqy:DrawImageView";

    private Paint paint;

    private Rect square;

    private String title = this.getContext().getString(R.string.front);

    public DrawImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setAlpha(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(square == null) {
            Rect rect = ViewUtil.getCenterPosition(ViewUtil.FULL_WIDTH, ViewUtil.FULL_HEIGHT, ViewUtil.VIEW_WIDTH, ViewUtil.VIEW_HEIGHT);
            //获取预览的九宫格
            square = ViewUtil.getRatioSquare(rect, 0.9f);
        }
        drawPreviewRect(canvas, paint, square);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    /**
     * 画九宫格
     */
    private void drawPreviewRect(Canvas canvas, Paint paint,Rect rect) {
        int pWidth = rect.width() / 3;
        int pHeight = rect.height() / 3;
        //设置标题
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1f);//设置线宽
        paint.setTextSize(60);
        canvas.drawText(title,rect.left,rect.top + 60,paint);

        //绘制九宫格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        canvas.drawRect(rect, paint);
        for (int i = 1; i < 3; ++i) {
            canvas.drawLine(rect.left, rect.top + i * pHeight, rect.right, rect.top + i * pHeight, paint);
        }
        for (int i = 1; i < 3; ++i) {
            canvas.drawLine(rect.left + i * pWidth, rect.top, rect.left + i * pWidth, rect.bottom, paint);
        }
    }

    public void updateTitle(String title){
        this.title = title;
        //重新绘制
        this.postInvalidate();
    }
}
