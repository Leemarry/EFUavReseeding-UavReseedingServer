package com.bear.reseeding.test;

public class GeoCalculator {
    // 地球半径（单位：千米）
    private static final double EARTH_RADIUS = 6371.0;

    // 每一度纬度的距离（单位：千米）
    private static final double DEGREE_LAT_DISTANCE = 111.0;

    // 每一度经度的距离（单位：千米）
    private static final double DEGREE_LON_DISTANCE = 111.0;

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
        // 中心点的经纬度   [116.439049, 39.89823]
        double lat0 = 39.89823;
        double lon0 = 116.439049;

        // 正方形边长（单位：千米）
        double distance = 1.0;

        // 计算四个角的经纬度
        double[][] coordinates = calculateCoordinates(lat0, lon0, distance);

        // 打印结果
        System.out.println("东北角：" + coordinates[0][0] + ", " + coordinates[0][1]);
        System.out.println("西北角：" + coordinates[1][0] + ", " + coordinates[1][1]);
        System.out.println("西南角：" + coordinates[2][0] + ", " + coordinates[2][1]);
        System.out.println("东南角：" + coordinates[3][0] + ", " + coordinates[3][1]);
    }
}