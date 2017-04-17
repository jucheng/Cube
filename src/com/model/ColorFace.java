package com.model;

import java.io.Serializable;

/**
 * Created by ZhengQinyu on 2016/4/30.
 */

public class ColorFace implements Serializable {

    private CubeColor[] colors;

    public static final int LENGTH = 9;

    public ColorFace() {
        //9个小块
        colors = new CubeColor[LENGTH];
        // 默认颜色是OTHER
        for (int i = 0; i < LENGTH; ++i) {
            colors[i] = CubeColor.OTHER;
        }
    }

    public void setColor(int index, CubeColor color) {
        if (index < 0 || index > LENGTH - 1) {
            throw new RuntimeException("请输入正确的位置！");
        }
        colors[index] = color;
    }

    public CubeColor getColor(int index) {
        if (index < 0 || index > LENGTH - 1) {
            throw new RuntimeException("请输入正确的位置！");
        }
        return colors[index];
    }

    /**
     * 判断魔方的各个颜色是否合法
     *
     * @return
     */
    public boolean valid() {
        for (int i = 0; i < LENGTH; ++i) {
            if (colors[i].equals(CubeColor.OTHER)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 该面的颜色
     *
     * @return
     */
    public CubeColor getCenterColor() {
        return colors[4];
    }

    public CubeColor[] getColors() {
        return colors;
    }

    public void setAllColor(CubeColor color) {
        for (int i = 0; i < LENGTH; ++i) {
            colors[i] = color;
        }
    }
}
