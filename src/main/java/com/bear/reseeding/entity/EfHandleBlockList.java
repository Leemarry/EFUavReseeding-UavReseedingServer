package com.bear.reseeding.entity;

/**
 * 作业地块列表
 */
public class EfHandleBlockList {
    private Integer id ;
    /**
     * 空斑面积，单位 平方米
     */
    private double gapSquare;
    /**
     * 补播数量
     */
    private  Integer reseedAreaNum;
    /**
     * 作业地块:所需草种数量
     */
    private Integer seedNum;
    private double reseedSquare;
    private double centreLatitude;
    private double centreLongitude;


    /**
     * 外键：关联处理ID
     */
    private  Integer handleId;
    /**
     * 作业地块图
     */
    private  String img;  // 图片？ 作业图

    public double getReseedSquare() {
        return reseedSquare;
    }

    public void setReseedSquare(double reseedSquare) {
        this.reseedSquare = reseedSquare;
    }

    public double getCentreLatitude() {
        return centreLatitude;
    }

    public void setCentreLatitude(double centreLatitude) {
        this.centreLatitude = centreLatitude;
    }

    public double getCentreLongitude() {
        return centreLongitude;
    }

    public void setCentreLongitude(double centreLongitude) {
        this.centreLongitude = centreLongitude;
    }



    public Integer getReseedAreaNum() {
        return reseedAreaNum;
    }

    public void setReseedAreaNum(Integer reseedAreaNum) {
        this.reseedAreaNum = reseedAreaNum;
    }





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getHandleId() {
        return handleId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getGapSquare() {
        return gapSquare;
    }

    public void setGapSquare(double gapSquare) {
        this.gapSquare = gapSquare;
    }


    public Integer getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(Integer seedNum) {
        this.seedNum = seedNum;
    }







}
