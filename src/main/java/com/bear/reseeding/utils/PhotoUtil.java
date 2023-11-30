package com.bear.reseeding.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: bear
 * @Date: 2023/11/30 16:30
 * @Description: null
 */
public class PhotoUtil {
    /**
     * 获取图片的地理位置
     *
     * @param lat 拍摄所在纬度
     * @param lng 拍摄所在经度
     * @return
     */
    public static String getPlace(double lat, double lng) {
        String addressFull = "";
        String name = null;
        String city = null;
        String township = null;
        String province = null;
        String district = null;
        if (lat != 0 && lng != 0) {
            try {
                String address = "https://restapi.amap.com/v3/geocode/regeo?location=" + lng + "," + lat + "&key=690ee7d7356fc5f1d90c0f1d3a650d70&radius=1000&extensions=all";
                address = HttpRequestUtil.HttpRequest(address);
                JSONObject jsonObject = JSONObject.parseObject(address);
                if (jsonObject.getIntValue("status") == 1) {
                    Object regeocode = jsonObject.get("regeocode");
                    JSONObject object2 = JSONObject.parseObject(regeocode.toString());
                    addressFull = object2.get("formatted_address").toString();
                    String obj = object2.get("addressComponent").toString();
                    JSONObject addressComponent = JSONArray.parseObject(obj);
                    city = addressComponent.get("city").toString();
                    province = addressComponent.get("province").toString();
                    district = addressComponent.get("district").toString();
                    String obj1 = addressComponent.get("streetNumber").toString();
                    JSONObject streetNumber = JSONArray.parseObject(obj1);
                    name = streetNumber.get("street").toString();
                    township = addressComponent.get("township").toString();
                    if (province == null || "[]".equals(province)) {
                        province = "";
                    }
                    if (city == null || "[]".equals(city)) {
                        city = province;
                    }
                    if (township == null || "[]".equals(township)) {
                        township = "";
                    }
                    return addressFull;
                } else {
                    return "";
                }
            } catch (Exception e) {
                LogUtil.logError("解析经纬度到地理位置异常：" + e.toString());
                return "获取地理位置异常！";
            }
        } else {
            return "解析经纬度异常！";
        }
    }
}
