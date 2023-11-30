package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 公司与无人机绑定关系表，通常情况下，一个公司可包含多架无人机，但一架无人机只能属于一个公司，特殊情况下允许无人机属于多个公司(EfRelationCompanyUav)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:35
 */
public class EfRelationCompanyUav implements Serializable {
    private static final long serialVersionUID = -76304361569598361L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 公司编号
     */
    private Integer companyId;

    public EfUav getEfUav() {
        return efUav;
    }

    public void setEfUav(EfUav efUav) {
        this.efUav = efUav;
    }

    /**
     * 无人机唯一标识
     */
    private String uavId;
    /**
     * 如果无人机是租赁的，可以限制租赁日期
     */
    private Date limitDate;

    /**
     *公司关联无人机信息
     */
    private  EfUav efUav;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

}
