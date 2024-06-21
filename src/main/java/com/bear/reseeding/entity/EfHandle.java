package com.bear.reseeding.entity;

import lombok.Builder;

import java.util.Date;
import java.io.Serializable;

/**
 * 处理记录表(EfHandle)实体类
 *
 * @author N.
 * @since 2024-03-13 16:10:41
 */

public class EfHandle implements Serializable {
    private static final long serialVersionUID = 347945384347757625L;
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 用于前端匹配的唯一标识符
     */
    private String handleUuid;
    /**
     * 处理时间
     */
    private Date date;
    /**
     * 原点纬度
     */
    private double lat;
    /**
     * 原点经度
     */
    private double lng;
    /**
     * 大地高
     */
    private double alt;
    /**
     * 补播无人机高度
     */
    private double flyAlt;
    /**
     * 补播机构参数,如口径大小等
     */
    private double loadP1;
    /**
     * 补播机构参数,如口径大小等
     */
    private double loadP2;
    /**
     * 补播机构参数,如口径大小等
     */
    private double loadP3;
    /**
     * 补播机构参数,如口径大小等
     */
    private double loadP4;
    /**
     * 补播机构参数,如口径大小等
     */
    private double loadP5;
    /**
     * 回传分析后：总空斑面积
     */
    private double gapSquare;
    /**
     * 回传分析后：补播区域数量
     */
    private double reseedAreaNum;
    /**
     * 回传分析后：补播区域面积
     */
    private double reseedSquare;
    /**
     * 回传分析后：补播草种数量
     */
    private Integer seedNum;

    public Integer getHandleUserid() {
        return handleUserid;
    }

    public void setHandleUserid(Integer handleUserid) {
        this.handleUserid = handleUserid;
    }

    /**
     * 回传分析后：一张草地退化等级评定图
     */
    private String imgLevel;

    private Integer handleUserid;


    public String getHandleUuid() {
        return handleUuid;
    }

    public void setHandleUuid(String handleUuid) {
        this.handleUuid = handleUuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public double getFlyAlt() {
        return flyAlt;
    }

    public void setFlyAlt(double flyAlt) {
        this.flyAlt = flyAlt;
    }

    public double getLoadP1() {
        return loadP1;
    }

    public void setLoadP1(double loadP1) {
        this.loadP1 = loadP1;
    }

    public double getLoadP2() {
        return loadP2;
    }

    public void setLoadP2(double loadP2) {
        this.loadP2 = loadP2;
    }

    public double getLoadP3() {
        return loadP3;
    }

    public void setLoadP3(double loadP3) {
        this.loadP3 = loadP3;
    }

    public double getLoadP4() {
        return loadP4;
    }

    public void setLoadP4(double loadP4) {
        this.loadP4 = loadP4;
    }

    public double getLoadP5() {
        return loadP5;
    }

    public void setLoadP5(double loadP5) {
        this.loadP5 = loadP5;
    }

    public double getGapSquare() {
        return gapSquare;
    }

    public void setGapSquare(double gapSquare) {
        this.gapSquare = gapSquare;
    }

    public double getReseedAreaNum() {
        return reseedAreaNum;
    }

    public void setReseedAreaNum(double reseedAreaNum) {
        this.reseedAreaNum = reseedAreaNum;
    }

    public double getReseedSquare() {
        return reseedSquare;
    }

    public void setReseedSquare(double reseedSquare) {
        this.reseedSquare = reseedSquare;
    }

    public Integer getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(Integer seedNum) {
        this.seedNum = seedNum;
    }

    public String getImgLevel() {
        return imgLevel;
    }

    public void setImgLevel(String imgLevel) {
        this.imgLevel = imgLevel;
    }

}

