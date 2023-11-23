package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 无人机原始唯一编号与系统唯一编号对应表，原始唯一编号很长，在系统中需要缩短(EfRelationUavsn)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:38
 */
public class EfRelationUavsn implements Serializable {
    private static final long serialVersionUID = -36048812833128992L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 无人机原始唯一编号
     */
    private String uavSn;
    /**
     * 无人机在系统中的编号
     */
    private String uavId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUavSn() {
        return uavSn;
    }

    public void setUavSn(String uavSn) {
        this.uavSn = uavSn;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

}
