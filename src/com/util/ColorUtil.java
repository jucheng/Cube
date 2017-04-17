package com.util;

import android.graphics.Color;
import com.model.CubeColor;

/**
 * Created by ZhengQinyu on 2016/4/21.
 */
public final class ColorUtil {

    public static int[] getRGB(int pixel) {
        int[] rgb = new int[3];
        rgb[0] = Color.red(pixel);
        rgb[1] = Color.green(pixel);
        rgb[2] = Color.blue(pixel);
        return rgb;
    }

    public static float[] getHSV(int pixel) {
        float[] hsv = new float[3];
        Color.colorToHSV(pixel, hsv);
        return hsv;
    }

    /**
     * 颜色识别器
     */
    public static CubeColor checkColor(int red, int green, int blue, float hue, float saturation, float value) {
        if (saturation <= 0.3 && value >= 0.4) {
            return CubeColor.WHITE;
        }
        if (value < 0.1) {
            //过滤黑色
            return CubeColor.OTHER;
        } else if (hue < 20) {
            if (green > blue) {
                return CubeColor.ORANGE;
            } else return CubeColor.RED;
        } else if (hue >= 20 && hue <= 70) {
            if (red == 0) return CubeColor.OTHER;
            //red和green相近时取黄色
            if (green != 0 && (red / green == 1 || green / red == 1)) {
                return CubeColor.YELLOW;
            } else return CubeColor.ORANGE;
        } else if (hue >= 90 && hue <= 170) {
            return CubeColor.GREEN;
        } else if (hue >= 200 && hue < 270) {
            return CubeColor.BLUE;
        } else if (hue >= 345) {
            return CubeColor.RED;
        } else return CubeColor.OTHER;
    }

    public static int covertRGBToColor(int[] rgb) {
        for (int i = 0; i < 3; ++i) {
            if (rgb[i] < 0) rgb[i] = 0;
            else if (rgb[i] > 255) rgb[i] = 255;
        }
        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    public static int covertHSVToColor(float[] hsv) {
        if (hsv[0] < 0 || hsv[0] >= 360) hsv[0] = 0;
        if (hsv[1] < 0) hsv[1] = 0;
        else if (hsv[1] > 1) hsv[1] = 1;
        if (hsv[2] < 0) hsv[2] = 0;
        else if (hsv[2] > 1) hsv[2] = 1;
        return Color.HSVToColor(hsv);
    }

    /**
     * 将颜色转成opengl的格式
     */
    public static float[] covertPixelToGLColor(int pixel) {
        float[] rgba = new float[4];
        rgba[0] = Color.red(pixel) / 255f;
        rgba[1] = Color.green(pixel) / 255f;
        rgba[2] = Color.blue(pixel) / 255f;
        rgba[3] = Color.alpha(pixel) / 255f;
        return rgba;
    }
}
