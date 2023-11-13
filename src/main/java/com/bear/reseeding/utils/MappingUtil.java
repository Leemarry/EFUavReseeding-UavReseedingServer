package com.bear.reseeding.utils;

/**
 * 测绘工具类
 *
 * @Auther: bear
 * @Date: 2023/4/7 10:36
 * @Description: null
 */
public class MappingUtil {


    /**
     * 度转弧度
     *
     * @param d 度
     * @return 弧度
     */
    private static double toRadian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 弧度转度
     *
     * @param radians 弧度
     * @return 度
     */
    private static double toDegrees(double radians) {
        double radToDegFactor = 180 / Math.PI;
        return radians * radToDegFactor;
    }

    /**
     * 根据相机参数计算水平与垂直的视场角度
     * <p>
     * 举例：  视场角大小和CMOS传感器尺寸和镜头焦距有关。
     * H水平视场角 = 2 × arctan（w / 2f）;
     * V垂直视场角 = 2 × arctan（h / 2f）;
     * 视场角 = 2 × arctan（d / 2f）;
     * w为CMOS的宽，h为CMOS的高，d为CMOS对角线长。
     * 以禅思P1 35mm镜头为例，CMOS尺寸为35.9×24 mm，带入公式计算：
     * <p>
     * 水平视场角= 2 × arctan（35 .9/ 70）= 54.3°。
     *
     * @param focalLength 焦距 mm
     * @param sensorW     CMOS像元大小，长度, 毫米
     * @param sensorH     CMOS像元大小，高度, 毫米
     * @return double[3] ， 0水平视场角，1垂直视场角 ，2视场角
     */
    public static double[] calculateViewAngle(double focalLength, double sensorW, double sensorH) {
        double angleH = 2 * toDegrees(Math.atan(sensorW / 2 * focalLength));
        double angleV = 2 * toDegrees(Math.atan(sensorH / 2 * focalLength));
        double d = Math.sqrt(Math.pow(sensorW, 2) + Math.pow(sensorH, 2)); // 对角线尺寸，mm
        double angle = 2 * toDegrees(Math.atan(d / 2 * focalLength));
        return new double[]{angleH, angleV, angle};
    }

    /**
     * 计算图片映射到地面的长宽，单位:米
     *
     * @param alt    飞行高度
     * @param pitch  相机俯仰倾斜角度，朝下为0度，水平超前为90度
     * @param angleH 水平视场角度
     * @param angleV 垂直视场角度
     * @return double[3] ， 0水平视场距离H米，1垂直视场距离V米 ，2斜角距离D米
     */
    public static double[] calculateViewMeter(double alt, double pitch, double angleH, double angleV) {
        // double rollRad = toRadian(roll);
        double pitchRad = toRadian(Math.abs(pitch));
        double distanceH = alt * Math.tan(toRadian(angleH) / 2) * 2; //水平视场距离
        double distanceV = alt * Math.tan(toRadian(angleV) / 2) * 2; //垂直视场距离
        distanceV = distanceV / Math.cos(pitchRad);
        double distance = Math.sqrt(Math.pow(distanceH, 2) + Math.pow(distanceV, 2)); // 对角线距离
        return new double[]{angleH, angleV, distance};
    }


    /**
     * 根据相机视场角度和拍摄坐标计算图片的四个角的坐标
     *
     * @param centerLat 拍照纬度
     * @param centerLng 拍照经度
     * @param alt       飞行高度
     * @param pitch     相机俯仰倾斜角度，朝下为0度，水平超前为90度
     * @param yaw       照片角度，以真北为0度
     * @param angleH    水平视场角度
     * @param angleV    垂直视场角度
     * @return double[4] ， 0左上坐标， 1右上坐标 ， 2右下坐标， 3左下坐标
     */
    public static double[][] calculateViewPos(double centerLat, double centerLng, double alt, double pitch, double yaw, double angleH, double angleV) {
        double[] temps = calculateViewMeter(alt, pitch, angleH, angleV);// 0水平视场距离H米，1垂直视场距离V米 ，2斜角距离D米
        return calculateViewPos(centerLat, centerLng, yaw, temps[0], temps[1]);
    }


    /**
     * 根据相机视场距离和拍摄坐标计算图片的四个角的坐标
     *
     * @param centerLat 拍照纬度
     * @param centerLng 拍照经度
     * @param yaw       照片角度，以真北为0度
     * @param distanceH 水平距离n米
     * @param distanceV 垂直距离n米
     * @return double[4] ， 0左上坐标， 1右上坐标 ， 2右下坐标， 3左下坐标
     */
    public static double[][] calculateViewPos(double centerLat, double centerLng, double yaw, double distanceH, double distanceV) {
        double distance = Math.sqrt(Math.pow(distanceH, 2) + Math.pow(distanceV, 2)) / 2; //中心点与四个角的距离
        double yaw1 = toDegrees(Math.atan((distanceV / 2) / (distanceH / 2)));   // 较小的一个角度
        double yaw2 = 90 - yaw1;  // 大的角度
        double[] leftTop = GisUtil.findPointAtDistanceFrom(centerLat, centerLng, yaw - yaw2, distance);
        double[] rightTop = GisUtil.findPointAtDistanceFrom(centerLat, centerLng, yaw + yaw2, distance);
        double[] rightBottom = GisUtil.findPointAtDistanceFrom(centerLat, centerLng, yaw + 90 + yaw1, distance);
        double[] leftBottom = GisUtil.findPointAtDistanceFrom(centerLat, centerLng, yaw - 90 - yaw1, distance);
        return new double[][]{leftTop, rightTop, rightBottom, leftBottom};
    }

}
