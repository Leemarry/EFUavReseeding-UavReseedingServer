package com.bear.reseeding.eflink.enums;

public enum GPSSignalLevel {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5),
    UNDEFINE(-1),
    NONE(255);

    private int data;

    private GPSSignalLevel(int var3) {
    }
    public static GPSSignalLevel valueIndexOf(int index) {   //   手写的从int到enum的转换函数
        try {
            return GPSSignalLevel.values()[index];
        } catch (Exception e) {
            return UNDEFINE;
        }
    }
    public static GPSSignalLevel find(int var0) {
        return null;
    }

    public int value() {
        return 0;
    }

    public boolean _equals(int var1) {
        return false;
    }
}
