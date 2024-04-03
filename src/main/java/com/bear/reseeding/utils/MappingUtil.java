package com.bear.reseeding.utils;


/**
 * 测绘航线计算
 */
public class MappingUtil {

    final double rad2deg = (180 / Math.PI);
    final double deg2rad = (1.0 / rad2deg);

    // 相机安装倾角，0-180度
    private double campitch = 0;

    // 相机焦距
    private double focallen = 0;

    // 传感器宽度，mm
    private double sensorwidth = 0;

    // 传感器高度，mm
    private double sensorheight = 0;

    // 图片宽度，像素
    private double imagewidth = 0;

    // 图片高度，像素
    private double imageheight = 0;

    // 比例尺，根据分辨率计算得出。一般根据比例尺去计算分辨率
    private double blc = 0;

    // 分辨率，根据比例尺计算得出。
    private double fbl = 0;

    // 航向重叠率 ， 1-99
    private double overlap = 0;

    // 旁向重叠率 ， 1-99
    private double sidelap = 0;

    // 摄像头是否朝前，默认true
    private boolean camTowardFront = true;

    /**
     * 构造函数
     *
     * @param campitch       相机安装倾角，0-180度
     * @param focallen       相机焦距
     * @param sensorwidth    传感器宽度，mm
     * @param sensorheight   传感器高度，mm
     * @param imagewidth     图片宽度，像素
     * @param imageheight    图片高度，像素
     * @param blc            比例尺
     * @param overlap        航向重叠率 ， 1-99
     * @param sidelap        旁向重叠率 ， 1-99
     * @param camTowardFront 摄像头是否朝前，默认true
     */
    public MappingUtil(double campitch, double focallen, double sensorwidth, double sensorheight, double imagewidth, double imageheight, double blc, double overlap, double sidelap, boolean camTowardFront) {
        this.campitch = campitch;
        this.focallen = focallen;
        this.sensorwidth = sensorwidth;
        this.sensorheight = sensorheight;
        this.imagewidth = imagewidth;
        this.imageheight = imageheight;
        this.blc = blc;
        this.overlap = overlap;
        this.sidelap = sidelap;
        this.camTowardFront = camTowardFront;
    }

    /**
     * 已知需要拍摄的比例尺分辨率，计算航线间距与旁向间距
     *
     * @return double[5]
     * values[0] = headingSpacing;  // 航线间距
     * values[1] = sideSpacing;  // 旁向间距
     * values[2] = flyalt;  // 飞行高度
     * values[3] = fbl;  // 分辨率
     * values[4] = cmpixel;  // 1像素等于多少厘米，厘米/像素
     */
    public double[] calculate() {
        // 分辨率 ,
        fbl = blc / 10000;

        // 1、计算飞行高度
        double blcnew = fbl / (sensorwidth / imagewidth) * 1000d;  // 比例尺 = 分辨率m / 相元大小 * 1000

        double flyalt = (focallen * blcnew / 1000 * Math.cos(campitch * Math.PI / 180d));

        // 2、计算航线间距与旁向间距
        double[] fovhv = getFOV();
        double viewwidth = fovhv[0];
        double viewheight = fovhv[1];
        //    mm  / pixels * 100
        double cmpixel = ((viewheight / imageheight) * 100);  //厘米/像素

        double spacing = 0; //航线间距
        double headingSpacing = 0;     //航向间距
        // 摄像头超前时
        if (camTowardFront) {
            spacing = ((1 - (sidelap / 100.0f)) * viewwidth); //航线间距
            headingSpacing = ((1 - (overlap / 100.0f)) * viewheight);  //航向间距
        } else {
            // 摄像头不超前
            spacing = ((1 - (sidelap / 100.0f)) * viewheight);  //航线间距
            headingSpacing = ((1 - (overlap / 100.0f)) * viewwidth);     //航向间距
        }

        double[] values = new double[5];
        values[0] = spacing;  // 航线间距
        values[1] = headingSpacing;  // 航向间距
        values[2] = flyalt;  // 飞行高度
        values[3] = fbl;  // 分辨率
        values[4] = cmpixel;  // 1像素等于多少厘米，厘米/像素
        return values;
    }


    public double[] getFOV() {
        double[] values = new double[2];
        // scale      mm / mm
        //double flscale = (1000 * flyalt) / focallen;
        //根据地面分辨率计算比例尺
        double xiangyuan = sensorwidth / imagewidth;//=传感器尺寸/对应分辨率
        double bilichi = fbl / xiangyuan * 1000;//=分辨率(米)/相元大小*1000;
        double flscale = bilichi;  //比例尺,单位：1/M
        //   mm * mm / 1000
        double viewwidth = (sensorwidth * flscale / 1000);
        double viewheight = (sensorheight * flscale / 1000);

        float fovh1 = (float) (Math.atan(sensorwidth / (2 * focallen)) * rad2deg * 2);
        float fovv1 = (float) (Math.atan(sensorheight / (2 * focallen)) * rad2deg * 2);

        values[0] = viewwidth;
        values[1] = viewheight;
        return values;
    }



}

