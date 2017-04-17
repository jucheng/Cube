package com.model;

import com.util.ColorUtil;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CubeBuild {

    private FloatBuffer vertexBuffer;

    private CubeColor[] colors;

    private float[] vertices = {
            //F前
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            //R右
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,

            //B后
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            //L左
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,

            //U上
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,

            //D下
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f
    };


    public CubeBuild() {
        ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        tmpBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = tmpBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        colors = new CubeColor[6];
    }

    public void setColor(int index, CubeColor color){
        colors[index] = color;
    }


    public void draw(GL10 gl10) {
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glFrontFace(GL10.GL_CCW);
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        float rgba[];
        for (int face = 0; face < 6; ++face) {
            rgba = ColorUtil.covertPixelToGLColor(
                    colors[face] == null ? CubeColor.OTHER.getPixel() : colors[face].getPixel()
            );
            gl10.glColor4f(rgba[0],rgba[1],rgba[2],rgba[3]);
            gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4 * face, 4);
        }
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}