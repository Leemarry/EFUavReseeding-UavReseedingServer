package com.bear.reseeding.entity;

import com.google.gson.JsonElement;

import java.util.Date;
import java.io.Serializable;

/**
 * (EfUser)实体类
 *
 * @author makejava
 * @since 2023-11-23 19:00:08
 */
public class EfUser implements Serializable {
    private static final long serialVersionUID = 105230581684594708L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 登录名
     */
    private String uLoginName;
    /**
     * 登录密码
     */
    private String uLoginPassword;
    /**
     * 用户姓名
     */
    private String uName;
    /**
     * 年龄
     */
    private String uAge;
    /**
     * 电话
     */
    private String uPhone;
    /**
     * 地址
     */
    private String uAddress;
    /**
     * 邮箱地址
     */
    private String uEmail;
    /**
     * 性别
     */
    private Boolean uSex;
    /**
     * 用户创建日期
     */
    private Date uCreateDate;
    /**
     * 创建人
     */
    private String uCreateByUser;
    /**
     * 用户修改日期
     */
    private Date uUpdateDate;
    /**
     * 修改人
     */
    private String uUpdateByUser;
    /**
     * 图标路径
     */
    private String uIco;
    /**
     * 描述信息
     */
    private String uDescription;
    /**
     * 状态0正常，-1已删除，-2已冻结，-3已失效，-4...
     */
    private Integer uStatus;
    /**
     * 外键，用户所属角色
     */
    private Integer uRId;
    /**
     * 外键，用户所属公司
     */
    private Integer uCId;
    /**
     * 上级用户ID，默认0，直属所属公司的管理员
     */
    private Integer uParentId;
    /**
     * 在允许登录的情况下，是否可操作无人机，默认0允许，-1不允许
     */
    private Integer uLimitUav;
    /**
     * 在允许登录的情况下，是否可操作停机坪，默认0允许，-1不允许
     */
    private Integer uLimitHive;
    /**
     * 账户到期时间，结合所属公司的限制日期，该限制主要限制用户登录
     */
    private Date uLimitDate;

    /**
     * 外键，用户所属公司
     */
    private EfCompany efCompany;

    /**
     * 角色外键
     */
    private EfRole efRole;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getULoginName() {
        return uLoginName;
    }

    public void setULoginName(String uLoginName) {
        this.uLoginName = uLoginName;
    }

    public String getULoginPassword() {
        return uLoginPassword;
    }

    public void setULoginPassword(String uLoginPassword) {
        this.uLoginPassword = uLoginPassword;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUAge() {
        return uAge;
    }

    public void setUAge(String uAge) {
        this.uAge = uAge;
    }

    public String getUPhone() {
        return uPhone;
    }

    public void setUPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getUAddress() {
        return uAddress;
    }

    public void setUAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public Boolean getUSex() {
        return uSex;
    }

    public void setUSex(Boolean uSex) {
        this.uSex = uSex;
    }

    public Date getUCreateDate() {
        return uCreateDate;
    }

    public void setUCreateDate(Date uCreateDate) {
        this.uCreateDate = uCreateDate;
    }

    public String getUCreateByUser() {
        return uCreateByUser;
    }

    public void setUCreateByUser(String uCreateByUser) {
        this.uCreateByUser = uCreateByUser;
    }

    public Date getUUpdateDate() {
        return uUpdateDate;
    }

    public void setUUpdateDate(Date uUpdateDate) {
        this.uUpdateDate = uUpdateDate;
    }

    public String getUUpdateByUser() {
        return uUpdateByUser;
    }

    public void setUUpdateByUser(String uUpdateByUser) {
        this.uUpdateByUser = uUpdateByUser;
    }

    public String getUIco() {
        return uIco;
    }

    public void setUIco(String uIco) {
        this.uIco = uIco;
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

    public Integer getURId() {
        return uRId;
    }

    public void setURId(Integer uRId) {
        this.uRId = uRId;
    }

    public Integer getUCId() {
        return uCId;
    }

    public void setUCId(Integer uCId) {
        this.uCId = uCId;
    }

    public Integer getUParentId() {
        return uParentId;
    }

    public void setUParentId(Integer uParentId) {
        this.uParentId = uParentId;
    }

    public Integer getULimitUav() {
        return uLimitUav;
    }

    public void setULimitUav(Integer uLimitUav) {
        this.uLimitUav = uLimitUav;
    }

    public Integer getULimitHive() {
        return uLimitHive;
    }

    public void setULimitHive(Integer uLimitHive) {
        this.uLimitHive = uLimitHive;
    }

    public Date getULimitDate() {
        return uLimitDate;
    }

    public void setULimitDate(Date uLimitDate) {
        this.uLimitDate = uLimitDate;
    }

    public EfCompany getEfCompany() {
        return efCompany;
    }

    public void setEfCompany(EfCompany efCompany) {
        this.efCompany = efCompany;
    }

    public EfRole getEfRole() {
        return efRole;
    }

    public void setEfRole(EfRole efRole) {
        this.efRole = efRole;
    }
}
