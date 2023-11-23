package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 公司信息表，每个登录用户都可能有公司，或没有公司(EfSysteminfo)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:47
 */
public class EfSysteminfo implements Serializable {
    private static final long serialVersionUID = 437600087417279603L;
    /**
     * 系统编号
     */
    private Integer id;
    /**
     * 系统名称
     */
    private String sysName;
    /**
     * 系统中文名称
     */
    private String sysNameZh;
    /**
     * 系统默认语言。zh/en/...
     */
    private String sysLanguage;
    /**
     * 系统使用的图标路径
     */
    private String sysIco;
    /**
     * 系统描述信息
     */
    private String sysInfo;
    /**
     * api 地址
     */
    private String sysServerHost;
    /**
     * api 端口
     */
    private Integer sysServerPort;
    /**
     * mqtt地址
     */
    private String sysMqttServer;
    /**
     * mqtt端口
     */
    private Integer sysMqttPort;
    /**
     * mqtt账号
     */
    private String sysMqttUserName;
    /**
     * mqtt密码
     */
    private String sysMqttUserPwd;
    /**
     * 视频推流地址
     */
    private String sysVideoPath;
    /**
     * 视频-用户名
     */
    private String sysVideoUserName;
    /**
     * 视频-密码
     */
    private String sysVideoUserPwd;
    /**
     * 无人机照片储存位置
     */
    private String sysUavPhotoStorage;
    /**
     * 无人机视频储存位置
     */
    private String sysUavVideoStorage;
    /**
     * 接口是否加密，默认1加密，0不加密
     */
    private Boolean sysApiEncryption;
    /**
     * 系统创建日期
     */
    private Date sysCreateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysNameZh() {
        return sysNameZh;
    }

    public void setSysNameZh(String sysNameZh) {
        this.sysNameZh = sysNameZh;
    }

    public String getSysLanguage() {
        return sysLanguage;
    }

    public void setSysLanguage(String sysLanguage) {
        this.sysLanguage = sysLanguage;
    }

    public String getSysIco() {
        return sysIco;
    }

    public void setSysIco(String sysIco) {
        this.sysIco = sysIco;
    }

    public String getSysInfo() {
        return sysInfo;
    }

    public void setSysInfo(String sysInfo) {
        this.sysInfo = sysInfo;
    }

    public String getSysServerHost() {
        return sysServerHost;
    }

    public void setSysServerHost(String sysServerHost) {
        this.sysServerHost = sysServerHost;
    }

    public Integer getSysServerPort() {
        return sysServerPort;
    }

    public void setSysServerPort(Integer sysServerPort) {
        this.sysServerPort = sysServerPort;
    }

    public String getSysMqttServer() {
        return sysMqttServer;
    }

    public void setSysMqttServer(String sysMqttServer) {
        this.sysMqttServer = sysMqttServer;
    }

    public Integer getSysMqttPort() {
        return sysMqttPort;
    }

    public void setSysMqttPort(Integer sysMqttPort) {
        this.sysMqttPort = sysMqttPort;
    }

    public String getSysMqttUserName() {
        return sysMqttUserName;
    }

    public void setSysMqttUserName(String sysMqttUserName) {
        this.sysMqttUserName = sysMqttUserName;
    }

    public String getSysMqttUserPwd() {
        return sysMqttUserPwd;
    }

    public void setSysMqttUserPwd(String sysMqttUserPwd) {
        this.sysMqttUserPwd = sysMqttUserPwd;
    }

    public String getSysVideoPath() {
        return sysVideoPath;
    }

    public void setSysVideoPath(String sysVideoPath) {
        this.sysVideoPath = sysVideoPath;
    }

    public String getSysVideoUserName() {
        return sysVideoUserName;
    }

    public void setSysVideoUserName(String sysVideoUserName) {
        this.sysVideoUserName = sysVideoUserName;
    }

    public String getSysVideoUserPwd() {
        return sysVideoUserPwd;
    }

    public void setSysVideoUserPwd(String sysVideoUserPwd) {
        this.sysVideoUserPwd = sysVideoUserPwd;
    }

    public String getSysUavPhotoStorage() {
        return sysUavPhotoStorage;
    }

    public void setSysUavPhotoStorage(String sysUavPhotoStorage) {
        this.sysUavPhotoStorage = sysUavPhotoStorage;
    }

    public String getSysUavVideoStorage() {
        return sysUavVideoStorage;
    }

    public void setSysUavVideoStorage(String sysUavVideoStorage) {
        this.sysUavVideoStorage = sysUavVideoStorage;
    }

    public Boolean getSysApiEncryption() {
        return sysApiEncryption;
    }

    public void setSysApiEncryption(Boolean sysApiEncryption) {
        this.sysApiEncryption = sysApiEncryption;
    }

    public Date getSysCreateDate() {
        return sysCreateDate;
    }

    public void setSysCreateDate(Date sysCreateDate) {
        this.sysCreateDate = sysCreateDate;
    }

}
