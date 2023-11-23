package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机每飞行一个架次，产生一条记录。(EfUavEachsortie)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:58
 */
public class EfUavEachsortie implements Serializable {
    private static final long serialVersionUID = -31137681681879270L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 无人机编号
     */
    private String uavId;
    /**
     * 连接时间
     */
    private Date connectTime;
    /**
     * 实时电压百分比
     */
    private Float lastVoltage;
    /**
     * 解锁时间
     */
    private Date aremdTime;
    /**
     * 解锁时的电压
     */
    private Float aremdVoltage;
    /**
     * 飞行时长，秒
     */
    private Integer flyingTime;
    /**
     * 在线时长，秒
     */
    private Integer onlineTime;
    /**
     * 飞行航程，米
     */
    private Double airRange;
    /**
     * 锁定时间
     */
    private Date lockedTime;
    /**
     * 锁定时的电压
     */
    private Float lockedVoltage;
    /**
     * 飞行记录日志路径
     */
    private String flyLogPath;
    /**
     * 飞行视频路径
     */
    private String flyVideoPath;
    /**
     * 测绘地址
     */
    private String place;
    /**
     * 当前架次挂载的种子总数
     */
    private Integer seedNumberAll;
    /**
     * 当前架次已经投放的种子总数
     */
    private Integer seedNumberUsed;
    /**
     * 外键：飞行时的航线信息，可空
     */
    private Integer flyWpsId;
    /**
     * 任务信息
     */
    private String mission;


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

    public Date getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Date connectTime) {
        this.connectTime = connectTime;
    }

    public Float getLastVoltage() {
        return lastVoltage;
    }

    public void setLastVoltage(Float lastVoltage) {
        this.lastVoltage = lastVoltage;
    }

    public Date getAremdTime() {
        return aremdTime;
    }

    public void setAremdTime(Date aremdTime) {
        this.aremdTime = aremdTime;
    }

    public Float getAremdVoltage() {
        return aremdVoltage;
    }

    public void setAremdVoltage(Float aremdVoltage) {
        this.aremdVoltage = aremdVoltage;
    }

    public Integer getFlyingTime() {
        return flyingTime;
    }

    public void setFlyingTime(Integer flyingTime) {
        this.flyingTime = flyingTime;
    }

    public Integer getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Integer onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Double getAirRange() {
        return airRange;
    }

    public void setAirRange(Double airRange) {
        this.airRange = airRange;
    }

    public Date getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(Date lockedTime) {
        this.lockedTime = lockedTime;
    }

    public Float getLockedVoltage() {
        return lockedVoltage;
    }

    public void setLockedVoltage(Float lockedVoltage) {
        this.lockedVoltage = lockedVoltage;
    }

    public String getFlyLogPath() {
        return flyLogPath;
    }

    public void setFlyLogPath(String flyLogPath) {
        this.flyLogPath = flyLogPath;
    }

    public String getFlyVideoPath() {
        return flyVideoPath;
    }

    public void setFlyVideoPath(String flyVideoPath) {
        this.flyVideoPath = flyVideoPath;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSeedNumberAll() {
        return seedNumberAll;
    }

    public void setSeedNumberAll(Integer seedNumberAll) {
        this.seedNumberAll = seedNumberAll;
    }

    public Integer getSeedNumberUsed() {
        return seedNumberUsed;
    }

    public void setSeedNumberUsed(Integer seedNumberUsed) {
        this.seedNumberUsed = seedNumberUsed;
    }

    public Integer getFlyWpsId() {
        return flyWpsId;
    }

    public void setFlyWpsId(Integer flyWpsId) {
        this.flyWpsId = flyWpsId;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

}
