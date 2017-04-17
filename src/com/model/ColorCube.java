package com.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhengQinyu on 2016/4/30.
 */
public class ColorCube implements Serializable {

    /**
     * 6个面的顺序是 F:前 R：右 B：后 L：左 U：上 D：下
     */
    private final static String FACE_ORDER = "FRBLUD";

    public final static int LENGTH = 6;

    private ColorFace[] faces;

    public ColorCube() {
        faces = new ColorFace[LENGTH];
        for (int i = 0; i < LENGTH; ++i) {
            faces[i] = new ColorFace();
        }
    }

    public void setFace(int index, ColorFace face) {
        if (index < 0 || index > LENGTH - 1) {
            throw new RuntimeException("请输入正确的面！");
        }
        faces[index] = face;
    }

    public ColorFace getFace(int index) {
        if (index < 0 || index > LENGTH - 1) {
            throw new RuntimeException("请输入正确的面！");
        }
        return faces[index];
    }

    /**
     * @param face FRBLUD中的一个
     * @return
     */
    public ColorFace getFace(char face) {
        int index = FACE_ORDER.indexOf(face);
        if (index < 0 || index > LENGTH - 1) {
            throw new RuntimeException("请输入正确的面！");
        }
        return faces[index];
    }

    public void setFace(char face, ColorFace colorFace) {
        int index = FACE_ORDER.indexOf(face);
        faces[index] = colorFace;
    }

    public boolean isNotEmpty() {
        for (int i = 0; i < LENGTH; ++i) {
            if (faces[i] == null) {
                return false;
            }
        }
        return true;
    }

    public String toRubikCubeString() {
        Map<CubeColor, Character> mapper = new HashMap<>();
        for (int i = 0; i < LENGTH; ++i) {
            mapper.put(faces[i].getCenterColor(), FACE_ORDER.charAt(i));
        }
        StringBuilder strBuilder = new StringBuilder();
        //将面的顺序调整成 U R F D L B
        int[] newOrder = {4, 1, 0, 5, 3, 2};
        for (int i = 0; i < faces.length; ++i) {
            ColorFace face = faces[newOrder[i]];
            for (int j = 0; j < ColorFace.LENGTH; ++j) {
                //将颜色转成面表示
                strBuilder.append(mapper.get(face.getColor(j)));
            }
        }
        return strBuilder.toString();
    }

    /**
     * 颜色变换
     *
     * @param loc
     * @param rot
     */
    public void cubeRotation(char loc, int rot) {
        //90,180,-90
        int index[][] = {
                {6, 3, 0, 7, 4, 1, 8, 5, 2},
                {8, 7, 6, 5, 4, 3, 2, 1, 0},
                {2, 5, 8, 1, 4, 7, 0, 3, 6}
        };
        //面旋转
        ColorFace face = getFace(loc);
        ColorFace newFace = new ColorFace();
        for (int i = 0; i < 9; ++i) {
            newFace.setColor(i, face.getColor(index[rot - 1][i]));
        }
        this.setFace(loc, newFace);
        //侧边旋转
        switch (loc) {
            case 'F':
                changeColor(new char[]{'U', 'R', 'D', 'L'},
                        new int[][]{
                                {6, 7, 8}, {0, 3, 6},
                                {2, 1, 0}, {8, 5, 2}
                        }, rot);
                break;
            case 'B':
                changeColor(new char[]{'U', 'L', 'D', 'R'},
                        new int[][]{
                                {2, 1, 0}, {0, 3, 6},
                                {6, 7, 8}, {8, 5, 2}
                        }, rot);
                break;
            case 'R':
                changeColor(new char[]{'U', 'B', 'D', 'F'},
                        new int[][]{
                                {8, 5, 2}, {0, 3, 6},
                                {8, 5, 2}, {8, 5, 2}
                        }, rot);
                break;
            case 'L':
                changeColor(new char[]{'U', 'F', 'D', 'B'},
                        new int[][]{
                                {0, 3, 6}, {0, 3, 6},
                                {0, 3, 6}, {8, 5, 2}
                        }, rot);
                break;
            case 'U'://
                changeColor(new char[]{'B', 'R', 'F', 'L'},
                        new int[][]{
                                {2, 1, 0}, {2, 1, 0},
                                {2, 1, 0}, {2, 1, 0}
                        }, rot);
                break;
            case 'D':
                changeColor(new char[]{'F', 'R', 'B', 'L'},
                        new int[][]{
                                {6, 7, 8}, {6, 7, 8},
                                {6, 7, 8}, {6, 7, 8}
                        }, rot);
                break;
        }
    }

    //侧边旋转
    private void changeColor(char[] changeFace, int[][] index, int rot) {
        ColorFace[] cFaces = new ColorFace[4];
        for (int i = 0; i < 4; ++i) {
            cFaces[i] = this.getFace(changeFace[i]);
        }
        switch (rot) {
            case 1:
                for (int i = 4; i >= 2; --i) {
                    int id = i % 4;
                    exchangeColor(cFaces[id], index[id][0], index[id][1], index[id][2],
                            cFaces[i - 1], index[i - 1][0], index[i - 1][1], index[i - 1][2]);
                }
                break;
            case 2:
                for (int i = 0; i < 2; ++i) {
                    exchangeColor(cFaces[i], index[i][0], index[i][1], index[i][2],
                            cFaces[i + 2], index[i + 2][0], index[i + 2][1], index[i + 2][2]);
                }
                break;
            case 3:
                for (int i = 0; i < 3; ++i) {
                    exchangeColor(cFaces[i], index[i][0], index[i][1], index[i][2],
                            cFaces[i + 1], index[i + 1][0], index[i + 1][1], index[i + 1][2]);
                }
                break;
        }
    }

    //交换颜色
    private void exchangeColor(ColorFace tFace, int tLoc1, int tLoc2, int tLoc3,
                               ColorFace fFace, int fLoc1, int fLoc2, int fLoc3) {
        CubeColor c1 = tFace.getColor(tLoc1);
        CubeColor c2 = tFace.getColor(tLoc2);
        CubeColor c3 = tFace.getColor(tLoc3);
        tFace.setColor(tLoc1, fFace.getColor(fLoc1));
        tFace.setColor(tLoc2, fFace.getColor(fLoc2));
        tFace.setColor(tLoc3, fFace.getColor(fLoc3));
        fFace.setColor(fLoc1, c1);
        fFace.setColor(fLoc2, c2);
        fFace.setColor(fLoc3, c3);
    }
}
