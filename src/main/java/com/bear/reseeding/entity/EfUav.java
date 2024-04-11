package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机信息表(EfUav)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:48
 */
public class EfUav implements Serializable {
    private static final long serialVersionUID = -62790280969449409L;
    /**
     * 无人机编号
     */
    private String uavId;
    /**
     * 无人机名称
     */
    private String uavName;
    /**
     * 生产日期
     */
    private Date uavProductDate;
    /**
     * 生产工作人员
     */
    private String uavProductByUser;
    /**
     * 出厂日期
     */
    private Date uavSellDate;
    /**
     * 操作人
     */
    private String uavSellByUser;
    /**
     * 轴距,单位：米
     */
    private Object uavWheelbase;
    /**
     * 最大载重,单位：千克
     */
    private Object uavMaxpayload;
    /**
     * 续航时间,单位：分钟
     */
    private Object uavEnduranceTime;
    /**
     * 最大飞行速度，单位：米每秒
     */
    private Object uavMaxspeed;
    /**
     * 外键：合同编号，关联合同信息表
     */
    private String uavContractNo;
    /**
     * 外键：无人机类型，关联无人机类型表
     */
    private Integer uavTypeId;
    /**
     * 外键：当前使用的相机，手动配置
     */
    private Integer uavCurrentCamera;

    public String getUavModel() {
        return uavModel;
    }

    public void setUavModel(String uavModel) {
        this.uavModel = uavModel;
    }

    /**
     *无人机默认型号
     */
    private  String uavModel;

    /**
     * 逻辑状态默认0正常，-1：已删除
     */
    private Integer uavStatus;

    /**
     * 无人机类型信息
     */
    private  EfUavType efUavType;


    public EfUavType getEfUavType() {
        return efUavType;
    }

    public void setEfUavType(EfUavType efUavType) {
        this.efUavType = efUavType;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public String getUavName() {
        return uavName;
    }

    public void setUavName(String uavName) {
        this.uavName = uavName;
    }

    public Date getUavProductDate() {
        return uavProductDate;
    }

    public void setUavProductDate(Date uavProductDate) {
        this.uavProductDate = uavProductDate;
    }

    public String getUavProductByUser() {
        return uavProductByUser;
    }

    public void setUavProductByUser(String uavProductByUser) {
        this.uavProductByUser = uavProductByUser;
    }

    public Date getUavSellDate() {
        return uavSellDate;
    }

    public void setUavSellDate(Date uavSellDate) {
        this.uavSellDate = uavSellDate;
    }

    public String getUavSellByUser() {
        return uavSellByUser;
    }

    public void setUavSellByUser(String uavSellByUser) {
        this.uavSellByUser = uavSellByUser;
    }

    public Object getUavWheelbase() {
        return uavWheelbase;
    }

    public void setUavWheelbase(Object uavWheelbase) {
        this.uavWheelbase = uavWheelbase;
    }

    public Object getUavMaxpayload() {
        return uavMaxpayload;
    }

    public void setUavMaxpayload(Object uavMaxpayload) {
        this.uavMaxpayload = uavMaxpayload;
    }

    public Object getUavEnduranceTime() {
        return uavEnduranceTime;
    }

    public void setUavEnduranceTime(Object uavEnduranceTime) {
        this.uavEnduranceTime = uavEnduranceTime;
    }

    public Object getUavMaxspeed() {
        return uavMaxspeed;
    }

    public void setUavMaxspeed(Object uavMaxspeed) {
        this.uavMaxspeed = uavMaxspeed;
    }

    public String getUavContractNo() {
        return uavContractNo;
    }

    public void setUavContractNo(String uavContractNo) {
        this.uavContractNo = uavContractNo;
    }

    public Integer getUavTypeId() {
        return uavTypeId;
    }

    public void setUavTypeId(Integer uavTypeId) {
        this.uavTypeId = uavTypeId;
    }

    public Integer getUavCurrentCamera() {
        return uavCurrentCamera;
    }

    public void setUavCurrentCamera(Integer uavCurrentCamera) {
        this.uavCurrentCamera = uavCurrentCamera;
    }

    public Integer getUavStatus() {
        return uavStatus;
    }

    public void setUavStatus(Integer uavStatus) {
        this.uavStatus = uavStatus;
    }

}
