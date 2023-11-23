package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户登录记录信息表
 * (EfUserLogin)实体类
 *
 * @author makejava
 * @since 2023-11-23 19:00:10
 */
public class EfUserLogin implements Serializable {
    private static final long serialVersionUID = -98964659550361538L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 用户主键ID
     */
    private Integer uUserId;
    /**
     * 登录用户名称
     */
    private String uLoginName;
    /**
     * 用户姓名
     */
    private String uName;
    /**
     * 登录时间
     */
    private Date uLoginTime;
    /**
     * 代理
     */
    private String uAgent;
    /**
     * 登录时的IP地址外网
     */
    private String uIpWww;
    /**
     * 登录时的IP地址内网
     */
    private String uIpLocal;
    /**
     * 登录客户机机器码
     */
    private String uMachineCode;
    /**
     * 退出登录时间
     */
    private Date uLoginOutTime;
    /**
     * 在线时长，秒
     */
    private Integer uOnlineTime;
    /**
     * 描述信息
     */
    private String uDescription;
    /**
     * 登录状态，0登录成功，-1表示登录失败
     */
    private Integer uStatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUUserId() {
        return uUserId;
    }

    public void setUUserId(Integer uUserId) {
        this.uUserId = uUserId;
    }

    public String getULoginName() {
        return uLoginName;
    }

    public void setULoginName(String uLoginName) {
        this.uLoginName = uLoginName;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public Date getULoginTime() {
        return uLoginTime;
    }

    public void setULoginTime(Date uLoginTime) {
        this.uLoginTime = uLoginTime;
    }

    public String getUAgent() {
        return uAgent;
    }

    public void setUAgent(String uAgent) {
        this.uAgent = uAgent;
    }

    public String getUIpWww() {
        return uIpWww;
    }

    public void setUIpWww(String uIpWww) {
        this.uIpWww = uIpWww;
    }

    public String getUIpLocal() {
        return uIpLocal;
    }

    public void setUIpLocal(String uIpLocal) {
        this.uIpLocal = uIpLocal;
    }

    public String getUMachineCode() {
        return uMachineCode;
    }

    public void setUMachineCode(String uMachineCode) {
        this.uMachineCode = uMachineCode;
    }

    public Date getULoginOutTime() {
        return uLoginOutTime;
    }

    public void setULoginOutTime(Date uLoginOutTime) {
        this.uLoginOutTime = uLoginOutTime;
    }

    public Integer getUOnlineTime() {
        return uOnlineTime;
    }

    public void setUOnlineTime(Integer uOnlineTime) {
        this.uOnlineTime = uOnlineTime;
    }

    public String getUDescription() {
        return uDescription;
    }

    public void setUDescription(String uDescription) {
        this.uDescription = uDescription;
    }

    public Integer getUStatus() {
        return uStatus;
    }

    public void setUStatus(Integer uStatus) {
        this.uStatus = uStatus;
    }

}
