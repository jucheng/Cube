package com.model;

/**
 * Created by ZhengQinyu on 2016/5/1.
 */
public interface ResultListener {

    void onDataSuccessfully(Object data);
    void onDataFailed();
}
