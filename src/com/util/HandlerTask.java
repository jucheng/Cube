package com.util;

import android.graphics.*;
import android.os.AsyncTask;
import android.util.Log;
import com.model.ColorFace;
import com.model.ResultListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ZhengQinyu on 2016/4/21.
 */
public class HandlerTask extends AsyncTask<Void, Void, ColorFace> {

    private final String TAG = "zqy:HandlerTask";

    //图像数据
    private byte[] mData;
    private int width;
    private int height;

    //返回结果所用的接口
    private ResultListener resultListener;

    //构造函数
    public HandlerTask(byte[] data, int width, int height) {
        this.mData = data;
        this.width = width;
        this.height = height;
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    protected ColorFace doInBackground(Void... params) {
        final YuvImage image = new YuvImage(mData, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
        if (!image.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        try {
            os.close();
        } catch (IOException e) {
            Log.e(TAG, "close ByteArrayOutputStream failure!", e);
            return null;
        }
//        FileUtil.saveImage(bmp);//保存图片
        //截取图片
        Rect square = ViewUtil.getRatioSquare(new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), 0.9f);
        Bitmap rectBitmap = Bitmap.createBitmap(bmp, square.left, square.top, square.right - square.left, square.bottom - square.top);
        return ImageReader.HandleImage(rectBitmap);
    }

    @Override
    protected void onPostExecute(ColorFace cubeFace) {
        if (cubeFace != null) {
            resultListener.onDataSuccessfully(cubeFace);
        } else {
            resultListener.onDataFailed();
        }
    }
}
