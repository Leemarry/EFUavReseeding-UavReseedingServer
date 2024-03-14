package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 草原空洞表，与照片和飞行架次关联(EfCavity)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:43:09
 */
public class EfCavity implements Serializable {
    private static final long serialVersionUID = -85204994348555703L;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 空洞名称，一般不需要
     */
    private String cavityName;
    /**
     * 所在纬度
     */
    private Double lat;
    /**
     * 所在经度
     */
    private Double lng;
    /**
     * 相对高度
     */
    private Float alt;
    /**
     * 海拔高度
     */
    private Float altabs;
    /**
     * 空洞大小
     */
    private double size;
    /**
     * 退化等级
     */
    private Integer level;
    /**
     * 是否已经播种，播种数量，0表示未播种
     */
    private Integer seedingCount;
    /**
     * 外键：飞行架次ID
     */
    private Integer eachsortieId;
    /**
     * 外键：照片ID
     */
    private Integer photoId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCavityName() {
        return cavityName;
    }

    public void setCavityName(String cavityName) {
        this.cavityName = cavityName;
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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSeedingCount() {
        return seedingCount;
    }

    public void setSeedingCount(Integer seedingCount) {
        this.seedingCount = seedingCount;
    }

    public Integer getEachsortieId() {
        return eachsortieId;
    }

    public void setEachsortieId(Integer eachsortieId) {
        this.eachsortieId = eachsortieId;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

}
