package com.bear.reseeding.entity;

public class continuousWaypoints {
    private  Integer id;
    private double onlat;
    private double onlng;
    private double onalt;
    private double offlat;
    private double offlng;
    private double offalt;
    private int reseedingid;
    private int handleid;
    private  int flyTimes = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getOnlat() {
        return onlat;
    }

    public void setOnlat(double onlat) {
        this.onlat = onlat;
    }

    public double getOnlng() {
        return onlng;
    }

    public void setOnlng(double onlng) {
        this.onlng = onlng;
    }

    public double getOnalt() {
        return onalt;
    }

    public void setOnalt(double onalt) {
        this.onalt = onalt;
    }

    public double getOfflat() {
        return offlat;
    }

    public void setOfflat(double offlat) {
        this.offlat = offlat;
    }

    public double getOfflng() {
        return offlng;
    }

    public void setOfflng(double offlng) {
        this.offlng = offlng;
    }

    public double getOffalt() {
        return offalt;
    }

    public void setOffalt(double offalt) {
        this.offalt = offalt;
    }

    public int getReseedingid() {
        return reseedingid;
    }

    public void setReseedingid(int reseedingid) {
        this.reseedingid = reseedingid;
    }

    public int getHandleid() {
        return handleid;
    }

    public void setHandleid(int handleid) {
        this.handleid = handleid;
    }

    public int getFlyTimes() {
        return flyTimes;
    }

    public void setFlyTimes(int flyTimes) {
        this.flyTimes = flyTimes;
    }
}
