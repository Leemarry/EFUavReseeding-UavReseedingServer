package com.bear.reseeding.entity;


public class reseedPoint {
    /**
     * 纬度
     */
    private double latitude;

    private double longitude;

    private double height;

    private Integer seednum;


    public reseedPoint(double prop1, double prop2, double prop3, Integer prop4) {
        this.latitude = prop1;
        this.longitude = prop2;
        this.height = prop3;
        this.seednum = prop4;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Integer getSeednum() {
        return seednum;
    }

    public void setSeednum(Integer seednum) {
        this.seednum = seednum;
    }


}
