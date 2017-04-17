package com.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ZhengQinyu on 2016/4/28.
 * 将预览的照片保存到本地的 imgPath 路径，便于测试
 */
public final class FileUtil {

    private final static String TAG = "zqy:FileUtil";
    //临时的文件保存路径
    private final static String imgPath = "/sdcard/DCIM/cubeTestData/";

    /**
     * 保存文件
     */
    public static void saveImage(Bitmap image) {
        if (!check()) {
            //检查不通过，退出
            return;
        }
        String fileName = StringUtil.getNowTimeFormatString("yyyyMMddHHmmss");
        File f = new File(imgPath, fileName + ".png");
        if (f.exists()) f.delete();
        try {
            FileOutputStream out = new FileOutputStream(f);
            image.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "保存图片");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean check() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) { // 检测sd是否可用
            Log.w(TAG, "SD卡不存在！");
            return false;
        }
        //检查文件夹是否存在
        File file = new File(imgPath);
        if (!file.exists() || !file.isDirectory()) {
            //创建文件夹
            file.mkdir();
        }
        return true;
    }
}
