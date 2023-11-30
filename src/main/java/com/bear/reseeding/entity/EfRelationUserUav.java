package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机与用户为多对多关系(EfRelationUserUav)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:41
 */
public class EfRelationUserUav implements Serializable {
    private static final long serialVersionUID = 500398500101493976L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 无人机在系统中的唯一标识
     */
    private String uavId;
    /**
     * 公司可以给某个用户限制使用日期
     */
    private Date limitDate;
    /**
     * 当前用户对当前无人机的权限，0为正常权限
     */
    private Integer operAuth;

    /**
     *公司关联无人机信息
     */
    private  EfUav efUav;

    public EfUav getEfUav() {
        return efUav;
    }

    public void setEfUav(EfUav efUav) {
        this.efUav = efUav;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public Integer getOperAuth() {
        return operAuth;
    }

    public void setOperAuth(Integer operAuth) {
        this.operAuth = operAuth;
    }

}
