package com.bear.reseeding.utils;

/**
 * @Auther: bear
 * @Date: 2023/7/19 11:05
 * @Description: null
 */
public class ConvertUtil {
    /**
     * 转换为double类型
     *
     * @param obj
     * @return
     */
    public static double convertToDouble(Object obj, double defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        try {
            double temp = Double.parseDouble(obj.toString());
            if (Double.isNaN(temp)) {
                return defaultVal;
            } else {
                return temp;
            }
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 转换为int类型
     *
     * @param obj
     * @return
     */
    public static int convertToInt(Object obj, int defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        try {
            int temp = Integer.parseInt(obj.toString());
            if (Double.isNaN(temp)) {
                return defaultVal;
            } else {
                return temp;
            }
        } catch (Exception e) {
            return defaultVal;
        }
    }


    /**
     * 转换为 long 类型
     *
     * @param obj
     * @return
     */
    public static long convertToLong(Object obj, long defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        try {
            long temp = Long.parseLong(obj.toString());
            if (Double.isNaN(temp)) {
                return defaultVal;
            } else {
                return temp;
            }
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
