package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机管制公告发布(EfUavAnnouncement)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:50
 */
public class EfUavAnnouncement implements Serializable {
    private static final long serialVersionUID = -76949386548672972L;

    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发布日期
     */
    private Date newsTime;
    /**
     * 内容更新日期
     */
    private Date updateTime;
    /**
     * 公告类别
     */
    private String type;
    /**
     * 公告区域
     */
    private String region;
    /**
     * 公告来源
     */
    private String source;
    /**
     * 公告发布单位
     */
    private String company;
    /**
     * 生效日期
     */
    private Date startTime;
    /**
     * 截止日期
     */
    private Date stopTime;
    /**
     * 对应系统的公司，为0则所有公司都可见
     */
    private Integer companyId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Date newsTime) {
        this.newsTime = newsTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
