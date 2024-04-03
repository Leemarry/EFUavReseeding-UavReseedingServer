package com.bear.reseeding.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:59
 * @Description: null
 */
public class GisUtil {
    /**
     * 地球半径
     */
    private static double EarthRadius = 6378.137;

    // 地球平均半径（单位：千米）
    private static final double EARTH_RADIUS = 6371.0;

    // 每一度纬度的距离（单位：千米）
    private static final double DEGREE_LAT_DISTANCE = 111.0;

    // 每一度经度的距离（单位：千米）
    private static final double DEGREE_LON_DISTANCE = 111.0;


    /**
     * 经纬度转化成弧度
     *
     * @param d 经度/纬度
     * @return
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    // 弧度转度
    private static double toDegrees(double radians) {
        double radToDegFactor = 180 / Math.PI;
        return radians * radToDegFactor;
    }

    /**
     * 根据坐标获取地理位置信息
     *
     * @param lat 纬度
     * @param lng 经度
     * @return 地理位置
     */
    public static JSONObject getAddressByPos(double lat, double lng) {
        JSONObject jsonObject = null;
        try {
            double[] doubles = GisUtil.convertGpsToAmap(lat, lng);
            String address = null;
            if (doubles[0] != 0 && doubles[1] != 0) {
                String url = "https://restapi.amap.com/v3/geocode/regeo?location=" + doubles[1] + "," + doubles[0] + "&key=690ee7d7356fc5f1d90c0f1d3a650d70&radius=1000&extensions=all";
                address = HttpRequestUtil.HttpRequest(url);
            }
            if (address != null) {
                jsonObject = JSONObject.parseObject(address);
            }
        } catch (Exception e) {
            LogUtil.logError("获取地理位置异常：" + e.toString());
        }
        return jsonObject;
    }


    /**
     * 计算两个坐标点之间的距离
     *
     * @param firstLatitude   第一个坐标的纬度
     * @param firstLongitude  第一个坐标的经度
     * @param secondLatitude  第二个坐标的纬度
     * @param secondLongitude 第二个坐标的经度
     * @return 返回两点之间的距离，单位：米
     */
    public static double getDistance(double firstLatitude, double firstLongitude,
                                     double secondLatitude, double secondLongitude) {
        double firstRadLat = rad(firstLatitude);
        double firstRadLng = rad(firstLongitude);
        double secondRadLat = rad(secondLatitude);
        double secondRadLng = rad(secondLongitude);

        double a = firstRadLat - secondRadLat;
        double b = firstRadLng - secondRadLng;
        double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(firstRadLat)
                * Math.cos(secondRadLat) * Math.pow(Math.sin(b / 2), 2))) * EarthRadius;
        return Math.round(cal * 10000d) / 10000d * 1000;
    }

    public static double[] findPointAtDistanceFrom(double lat, double lng, double initialBearing, double meter) {
        if (initialBearing < 0) {
            initialBearing += 360;
        }
        if (initialBearing >= 360) {
            initialBearing %= 360;
        }
        double initialBearingRadians = rad(initialBearing);
        double distRatio = (meter / 1000) / EarthRadius;
        double distRatioSine = Math.sin(distRatio);
        double distRatioCosine = Math.cos(distRatio);

        double startLatRad = rad(lat);
        double startLonRad = rad(lng);

        double startLatCos = Math.cos(startLatRad);
        double startLatSin = Math.sin(startLatRad);

        double endLatRads = Math.asin((startLatSin * distRatioCosine) + (startLatCos * distRatioSine * Math.cos(initialBearingRadians)));

        double endLonRads = startLonRad
                + Math.atan2(
                Math.sin(initialBearingRadians) * distRatioSine * startLatCos,
                distRatioCosine - startLatSin * Math.sin(endLatRads));

        return new double[]{
                toDegrees(endLatRads), toDegrees(endLonRads)
        };
    }

    // 是否在中国区域内
    static boolean outOfChina(double lat, double lng) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    /**
     * 高德坐标 转 GPS坐标
     *
     * @param lat 高德坐标纬度
     * @param lon 高德坐标经度
     * @return double[2]经纬度数组，索引 0 纬度，索引 1 经度
     */
    public static double[] convertAmapToGps(double lat, double lon) {
        double[] latlng = ConvertPostion(lat, lon);
        return new double[]{
                lat - latlng[0], lon - latlng[1]
        };
        //return {lat:lat - latlng.lat, lng:lon - latlng.lng };
    }

    /**
     * 转换 GPS 为 高德坐标
     *
     * @param lat gps坐标纬度
     * @param lon gps坐标经度
     * @return double[2]经纬度数组，索引 0 纬度，索引 1 经度
     */
    public static double[] convertGpsToAmap(double lat, double lon) {
        double[] latlng = ConvertPostion(lat, lon);
        return new double[]{
                lat + latlng[0], lon + latlng[1]
        };
//        return {lat:lat + latlng.lat, lng:lon + latlng.lng };
    }

    // 转换坐标，获得差值
    static double[] ConvertPostion(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{
                    0, 0
            };
        } else {
            double PI = Math.PI;
            double a = 6378245.0;//  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
            double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
            double dLat = transformLat(lon - 105.0, lat - 35.0);
            double dLon = transformLon(lon - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * PI;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
            //return {lat:dLat, lng:dLon };
            return new double[]{
                    dLat, dLon
            };
        }
    }

    // 转换纬度
    static double transformLat(double x, double y) {
        double PI = Math.PI;
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    // 转换经度
    static double transformLon(double x, double y) {
        double PI = Math.PI;
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    // 计算经度与纬度
    public static double[][] calculateCoordinates(double lat0, double lon0, double distance) {
        // 计算正方形的四个角的经纬度
        double[][] coordinates = new double[4][2];

        // 计算东北角
        coordinates[0][0] = lat0 + (distance / DEGREE_LAT_DISTANCE);
        coordinates[0][1] = lon0 + (distance / (DEGREE_LON_DISTANCE * Math.cos(Math.toRadians(lat0))));

        // 计算西北角
        coordinates[1][0] = lat0 + (distance / DEGREE_LAT_DISTANCE);
        coordinates[1][1] = lon0 - (distance / (DEGREE_LON_DISTANCE * Math.cos(Math.toRadians(lat0))));

        // 计算西南角
        coordinates[2][0] = lat0 - (distance / DEGREE_LAT_DISTANCE);
        coordinates[2][1] = lon0 - (distance / (DEGREE_LON_DISTANCE * Math.cos(Math.toRadians(lat0))));

        // 计算东南角
        coordinates[3][0] = lat0 - (distance / DEGREE_LAT_DISTANCE);
        coordinates[3][1] = lon0 + (distance / (DEGREE_LON_DISTANCE * Math.cos(Math.toRadians(lat0))));

        return coordinates;
    }

    public static void main(String[] args) {
        double[] doubles = convertGpsToAmap(31.22492919, 109.02723639);
        double lat = doubles[0];
        double lng = doubles[1];
        System.out.println(lat);
        System.out.println(lng);

    }
}
