package com.util;

import android.graphics.Rect;
import android.hardware.Camera.Size;

import java.util.List;

/**
 * Created by ZhengQinyu on 2016/4/24.
 */
public final class ViewUtil {

    /**
     * 屏幕宽度
     */
    public static Integer FULL_WIDTH = null;

    /**
     * 屏幕高度
     */
    public static Integer FULL_HEIGHT = null;

    /**
     * 展示宽度
     */
    public static Integer VIEW_WIDTH = null;

    /**
     * 展示高度
     */
    public static Integer VIEW_HEIGHT = null;

    /**
     * 计算最适应的视图大小（来官方文档）
     */
    public static Size getMatchSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 计算居中的位置
     */
    public static Rect getCenterPosition(int width, int height, int previewWidth, int previewHeight) {
        Rect rect;
        if (width * previewHeight > height * previewWidth) {
            final int scaledChildWidth = previewWidth * height / previewHeight;
            rect = new Rect((width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height);
        } else {
            final int scaledChildHeight = previewHeight * width / previewWidth;
            rect = new Rect(0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2);
        }
        return rect;
    }

    /**
     * 获得最大的正方形
     * @param rect
     * @param ratio 正方形边长占短边的比例
     * @return
     */
    public static Rect getRatioSquare(Rect rect,float ratio) {
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        int min = (int)((width > height ? height : width) * ratio);
        int tw = (width - min) / 2;
        int th = (height - min) / 2;
        return new Rect(rect.left + tw,rect.top + th,rect.right - tw, rect.bottom - th);
    }
}
