package com.view;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.activity.R;
import com.model.ColorCube;
import com.model.ColorFace;
import com.model.ResultListener;
import com.util.HandlerTask;
import com.util.ViewUtil;
import org.kociemba.twophase.Tools;

import java.io.IOException;
import java.util.List;

/**
 * Created by ZhengQinyu on 2016/4/24.
 */
public class PreviewView extends ViewGroup implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private final String TAG = "zqy:PreviewView";
    private HandlerTask task;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private Camera mCamera;
    private DrawImageView drawImageView;
    private ResultView resultView;

    //魔方对象
    private ColorCube rubikCube;
    //预览状态
    private boolean updateStatus = false;
    //当前识别的面
    private int curFace = 0;
    private final String FACE_CN_NAME = "前右后左上下";

    public PreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setExtraView(DrawImageView drawImageView, ResultView resultView) {
        this.drawImageView = drawImageView;
        this.resultView = resultView;
        rubikCube = new ColorCube();
        //初始化设值
        resultView.updateResultColor(rubikCube.getFace('F'));
    }

    //设置照相机
    public void setCamera(Camera camera) {
        if (camera != null) {
            //获得手机支持的预览大小
            mCamera = camera;
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            requestLayout();
        } else {
            //释放照相机资源
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
            }
            mCamera = null;
        }
    }

    //设置最佳的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        ViewUtil.FULL_WIDTH = width;
        ViewUtil.FULL_HEIGHT = height;
        setMeasuredDimension(width, height);
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = ViewUtil.getMatchSize(mSupportedPreviewSizes, width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }
            // 使SurfaceView居中
            Rect rect = ViewUtil.getCenterPosition(width, height, previewWidth, previewHeight);
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
            //设置展示的宽度和高度
            ViewUtil.VIEW_WIDTH = rect.right - rect.left;
            ViewUtil.VIEW_HEIGHT = rect.bottom - rect.top;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException exception) {
            Log.e(TAG, "setPreviewDisplay()异常", exception);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            return;
        }
        //实现自动对焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    camera.setPreviewCallback(PreviewView.this);
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != task) {
            switch (task.getStatus()) {
                case RUNNING:
                    return;
                case PENDING:
                    task.cancel(false);
                    break;
            }
        }
        Camera.Size size = mCamera.getParameters().getPreviewSize(); //获取预览大小
        if (updateStatus) {
            task = new HandlerTask(data, size.width, size.height);
            //返回结果的处理
            task.setResultListener(new ResultListener() {
                @Override
                public void onDataSuccessfully(Object data) {
                    ColorFace cubeFace = (ColorFace) data;
                    if (cubeFace.valid()) {
                        resultView.updateResultColor(cubeFace);
                        rubikCube.setFace(curFace, cubeFace);
                    }
                }

                @Override
                public void onDataFailed() {
                    Log.e(TAG, "识别结果为空！");
                }
            });
            task.execute((Void) null);
        }
    }

    //点击事件在这个处理
    public void setPreviousClick(Button button, Button buttonStatus) {
        ColorFace face = rubikCube.getFace(curFace);
        if (face == null || !face.valid()) {
            Toast.makeText(this.getContext(), "无效的颜色，请重新设置!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (curFace == 0) return;
        curFace--;
        if (curFace <= 0) curFace = 0;
        refreshView();
        button.setText(R.string.nextFace);
        buttonStatus.setText(R.string.start);
    }

    public void setPreviewStatusClick(Button button) {
        //识别状态切换
        if (updateStatus) {
            updateStatus = false;
            button.setText(R.string.start);
        } else {
            updateStatus = true;
            button.setText(R.string.pause);
        }
    }

    public boolean setNextClick(Button button, Button buttonStatus) {
        ColorFace face = rubikCube.getFace(curFace);
        if (face == null || !face.valid()) {
            Toast.makeText(this.getContext(), "无效的颜色，请重新设置!", Toast.LENGTH_SHORT).show();
            return false;
        }
        //最后一面
        if (curFace == 5) {
            //验证魔方的正确性
            if (Tools.verify(rubikCube.toRubikCubeString()) != 0) {
                Toast.makeText(this.getContext(), "魔方序列错误，请检查各个面!", Toast.LENGTH_SHORT).show();
                return false;
            } else return true;
        }
        curFace++;
        if (curFace >= 5) curFace = 5;
        refreshView();
        buttonStatus.setText(R.string.pause);
        if (curFace == 5) {
            button.setText(R.string.solver);
        } else {
            button.setText(R.string.nextFace);
        }
        return false;
    }

    /**
     * 刷新页面
     */
    public void refreshView() {
        ColorFace face = rubikCube.getFace(curFace);
        if (face == null) {
            face = new ColorFace();
        }
        updateStatus = !face.valid();
        resultView.updateResultColor(face);
        drawImageView.updateTitle(String.valueOf(FACE_CN_NAME.charAt(curFace)));
    }

    /**
     * 获得最终识别到的魔方
     *
     * @return
     */
    public ColorCube getResultColorCube() {
        return rubikCube;
    }
}
