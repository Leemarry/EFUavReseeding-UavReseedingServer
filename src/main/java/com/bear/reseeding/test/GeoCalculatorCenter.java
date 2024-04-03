package com.bear.reseeding.test;

public class GeoCalculatorCenter {
    // 计算四个点的中心点坐标
    public static double[] calculateCenter(double[][] points) {
        double sumLat = 0.0;
        double sumLon = 0.0;

        // 计算经纬度之和
        for (double[] point : points) {
            sumLat += point[0];
            sumLon += point[1];
        }

        // 计算平均值
        double avgLat = sumLat / points.length;
        double avgLon = sumLon / points.length;

        return new double[]{avgLat, avgLon};
    }

    public static void main(String[] args) {
        // 四个点的经纬度坐标
        double[][] points = {
                {39.907239009009004, 116.45079194256633}, // 第一个点
                // 后续点的经纬度坐标依次类推
                {39.907239009009004, 116.42730605743367},
                {39.88922099099099, 116.42730605743367},
                {39.88922099099099, 116.45079194256633}

        };

        // 计算中心点坐标
        double[] center = calculateCenter(points);

        // 打印结果
        System.out.println("中心点坐标：" + center[0] + ", " + center[1]);
    }
}