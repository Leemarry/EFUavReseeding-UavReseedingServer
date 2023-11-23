package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:59
 */
public class EfUavRealtimedata implements Serializable {
    private static final long serialVersionUID = 971909579040931150L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 无人机编号
     */
    private String uavId;
    /**
     * 数据时间
     */
    private Date dataDate;
    /**
     * 当前飞行模式
     */
    private String flightMode;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
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
     * 横滚
     */
    private Float roll;
    /**
     * 俯仰
     */
    private Float pitch;
    /**
     * 机头朝向
     */
    private Float yaw;
    /**
     * 飞行速度
     */
    private Float xySpeed;
    /**
     * 垂直速度
     */
    private Float zSpeed;
    /**
     * 定位状态
     */
    private Float gpsStatus;
    /**
     * 卫星颗数
     */
    private Integer satecount;
    /**
     * 无人机电压
     */
    private Float batteryValue;
    /**
     * 无人机电压百分比
     */
    private Integer batteryPert;
    /**
     * 无人机当前状态
     */
    private Float uavStatus;
    /**
     * 无人机是否存在异常，异常信息
     */
    private Float uavAbnormal;
    /**
     * 遥控与无人机通讯质量，下行
     */
    private Integer linkAirDownload;
    /**
     * 遥控与无人机通讯质量，上行
     */
    private Integer linkAirUpload;
    /**
     * 1已解锁，0未解锁
     */
    private Integer aremd;
    /**
     * 整个系统的运行状态
     */
    private Integer systemStatus;
    /**
     * 外键：无人机当前所连接的蜂巢编号，可空
     */
    private String uavCurentHive;


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

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public String getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(String flightMode) {
        this.flightMode = flightMode;
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

    public Float getRoll() {
        return roll;
    }

    public void setRoll(Float roll) {
        this.roll = roll;
    }

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public Float getXySpeed() {
        return xySpeed;
    }

    public void setXySpeed(Float xySpeed) {
        this.xySpeed = xySpeed;
    }

    public Float getZSpeed() {
        return zSpeed;
    }

    public void setZSpeed(Float zSpeed) {
        this.zSpeed = zSpeed;
    }

    public Float getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(Float gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Integer getSatecount() {
        return satecount;
    }

    public void setSatecount(Integer satecount) {
        this.satecount = satecount;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Integer getBatteryPert() {
        return batteryPert;
    }

    public void setBatteryPert(Integer batteryPert) {
        this.batteryPert = batteryPert;
    }

    public Float getUavStatus() {
        return uavStatus;
    }

    public void setUavStatus(Float uavStatus) {
        this.uavStatus = uavStatus;
    }

    public Float getUavAbnormal() {
        return uavAbnormal;
    }

    public void setUavAbnormal(Float uavAbnormal) {
        this.uavAbnormal = uavAbnormal;
    }

    public Integer getLinkAirDownload() {
        return linkAirDownload;
    }

    public void setLinkAirDownload(Integer linkAirDownload) {
        this.linkAirDownload = linkAirDownload;
    }

    public Integer getLinkAirUpload() {
        return linkAirUpload;
    }

    public void setLinkAirUpload(Integer linkAirUpload) {
        this.linkAirUpload = linkAirUpload;
    }

    public Integer getAremd() {
        return aremd;
    }

    public void setAremd(Integer aremd) {
        this.aremd = aremd;
    }

    public Integer getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(Integer systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getUavCurentHive() {
        return uavCurentHive;
    }

    public void setUavCurentHive(String uavCurentHive) {
        this.uavCurentHive = uavCurentHive;
    }

}
