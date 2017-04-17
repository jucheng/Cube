package com.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.model.CubeColor;
import com.model.ColorFace;

/**
 * Created by ZhengQinyu on 2016/4/15.
 * 图片读取工具
 */
public final class ImageReader {

    private final static String TAG = "zqy:ImageReader";

    public static ColorFace HandleImage(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int perWidth = width / 3;
        int perHeight = height / 3;
        ColorFace cubeFace = new ColorFace();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                //处理一个单元格
                CubeColor color = HandlePerSquareColor(image,
                        new Rect(perWidth * x, perHeight * y,
                                perWidth * (x + 1), perHeight * (y + 1)));
                cubeFace.setColor(3 * y + x, color);
            }
        }
        return cubeFace;
    }

    /**
     * 计算平均hsv值
     *
     * @param image
     * @param tRect
     * @return
     */
    private static String HandlePerSquareData(Bitmap image, Rect tRect) {
        //取中间部分的像素进行计算
        Rect rect = ViewUtil.getRatioSquare(tRect, 0.33f);
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        int tw = 1;
        int th = 1;
        //压缩处理，每个单元格最多处理50*50的像素
        if (width > 50) tw = width / 50;
        if (height > 50) th = height / 50;
        float[] resultColor = new float[3];
        //初始化颜色的数量
        for (int i = 0; i < 3; ++i) resultColor[i] = 0;
        int sizes = rect.width() * rect.height();
        for (int x = rect.left; x < rect.right; x += tw) {
            for (int y = rect.top; y < rect.bottom; y += th) {
                float[] hsv = ColorUtil.getHSV(image.getPixel(x, y));
                for (int let = 0; let < 3; ++let) resultColor[let] += hsv[let];
            }
        }
        //计算平均值
        for (int let = 0; let < 3; ++let) resultColor[let] /= sizes;
        return String.format("(%f,%f,%f)", resultColor[0], resultColor[1], resultColor[2]);
    }

    /**
     * 识别颜色
     *
     * @param image
     * @param tRect
     * @return
     */
    private static CubeColor HandlePerSquareColor(Bitmap image, Rect tRect) {
        //取中间部分的像素进行计算
        Rect rect = ViewUtil.getRatioSquare(tRect, 0.5f);
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        int tw = 1;
        int th = 1;
        //压缩处理，每个单元格最多处理100*100的像素
        if (width > 100) tw = width / 100;
        if (height > 100) th = height / 100;
        int[] resultColor = new int[7];
        //初始化颜色的数量
        for (int i = 0; i < 6; ++i) resultColor[i] = 0;
        for (int x = rect.left; x < rect.right; x += tw) {
            for (int y = rect.top; y < rect.bottom; y += th) {
                int[] rgb = ColorUtil.getRGB(image.getPixel(x, y));
                float[] hsv = ColorUtil.getHSV(image.getPixel(x, y));
                CubeColor color = ColorUtil.checkColor(rgb[0], rgb[1], rgb[2], hsv[0], hsv[1], hsv[2]);
                resultColor[color.getId()]++;
            }
        }
        return findMaxColor(resultColor);
    }

    /**
     * 找像素最多的颜色
     *
     * @param numbers
     * @return
     */
    private static CubeColor findMaxColor(int[] numbers) {
        //计算像素总数
        int max = numbers[0], id = 0;
        for (int i = 1; i < numbers.length; ++i) {
            if (numbers[i] >= max) {
                max = numbers[i];
                id = i;
            }
        }
        for (CubeColor color : CubeColor.values()) {
            if (color.getId() == id) return color;
        }
        return null;
    }


    /**
     * 高斯模糊
     * [1 2 1]
     * 1/16[2 4 2]
     * [1 2 1]
     * 使用高斯模板针对色相做邻域平均，对图像去噪
     */
    public static void GaussianBlur(Bitmap image) {
        for (int y = 1; y < image.getHeight() - 1; ++y) {
            for (int x = 1; x < image.getWidth() - 1; ++x) {
                float sum = 0;
                sum += ColorUtil.getHSV(image.getPixel(x - 1, y - 1))[0] * 1;
                sum += ColorUtil.getHSV(image.getPixel(x, y - 1))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y - 1))[0] * 1;
                sum += ColorUtil.getHSV(image.getPixel(x - 1, y))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x, y))[0] * 4;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x - 1, y + 1))[0] * 1;
                sum += ColorUtil.getHSV(image.getPixel(x, y + 1))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y + 1))[0] * 1;
                float[] hsv = ColorUtil.getHSV(image.getPixel(x, y));
                hsv[0] = sum * (1f / 16);
                image.setPixel(x, y, ColorUtil.covertHSVToColor(hsv));
            }
        }
    }


    /**
     * 锐化，使用Sobel算子针对色相增强图像
     * [-1 0 1]
     * [-2 0 2]
     * [-1 0 1]
     */
    public static void Sobel(Bitmap image) {
        for (int y = 1; y < image.getHeight() - 1; ++y) {
            for (int x = 1; x < image.getWidth() - 1; ++x) {
                float sum = 0;
                sum -= ColorUtil.getHSV(image.getPixel(x - 1, y - 1))[0];
                sum -= ColorUtil.getHSV(image.getPixel(x, y - 1))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y - 1))[0];
                sum -= ColorUtil.getHSV(image.getPixel(x - 1, y))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y))[0] * 2;
                sum -= ColorUtil.getHSV(image.getPixel(x - 1, y + 1))[0];
                sum += ColorUtil.getHSV(image.getPixel(x, y + 1))[0] * 2;
                sum += ColorUtil.getHSV(image.getPixel(x + 1, y + 1))[0];
                float[] hsv = ColorUtil.getHSV(image.getPixel(x, y));
                hsv[0] = sum;
                image.setPixel(x, y, ColorUtil.covertHSVToColor(hsv));
            }
        }
    }
}