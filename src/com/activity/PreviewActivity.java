package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.model.ColorCube;
import com.view.DrawImageView;
import com.view.PreviewView;
import com.view.ResultView;

/**
 * Created by ZhengQinyu on 2016/4/21.
 */
public class PreviewActivity extends Activity {

    private final String TAG = "zqy:PreviewActivity";

    private PreviewView mPreview;
    private Camera mCamera;

    //默认照相机的ID
    private int defaultCameraId;
    int cameraCurrentlyLocked;

    //三个按钮
    private Button btnPrevious;
    private Button btnPreviewStatus;
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);
        initExtraView();
        initCamera();

    }

    private void initExtraView(){
        //设置视图
        mPreview = (PreviewView) findViewById(R.id.PreviewView);
        DrawImageView drawImageView = (DrawImageView) findViewById(R.id.DrawImageView);
        ResultView resultView = (ResultView) findViewById(R.id.ResultView);
        mPreview.setExtraView(drawImageView,resultView);

        btnPrevious = (Button)findViewById(R.id.previous);
        btnPreviewStatus = (Button)findViewById(R.id.previewStatus);
        btnNext = (Button)findViewById(R.id.next);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.setPreviousClick(btnNext,btnPreviewStatus);
            }
        });

        btnPreviewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.setPreviewStatusClick(btnPreviewStatus);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPreview.setNextClick(btnNext,btnPreviewStatus)){
                    ColorCube colorCube = mPreview.getResultColorCube();
                    Intent intent = new Intent();
                    //将魔方对象传给下一个activity
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("colorCube",colorCube);
                    intent.putExtras(mBundle);
                    intent.setClass(PreviewActivity.this, SolverActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initCamera(){
        // 找到所有的照相机
        int numberOfCameras = Camera.getNumberOfCameras();
        // 找到默认的照相机ID
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 打开默认照相机
        mCamera = Camera.open();
        cameraCurrentlyLocked = defaultCameraId;
        mPreview.setCamera(mCamera);
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 因为照相机共享资源，所以暂停的时候必须释放该资源，避免占用.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        Log.d(TAG, "onPause");
    }
}
