package com.model;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : neaGaze (Nigesh)
 *         3D立体魔方
 */

public class RubikCube extends GLSurfaceView implements GLSurfaceView.Renderer {

    private final String TAG = "zqy:RubikCube";
    public final static int LENGTH = 27;
    private static float width = 2.1f;
    private CubeBuild[] cube;
    private static float cubeRotX = 0.0f, cubeRotY = 0.0f;
    private static final float TOUCH_SCALE = 0.2f;
    private float oldX = 0, oldY = 0;
    public Context context;

    //魔方的颜色
    private ColorCube colorCube;
    //旋转需要的参数
    private List<Rotation> rotations;

    public RubikCube(Context context, ColorCube colorCube) {
        super(context);
        this.context = context;
        this.requestFocus();
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        cube = new CubeBuild[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            cube[i] = new CubeBuild();
        }
        this.colorCube = colorCube;
        //更新魔方的颜色
        updateRubikCubeColor();
    }

    public void updateRubikCubeColor() {
        // F R B L U D
        for (int i = 0; i < 6; ++i) {
            ColorFace face = colorCube.getFace(i);
            this.setFaceColor(i, face);
        }
    }

    //设置面的颜色
    private void setFaceColor(int index, ColorFace cubeFace) {
        int loc;
        switch (index) {
            case 0: //F
                loc = 0;
                for (int z = 2; z >= 0; --z) {
                    for (int y = 0; y < 3; ++y) {
                        this.getCubeBuild(2, y, z).setColor(0, cubeFace.getColor(loc++));
                    }
                }
                break;
            case 1: //R
                loc = 0;
                for (int z = 2; z >= 0; --z) {
                    for (int x = 2; x >= 0; --x) {
                        this.getCubeBuild(x, 2, z).setColor(1, cubeFace.getColor(loc++));
                    }
                }
                break;
            case 2: //B
                loc = 0;
                for (int z = 2; z >= 0; --z) {
                    for (int y = 2; y >= 0; --y) {
                        this.getCubeBuild(0, y, z).setColor(2, cubeFace.getColor(loc++));
                    }
                }
                break;
            case 3: //L
                loc = 0;
                for (int z = 2; z >= 0; --z) {
                    for (int x = 0; x < 3; ++x) {
                        this.getCubeBuild(x, 0, z).setColor(3, cubeFace.getColor(loc++));
                    }
                }
                break;
            case 4: //U
                loc = 0;
                for (int x = 0; x < 3; ++x) {
                    for (int y = 0; y < 3; ++y) {
                        this.getCubeBuild(x, y, 2).setColor(4, cubeFace.getColor(loc++));
                    }
                }
                break;
            case 5: //D
                loc = 0;
                for (int x = 2; x >= 0; --x) {
                    for (int y = 0; y < 3; ++y) {
                        this.getCubeBuild(x, y, 0).setColor(5, cubeFace.getColor(loc++));
                    }
                }
                break;
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -10 * width);
        gl.glRotatef(cubeRotX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(cubeRotY, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 10 * width);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    gl.glPushMatrix();
                    rotationHandler(gl, x, y, z);
                    gl.glTranslatef(-width + (width * y), -width + (width * z), -11 * width + (width * x));
                    this.getCubeBuild(x, y, z).draw(gl);
                    gl.glPopMatrix();
                }
            }
        }

        cubeRotX -= 0.0f;
        cubeRotY -= 0.0f;
        gl.glPopMatrix();
    }

    /**
     * 旋转处理
     */
    private void rotationHandler(GL10 gl, int x, int y, int z) {
        if (rotations == null || rotations.size() <=0 ) return;
        Rotation rotation = rotations.get(0);
        if (rotation == null) {
            return;
        }
        if (rotation.isMove()) {
            if (rotation.getRotX() == x || rotation.getRotY() == y || rotation.getRotZ() == z) {
                //移动位置坐标再旋转
                gl.glTranslatef(0.0f, 0.0f, -10 * width);
                gl.glRotatef(rotation.move(), rotation.getAxisX(), rotation.getAxisY(), rotation.getAxisZ());
                gl.glTranslatef(0.0f, 0.0f, 10 * width);
            }
        } else {
            //开始变化颜色
            colorCube.cubeRotation(rotation.getLoc(),rotation.getRotType());
            //更新颜色
            this.updateRubikCubeColor();
            //置空
            rotations.remove(0);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); // (viewing angle, aspect ratio, near, further)

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - oldX;
            float dy = y - oldY;

            cubeRotX += dy * TOUCH_SCALE;
            cubeRotY += dx * TOUCH_SCALE;
        }
        oldX = x;
        oldY = y;
        return true;
    }

    //For rotating x-value and y-value
    public void rotateMethod(float xRotFactor, float yRotFactor) {
        cubeRotX += yRotFactor * TOUCH_SCALE;
        cubeRotY += xRotFactor * TOUCH_SCALE;
    }

    private CubeBuild getCubeBuild(int x, int y, int z) {
        return cube[9 * x + 3 * y + z];
    }

    /**
     * 设置旋转方式
     * @param loc
     * @param rot 1:90度 2:180度 3：-90
     */
    public void addRotation(char loc, int rot) {
        if(rotations == null){
            rotations = new ArrayList<>();
        }
        rotations.add(new Rotation(loc, rot));
    }
}
