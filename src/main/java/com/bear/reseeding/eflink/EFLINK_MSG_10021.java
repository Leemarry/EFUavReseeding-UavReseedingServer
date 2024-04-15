package com.bear.reseeding.eflink;

import java.io.Serializable;

/**
 * 草原空斑照片与参数 ， 通过websocket推送进度给前台显示实时照片，推送内容为Result，数据存放于Result-Data中。
 * Data数据字段如下
 */
public class EFLINK_MSG_10021 implements Serializable {

    public final int EFLINK_MSG_ID = 10021;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 18;

    String uavId;
    long time;
    String url;
    double lat;
    double lng;
    double alt;
    double altAbs;
    double yaw;
    double square;
    Integer level;
    double seedNumber;
    int id;


    public EFLINK_MSG_10021() {
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_10010{" +
                "uavId='" + uavId + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", alt=" + alt +
                ", altAbs=" + altAbs +
                ", yaw=" + yaw +
                ", square=" + square +
                ", level=" + level +
                ", seedNumber=" + seedNumber +
                ", id=" + id +
                '}';
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public double getSeedNumber() {
        return seedNumber;
    }

    public void setSeedNumber(double seedNumber) {
        this.seedNumber = seedNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getAltAbs() {
        return altAbs;
    }

    public void setAltAbs(double altAbs) {
        this.altAbs = altAbs;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
}
