package com.model;

public enum CubeColor {
    //用黑色代表其他颜色
    OTHER(0, "无", 0xFF000000),
    BLUE(1, "蓝", 0xFF0051BA), RED(2, "红", 0xFFC41E3A), GREEN(3, "绿", 0xFF009E60),
    YELLOW(4, "黄", 0xFFFFD500), ORANGE(5, "橙", 0xFFFF5800), WHITE(6, "白", 0xFFFFFFFF);

    public int getId() {
        return id;
    }

    public String getCNName() {
        return cnName;
    }

    public int getPixel() {
        return pixel;
    }

    private int id;
    private String cnName;
    private int pixel;

    CubeColor(int id, String cnName, int pixel) {
        this.id = id;
        this.cnName = cnName;
        this.pixel = pixel;
    }
}