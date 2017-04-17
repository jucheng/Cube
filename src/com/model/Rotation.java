package com.model;

/**
 * Created by ZhengQinyu on 2016/5/7.
 * 执行的旋转动作类
 */
public class Rotation {

    private boolean moveStatus;
    private int rotX = -1;
    private int rotY = -1;
    private int rotZ = -1;
    private float axisX;
    private float axisY;
    private float axisZ;

    private float curRot;
    private float rot;
    //移动速度，数值越大速度越快
    private static final float addRot = 0.1f;
    private float sign;

    private char loc;
    private int rotType;

    public Rotation(char loc, int rot) {
        moveStatus = true;
        float extraSign;
        switch (loc) {
            case 'F':
                rotX = 2;
                rotY = -1;
                rotZ = -1;
                axisX = 0f;
                axisY = 0f;
                axisZ = 1f;
                extraSign = -1;
                break;
            case 'B':
                rotX = 0;
                rotY = -1;
                rotZ = -1;
                axisX = 0f;
                axisY = 0f;
                axisZ = 1f;
                extraSign = 1;
                break;
            case 'R':
                rotX = -1;
                rotY = 2;
                rotZ = -1;
                axisX = 1f;
                axisY = 0f;
                axisZ = 0f;
                extraSign = -1;
                break;
            case 'L':
                rotX = -1;
                rotY = 0;
                rotZ = -1;
                axisX = 1f;
                axisY = 0f;
                axisZ = 0f;
                extraSign = 1;
                break;
            case 'U':
                rotX = -1;
                rotY = -1;
                rotZ = 2;
                axisX = 0f;
                axisY = 1f;
                axisZ = 0f;
                extraSign = -1;
                break;
            case 'D':
                rotX = -1;
                rotY = -1;
                rotZ = 0;
                axisX = 0f;
                axisY = 1f;
                axisZ = 0f;
                extraSign = 1;
                break;
            default:
                throw new RuntimeException("参数错误");
        }
        switch (rot){
            case 1:
                this.rot = 90 * extraSign;
                break;
            case 2:
                this.rot = 180 * extraSign;
                break;
            case 3:
                this.rot = -90 * extraSign;
                break;
            default:
                throw new RuntimeException("参数错误");
        }
        curRot = 0f;
        //设置符号
        if (this.rot > 0) {
            sign = 1f;
        } else {
            sign = -1f;
        }
        //状态保存
        this.loc = loc;
        this.rotType = rot;
    }

    public float move() {
        if (moveStatus) {
            if (curRot >= rot * sign) {
                curRot = rot * sign;
                moveStatus = false;
            } else curRot += addRot;
            return curRot * sign;
        } else {
            return rot;
        }
    }

    public boolean isMove() {
        return moveStatus;
    }


    public int getRotX() {
        return rotX;
    }

    public int getRotY() {
        return rotY;
    }

    public int getRotZ() {
        return rotZ;
    }

    public float getAxisX() {
        return axisX;
    }

    public float getAxisY() {
        return axisY;
    }

    public float getAxisZ() {
        return axisZ;
    }

    public int getRotType() {
        return rotType;
    }

    public char getLoc() {
        return loc;
    }
}
