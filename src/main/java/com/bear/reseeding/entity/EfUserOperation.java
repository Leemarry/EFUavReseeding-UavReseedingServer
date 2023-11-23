package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户操作记录表，记录修改数据，删除数据等所有的操作。(EfUserOperation)实体类
 *
 * @author makejava
 * @since 2023-11-23 19:00:11
 */
public class EfUserOperation implements Serializable {
    private static final long serialVersionUID = 348177873374804844L;
    /**
     * 唯一编号
     */
    private Integer id;
    /**
     * 登录时的IP地址内网
     */
    private String ipLocal;
    /**
     * 登录时的IP地址外网
     */
    private String ipWww;
    /**
     * 操作记录
     */
    private String description;
    /**
     * 操作时间
     */
    private Date optdate;
    /**
     * 操作人员
     */
    private String userid;
    /**
     * 操作等级，0普通，1警告，2重要操作，3...
     */
    private Integer level;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpLocal() {
        return ipLocal;
    }

    public void setIpLocal(String ipLocal) {
        this.ipLocal = ipLocal;
    }

    public String getIpWww() {
        return ipWww;
    }

    public void setIpWww(String ipWww) {
        this.ipWww = ipWww;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
