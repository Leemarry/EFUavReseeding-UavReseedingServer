package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 草原空洞播种记录表(EfCavitySeeding)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:54:39
 */
public class EfCavitySeeding implements Serializable {
    private static final long serialVersionUID = -71318865163857720L;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 播种时间
     */
    private Date seedingTime;
    /**
     * 所在纬度
     */
    private Double lat;
    /**
     * 所在经度
     */
    private Double lng;
    /**
     * 播种时的飞行高度
     */
    private Float alt;
    /**
     * 海拔高度
     */
    private Float altabs;
    /**
     * 这次飞行时播种投放的种子数量
     */
    private Integer seedNumber;
    /**
     * 外键：空洞ID
     */
    private Integer cavityId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSeedingTime() {
        return seedingTime;
    }

    public void setSeedingTime(Date seedingTime) {
        this.seedingTime = seedingTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public Float getAltabs() {
        return altabs;
    }

    public void setAltabs(Float altabs) {
        this.altabs = altabs;
    }

    public Integer getSeedNumber() {
        return seedNumber;
    }

    public void setSeedNumber(Integer seedNumber) {
        this.seedNumber = seedNumber;
    }

    public Integer getCavityId() {
        return cavityId;
    }

    public void setCavityId(Integer cavityId) {
        this.cavityId = cavityId;
    }

}
