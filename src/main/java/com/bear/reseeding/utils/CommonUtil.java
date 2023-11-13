package com.bear.reseeding.utils;


import java.util.HashMap;

/**
 * Request 处理类
 *
 * @Auther: bear
 * @Date: 2023/6/1 17:10
 * @Description: null
 */
public class CommonUtil {

    /**
     * 从map中获取某个key的值
     *
     * @param map 源Map
     * @param key Key
     * @return value
     */
    public static String getStrValueFromMap(HashMap<String, Object> map, String key) {
        return getStrValueFromMap(map, key, null);
    }

    /**
     * 从map中获取某个key的值
     *
     * @param map        源Map
     * @param key        Key
     * @param defaultVal 默认值
     * @return value
     */
    public static String getStrValueFromMap(HashMap<String, Object> map, String key, String defaultVal) {
        String result = defaultVal;
        try {
            Object obj = map.get(key);
            if (obj != null) {
                result = obj.toString();
            }
        } catch (Exception e) {
            LogUtil.logError("读取Map中的Key值异常：" + e.toString());
        }
        return result;
    }

    /**
     * 从map中获取某个key的值
     *
     * @param map        源Map
     * @param key        Key
     * @param defaultVal 默认值
     * @return value
     */
    public static double getDoubleValueFromMap(HashMap<String, Object> map, String key, double defaultVal) {
        double result = defaultVal;
        try {
            Object obj = map.get(key);
            if (obj != null) {
                result = Double.parseDouble(obj.toString());
            }
        } catch (Exception e) {
            LogUtil.logError("读取Map中的Key值异常：" + e.toString());
        }
        return result;
    }

    /**
     * 从map中获取某个key的值
     *
     * @param map        源Map
     * @param key        Key
     * @param defaultVal 默认值
     * @return value
     */
    public static long getLongValueFromMap(HashMap<String, Object> map, String key, long defaultVal) {
        long result = defaultVal;
        try {
            Object obj = map.get(key);
            if (obj != null) {
                result = Long.parseLong(obj.toString());
            }
        } catch (Exception e) {
            LogUtil.logError("读取Map中的Key值异常：" + e.toString());
        }
        return result;
    }
}
