package com.serg.cellauto;

public class CellAutoUtil {
    public static int getStartPoint(int size) {
        return size / 2 - 60; //200 - 60 = 140
    }

    public static int getEndPoint(int size) {
        return size / 2 + 60; //200 + 60 = 260
    }


}
