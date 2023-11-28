package com.bear.reseeding.entity;


import java.util.Date;
import java.io.Serializable;

/**
 * 航点任务表(EfTaskWps)实体类
 *
 * @author cwk
 * @since 2022-03-29 15:10:00
 */
public class EfTaskWps implements Serializable {
    private static final long serialVersionUID = -42083000728460059L;
    /**
     * 主键自增，任务编号
     */
    private Integer  id;
    /**
     * 任务名称
     */
    private String wpsName;
    /**
     * 任务类型，默认0为翼飞任务，1为大疆任务
     */
    private Integer wpsType;
    /**
     * 任务大致所在纬度
     */
    private Double wpsLat;
    /**
     * 任务大致所在经度
     */
    private Double wpsLng;
    /**
     * 任务高度，根据AltType确定是海拔还是相对高度
     */
    private double wpsAlt;
    /**
     * 任务航程
     */
    private double wpsDistance;
    /**
     * 任务预计用时
     */
    private Integer wpsUserTime;
    /**
     * 航点数量
     */
    private Integer wpsWpCount;
    /**
     * 任务位置
     */
    private String wpsLocation;
    /**
     * 海拔，某些航点使用海拔飞行
     */
    private double wpsAltAbs;
    /**
     * 任务创建时间
     */
    private Date wpsCreateTime;
    /**
     * 外键：任务创建人
     */
    private Integer wpsCreateByUserId;
    /**
     * 任务最新修改时间
     */
    private Date wpsUpdateTime;
    /**
     * 外键：任务最新修改人
     */
    private Integer wpsUpdateByUserId;
    /**
     * 任务描述与说明
     */
    private String wpsDes;
    /**
     * 外键：任务所属公司对象
     */
    private Integer wpsCompanyId;
    /**
     * 任务飞行速度，米每秒
     */
    private Float wpsSpeed;
    /**
     * 任务飞行最大速度，米每秒
     */
    private Float wpsMaxSpeed;
    /**
     * 确定当飞机与遥控器之间的连接丢失时，任务是否应该停止,0 继续任务，1 终止。
     */
    private Integer wpsRcSignalLost;
    /**
     * 航点任务完成后，飞机将采取的行动
     */
    private Integer wpsFinishedAction;
    /**
     * 飞机在航点之间遵循的路径。飞机可以直接在航路点之间沿直线飞行，也可以在弯道中的航路点附近转弯，航路点位置定义了弯路的一部分。
     */
    private Integer wpsFlightPathMode;
    /**
     * 定义飞机如何从当前位置前往第一个航点。默认值为SAFELY。
     */
    private Integer wpsGotoWaypointMode;
    /**
     * 飞机在航点之间移动时的航向。默认值为AUTO。
     */
    private Integer wpsHeadingMode;
    /**
     * 任务执行次数，1-255
     */
    private Integer wpsRepeatTimes;
    /**
     * 外键：航线任务分组，默认为0，未分组
     */
    private Integer  wpsGroup;
    /**
     * dji任务版本0,1，目前v1,v2
     */
    private Integer wpsVersion;
    /**
     * 0相对高度、1海拔高度飞行
     */
    private boolean  wpsAltMode;
    /**
     * DJI任务航点任务期间可以控制云台俯仰旋转
     */
    private boolean wpsGimbalPitchRotationEnabled;
    /**
     * 是否使用当前设置的高度为起飞点海拔高度
     */
    private boolean wpsUseThisHomeAltAbs;
    /**
     * 起飞点海拔
     */
    private double wpsHomeAltAbs;

    private EfUser efUserCreate;

    private EfUser efUserUpdate;

    /**
     * 公司信息
     */
    private EfCompany efCompany;

    public EfUser getEfUserCreate() {
        return efUserCreate;
    }

    public void setEfUserCreate(EfUser efUserCreate) {
        this.efUserCreate = efUserCreate;
    }

    public EfUser getEfUserUpdate() {
        return efUserUpdate;
    }

    public void setEfUserUpdate(EfUser efUserUpdate) {
        this.efUserUpdate = efUserUpdate;
    }

    public EfCompany getEfCompany() {
        return efCompany;
    }

    public void setEfCompany(EfCompany efCompany) {
        this.efCompany = efCompany;
    }

    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    public String getWpsName() {
        return wpsName;
    }

    public void setWpsName(String wpsName) {
        this.wpsName = wpsName;
    }

    public Integer getWpsType() {
        return wpsType;
    }

    public void setWpsType(Integer wpsType) {
        this.wpsType = wpsType;
    }

    public Double getWpsLat() {
        return wpsLat;
    }

    public void setWpsLat(Double wpsLat) {
        this.wpsLat = wpsLat;
    }

    public Double getWpsLng() {
        return wpsLng;
    }

    public void setWpsLng(Double wpsLng) {
        this.wpsLng = wpsLng;
    }

    public double getWpsAlt() {
        return wpsAlt;
    }

    public void setWpsAlt(double wpsAlt) {
        this.wpsAlt = wpsAlt;
    }

    public double getWpsDistance() {
        return wpsDistance;
    }

    public void setWpsDistance(double wpsDistance) {
        this.wpsDistance = wpsDistance;
    }

    public Integer getWpsUserTime() {
        return wpsUserTime;
    }

    public void setWpsUserTime(Integer wpsUserTime) {
        this.wpsUserTime = wpsUserTime;
    }

    public Integer getWpsWpCount() {
        return wpsWpCount;
    }

    public void setWpsWpCount(Integer wpsWpCount) {
        this.wpsWpCount = wpsWpCount;
    }

    public String getWpsLocation() {
        return wpsLocation;
    }

    public void setWpsLocation(String wpsLocation) {
        this.wpsLocation = wpsLocation;
    }

    public double getWpsAltAbs() {
        return wpsAltAbs;
    }

    public void setWpsAltAbs(double wpsAltAbs) {
        this.wpsAltAbs = wpsAltAbs;
    }

    public Date getWpsCreateTime() {
        return wpsCreateTime;
    }

    public void setWpsCreateTime(Date wpsCreateTime) {
        this.wpsCreateTime = wpsCreateTime;
    }

    public Integer getWpsCreateByUserId() {
        return wpsCreateByUserId;
    }

    public void setWpsCreateByUserId(Integer wpsCreateByUserId) {
        this.wpsCreateByUserId = wpsCreateByUserId;
    }

    public Date getWpsUpdateTime() {
        return wpsUpdateTime;
    }

    public void setWpsUpdateTime(Date wpsUpdateTime) {
        this.wpsUpdateTime = wpsUpdateTime;
    }

    public Integer getWpsUpdateByUserId() {
        return wpsUpdateByUserId;
    }

    public void setWpsUpdateByUserId(Integer wpsUpdateByUserId) {
        this.wpsUpdateByUserId = wpsUpdateByUserId;
    }

    public String getWpsDes() {
        return wpsDes;
    }

    public void setWpsDes(String wpsDes) {
        this.wpsDes = wpsDes;
    }

    public Integer getWpsCompanyId() {
        return wpsCompanyId;
    }

    public void setWpsCompanyId(Integer wpsCompanyId) {
        this.wpsCompanyId = wpsCompanyId;
    }

    public Float getWpsSpeed() {
        return wpsSpeed;
    }

    public void setWpsSpeed(Float wpsSpeed) {
        this.wpsSpeed = wpsSpeed;
    }

    public Float getWpsMaxSpeed() {
        return wpsMaxSpeed;
    }

    public void setWpsMaxSpeed(Float wpsMaxSpeed) {
        this.wpsMaxSpeed = wpsMaxSpeed;
    }

    public Integer getWpsRcSignalLost() {
        return wpsRcSignalLost;
    }

    public void setWpsRcSignalLost(Integer wpsRcSignalLost) {
        this.wpsRcSignalLost = wpsRcSignalLost;
    }

    public Integer getWpsFinishedAction() {
        return wpsFinishedAction;
    }

    public void setWpsFinishedAction(Integer wpsFinishedAction) {
        this.wpsFinishedAction = wpsFinishedAction;
    }

    public Integer getWpsFlightPathMode() {
        return wpsFlightPathMode;
    }

    public void setWpsFlightPathMode(Integer wpsFlightPathMode) {
        this.wpsFlightPathMode = wpsFlightPathMode;
    }

    public Integer getWpsGotoWaypointMode() {
        return wpsGotoWaypointMode;
    }

    public void setWpsGotoWaypointMode(Integer wpsGotoWaypointMode) {
        this.wpsGotoWaypointMode = wpsGotoWaypointMode;
    }

    public Integer getWpsHeadingMode() {
        return wpsHeadingMode;
    }

    public void setWpsHeadingMode(Integer wpsHeadingMode) {
        this.wpsHeadingMode = wpsHeadingMode;
    }

    public Integer getWpsRepeatTimes() {
        return wpsRepeatTimes;
    }

    public void setWpsRepeatTimes(Integer wpsRepeatTimes) {
        this.wpsRepeatTimes = wpsRepeatTimes;
    }

    public Integer  getWpsGroup() {
        return wpsGroup;
    }

    public void setWpsGroup(Integer  wpsGroup) {
        this.wpsGroup = wpsGroup;
    }

    public Integer getWpsVersion() {
        return wpsVersion;
    }

    public void setWpsVersion(Integer wpsVersion) {
        this.wpsVersion = wpsVersion;
    }

    public Boolean  getWpsAltMode() {
        return wpsAltMode;
    }

    public void setWpsAltMode(Boolean  wpsAltMode) {
        this.wpsAltMode = wpsAltMode;
    }

    public boolean getWpsGimbalPitchRotationEnabled() {
        return wpsGimbalPitchRotationEnabled;
    }

    public void setWpsGimbalPitchRotationEnabled(boolean wpsGimbalPitchRotationEnabled) {
        this.wpsGimbalPitchRotationEnabled = wpsGimbalPitchRotationEnabled;
    }

    public Boolean getWpsUseThisHomeAltAbs() {
        return wpsUseThisHomeAltAbs;
    }

    public void setWpsUseThisHomeAltAbs(Boolean wpsUseThisHomeAltAbs) {
        this.wpsUseThisHomeAltAbs = wpsUseThisHomeAltAbs;
    }

    public double getWpsHomeAltAbs() {
        return wpsHomeAltAbs;
    }

    public void setWpsHomeAltAbs(double wpsHomeAltAbs) {
        this.wpsHomeAltAbs = wpsHomeAltAbs;
    }

    @Override
    public String toString() {
        return "EfTaskWps{" +
                "id=" + id +
                ", wpsName='" + wpsName + '\'' +
                ", wpsType=" + wpsType +
                ", wpsLat=" + wpsLat +
                ", wpsLng=" + wpsLng +
                ", wpsAlt=" + wpsAlt +
                ", wpsDistance=" + wpsDistance +
                ", wpsUserTime=" + wpsUserTime +
                ", wpsWpCount=" + wpsWpCount +
                ", wpsLocation='" + wpsLocation + '\'' +
                ", wpsAltAbs=" + wpsAltAbs +
                ", wpsCreateTime=" + wpsCreateTime +
                ", wpsCreateByUserId=" + wpsCreateByUserId +
                ", wpsUpdateTime=" + wpsUpdateTime +
                ", wpsUpdateByUserId=" + wpsUpdateByUserId +
                ", wpsDes='" + wpsDes + '\'' +
                ", wpsCompanyId=" + wpsCompanyId +
                ", wpsSpeed=" + wpsSpeed +
                ", wpsMaxSpeed=" + wpsMaxSpeed +
                ", wpsRcSignalLost=" + wpsRcSignalLost +
                ", wpsFinishedAction=" + wpsFinishedAction +
                ", wpsFlightPathMode=" + wpsFlightPathMode +
                ", wpsGotoWaypointMode=" + wpsGotoWaypointMode +
                ", wpsHeadingMode=" + wpsHeadingMode +
                ", wpsRepeatTimes=" + wpsRepeatTimes +
                ", wpsGroup=" + wpsGroup +
                ", wpsVersion=" + wpsVersion +
                ", wpsAltMode=" + wpsAltMode +
                ", wpsGimbalPitchRotationEnabled=" + wpsGimbalPitchRotationEnabled +
                ", wpsUseThisHomeAltAbs=" + wpsUseThisHomeAltAbs +
                ", wpsHomeAltAbs=" + wpsHomeAltAbs +
                ", efUserCreate=" + efUserCreate +
                ", efUserUpdate=" + efUserUpdate +
                ", efCompany=" + efCompany +
                '}';
    }
}

