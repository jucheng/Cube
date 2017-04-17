package com.activity;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import com.model.ColorCube;
import com.model.CubeColor;
import com.model.ResultListener;
import com.model.RubikCube;
import com.util.SolverTask;

/**
 * Created by ZhengQinyu on 2016/5/5.
 */


public class SolverActivity extends Activity {
    private final String TAG = "zqy:SolverActivity";
    RubikCube cubeModel;
    private GLSurfaceView glsurfaceView;
    private float oldX = 0, oldY = 0;

    private SolverTask task;
    private String rubikCubeStr;
    private boolean isSolving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ColorCube colorCube = (ColorCube) getIntent().getSerializableExtra("colorCube");
        //初始化魔方
        if (colorCube == null) {
            colorCube = new ColorCube();
            colorCube.getFace(0).setAllColor(CubeColor.BLUE);
            colorCube.getFace(1).setAllColor(CubeColor.RED);
            colorCube.getFace(2).setAllColor(CubeColor.GREEN);
            colorCube.getFace(3).setAllColor(CubeColor.YELLOW);
            colorCube.getFace(4).setAllColor(CubeColor.ORANGE);
            colorCube.getFace(5).setAllColor(CubeColor.WHITE);
        }
        cubeModel = new RubikCube(this, colorCube);
        glsurfaceView = new GLSurfaceView(this);
        glsurfaceView.setRenderer(cubeModel);
        setContentView(glsurfaceView);
        this.rubikCubeStr = colorCube.toRubikCubeString();
    }

    private void beginSolverCube() {
        if (null != task) {
            if (task.getStatus() == AsyncTask.Status.RUNNING) {
                return;
            } else if (task.getStatus() == AsyncTask.Status.PENDING) {
                task.cancel(false);
            }
        }
        isSolving = true;
        Log.d(TAG, rubikCubeStr);
        task = new SolverTask(rubikCubeStr);
        //返回结果的处理
        task.setResultListener(new ResultListener() {
            @Override
            public void onDataSuccessfully(Object data) {
                String resultStr = (String) data;
                if (resultStr.contains("Error")) {
                    Toast.makeText(SolverActivity.this, "解魔方失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, resultStr);
                String[] results = resultStr.split(" ");
                //将结果传入魔方执行动画
                for (String str : results) {
                    if (str.length() == 1) {
                        cubeModel.addRotation(str.charAt(0), 1);
                    } else if (str.length() == 2) {
                        if (str.charAt(1) == '\'') {
                            cubeModel.addRotation(str.charAt(0), 3);
                        } else {
                            cubeModel.addRotation(str.charAt(0), 2);
                        }
                    }
                }
            }

            @Override
            public void onDataFailed() {
                isSolving = false;
                Toast.makeText(SolverActivity.this, "连接不上服务器！", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "连接不上服务器！");
            }
        });
        task.execute((Void) null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - oldX;
            float dy = y - oldY;
            cubeModel.rotateMethod(dx, dy);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //开始阶魔方
            if (!isSolving) {
                beginSolverCube();
            }
        }
        oldX = x;
        oldY = y;
        return true;
    }
}
