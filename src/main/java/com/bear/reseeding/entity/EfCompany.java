package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 公司信息，c_parent_id 用于区分子公司(EfCompany)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:09
 */
public class EfCompany implements Serializable {
    private static final long serialVersionUID = 367795797443240093L;
    /**
     * 公司编号
     */
    private Integer id;
    /**
     * 公司名称
     */
    private String cName;
    /**
     * 系统中文名称
     */
    private String cNameZh;
    /**
     * 公司网页地址
     */
    private String cHomePage;
    /**
     * 公司logo
     */
    private String cLogoIco;
    /**
     * 公司地址
     */
    private String cAddress;
    /**
     * 联系手机
     */
    private String cPhone;
    /**
     * 联系电话
     */
    private String cTelephone;
    /**
     * 创建日期
     */
    private Date cCreateDate;
    /**
     * 外键：对应的系统ID，主要是系统标题等信息
     */
    private Integer cSystemId;
    /**
     * 父公司，默认0无父公司
     */
    private Integer cParentId;
    /**
     * 在允许登录的情况下，是否可操作无人机，默认0允许，-1不允许
     */
    private Integer cLimitUav;
    /**
     * 在允许登录的情况下，是否可操作停机坪，默认0允许，-1不允许
     */
    private Integer cLimitHive;
    /**
     * 限制日期，结合下属用户的限制日期，该限制主要限制用户登录
     */
    private Date cLimitDate;
    /**
     * 值为 1&2&3。 其它功能(1: 秸秆功能 ， 2: 光伏)
     */
    private String cFunction;
    /**
     * 关联系统信息
     */
    private EfSysteminfo efSysteminfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCNameZh() {
        return cNameZh;
    }

    public void setCNameZh(String cNameZh) {
        this.cNameZh = cNameZh;
    }

    public String getCHomePage() {
        return cHomePage;
    }

    public void setCHomePage(String cHomePage) {
        this.cHomePage = cHomePage;
    }

    public String getCLogoIco() {
        return cLogoIco;
    }

    public void setCLogoIco(String cLogoIco) {
        this.cLogoIco = cLogoIco;
    }

    public String getCAddress() {
        return cAddress;
    }

    public void setCAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getCPhone() {
        return cPhone;
    }

    public void setCPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getCTelephone() {
        return cTelephone;
    }

    public void setCTelephone(String cTelephone) {
        this.cTelephone = cTelephone;
    }

    public Date getCCreateDate() {
        return cCreateDate;
    }

    public void setCCreateDate(Date cCreateDate) {
        this.cCreateDate = cCreateDate;
    }

    public Integer getCSystemId() {
        return cSystemId;
    }

    public void setCSystemId(Integer cSystemId) {
        this.cSystemId = cSystemId;
    }

    public Integer getCParentId() {
        return cParentId;
    }

    public void setCParentId(Integer cParentId) {
        this.cParentId = cParentId;
    }

    public Integer getCLimitUav() {
        return cLimitUav;
    }

    public void setCLimitUav(Integer cLimitUav) {
        this.cLimitUav = cLimitUav;
    }

    public Integer getCLimitHive() {
        return cLimitHive;
    }

    public void setCLimitHive(Integer cLimitHive) {
        this.cLimitHive = cLimitHive;
    }

    public Date getCLimitDate() {
        return cLimitDate;
    }

    public void setCLimitDate(Date cLimitDate) {
        this.cLimitDate = cLimitDate;
    }

    public String getCFunction() {
        return cFunction;
    }

    public void setCFunction(String cFunction) {
        this.cFunction = cFunction;
    }

    public EfSysteminfo getEfSysteminfo() {
        return efSysteminfo;
    }

    public void setEfSysteminfo(EfSysteminfo efSysteminfo) {
        this.efSysteminfo = efSysteminfo;
    }
}
