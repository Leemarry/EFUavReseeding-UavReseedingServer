package com.bear.reseeding.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 航点任务表(EfTaskKmz)实体类
 *
 * @author makejava
 * @since 2022-11-18 09:29:45
 */
public class EfTaskKmz implements Serializable {

    private static final long serialVersionUID = 584139400503767053L;
    /**
     * 主键自增，任务编号
     */
    private Integer id;
    /**
     * 任务名称
     */
    private String kmzName;
    /**
     * 航线文件保存位置
     */
    private String kmzPath;
    /**
     * 后缀名，kmz/kml...
     */
    private String kmzType;
    /**
     * dji v1.0.0
     */
    private String kmzVersion;
    /**
     * 文件大小，byte
     */
    private long kmzSize;
    /**
     * 任务航程，米
     */
    private Double kmzDistance;
    /**
     * 预计用时，秒
     */
    private Double kmzDuration;
    /**
     * 任务创建时间
     */
    private Date kmzCreateTime;

    /**
     * 创建人名称
     */
    private String kmzCreateUser;
    /**
     * 外键：任务创建人
     */
    private Integer kmzCreateByUserId;
    /**
     * 任务最新修改时间
     */
    private Date kmzUpdateTime;

    public String getKmzCreateUser() {
        return kmzCreateUser;
    }

    public void setKmzCreateUser(String kmzCreateUser) {
        this.kmzCreateUser = kmzCreateUser;
    }

    public String getKmzUpdateUser() {
        return kmzUpdateUser;
    }

    public void setKmzUpdateUser(String kmzUpdateUser) {
        this.kmzUpdateUser = kmzUpdateUser;
    }

    /**
     * 修改人名称
     */
    private String kmzUpdateUser;

    /**
     * 外键：任务最新修改人
     */
    private Integer kmzUpdateByUserId;
    /**
     * 外键：任务所属公司对象
     */
    private Integer kmzCompanyId;
    /**
     * 任务描述与说明
     */
    private String kmzDes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKmzName() {
        return kmzName;
    }

    public void setKmzName(String kmzName) {
        this.kmzName = kmzName;
    }

    public String getKmzPath() {
        return kmzPath;
    }

    public void setKmzPath(String kmzPath) {
        this.kmzPath = kmzPath;
    }

    public String getKmzType() {
        return kmzType;
    }

    public void setKmzType(String kmzType) {
        this.kmzType = kmzType;
    }

    public String getKmzVersion() {
        return kmzVersion;
    }

    public void setKmzVersion(String kmzVersion) {
        this.kmzVersion = kmzVersion;
    }

    public long getKmzSize() {
        return kmzSize;
    }

    public void setKmzSize(long kmzSize) {
        this.kmzSize = kmzSize;
    }

    public Double getKmzDistance() {
        return kmzDistance;
    }

    public void setKmzDistance(Double kmzDistance) {
        this.kmzDistance = kmzDistance;
    }

    public Double getKmzDuration() {
        return kmzDuration;
    }

    public void setKmzDuration(Double kmzDuration) {
        this.kmzDuration = kmzDuration;
    }

    public Date getKmzCreateTime() {
        return kmzCreateTime;
    }

    public void setKmzCreateTime(Date kmzCreateTime) {
        this.kmzCreateTime = kmzCreateTime;
    }

    public Integer getKmzCreateByUserId() {
        return kmzCreateByUserId;
    }

    public void setKmzCreateByUserId(Integer kmzCreateByUserId) {
        this.kmzCreateByUserId = kmzCreateByUserId;
    }

    public Date getKmzUpdateTime() {
        return kmzUpdateTime;
    }

    public void setKmzUpdateTime(Date kmzUpdateTime) {
        this.kmzUpdateTime = kmzUpdateTime;
    }

    public Integer getKmzUpdateByUserId() {
        return kmzUpdateByUserId;
    }

    public void setKmzUpdateByUserId(Integer kmzUpdateByUserId) {
        this.kmzUpdateByUserId = kmzUpdateByUserId;
    }

    public Integer getKmzCompanyId() {
        return kmzCompanyId;
    }

    public void setKmzCompanyId(Integer kmzCompanyId) {
        this.kmzCompanyId = kmzCompanyId;
    }

    public String getKmzDes() {
        return kmzDes;
    }

    public void setKmzDes(String kmzDes) {
        this.kmzDes = kmzDes;
    }


}
