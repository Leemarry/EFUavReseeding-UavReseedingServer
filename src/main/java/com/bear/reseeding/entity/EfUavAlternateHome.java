package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 无人机备降点表，一个无人机可设置多个备降点(EfUavAlternateHome)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:49
 */
public class EfUavAlternateHome implements Serializable {
    private static final long serialVersionUID = -74589531086085600L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 无人机唯一标识
     */
    private String uavId;
    /**
     * 停机坪唯一标识
     */
    private String hiveId;
    /**
     * 纬度
     */
    private double lat;
    /**
     * 经度
     */
    private double lng;
    /**
     * 高度，米
     */
    private double alt;
    /**
     * 海拔高度，米
     */
    private double altAbs;
    /**
     * 地理位置，文字描述大概位置
     */
    private String address;
    /**
     * 备降点可用面积，单位平方米，如2平方米
     */
    private double area;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public String getHiveId() {
        return hiveId;
    }

    public void setHiveId(String hiveId) {
        this.hiveId = hiveId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

}
