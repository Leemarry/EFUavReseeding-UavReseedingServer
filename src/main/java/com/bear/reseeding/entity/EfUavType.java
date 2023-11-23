package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 无人机类型表(EfUavType)实体类
 *
 * @author makejava
 * @since 2023-11-23 19:00:00
 */
public class EfUavType implements Serializable {
    private static final long serialVersionUID = -13336887508467880L;
    /**
     * 唯一编号
     */
    private Integer id;
    /**
     * 无人机类型
     */
    private String typeName;
    /**
     * 无人机类型,型号
     */
    private String typeNo;
    /**
     * 无人机图片
     */
    private String typeImage;
    /**
     * 无人机控制协议，0大疆，1开源
     */
    private Integer typeProtocol;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }

    public Integer getTypeProtocol() {
        return typeProtocol;
    }

    public void setTypeProtocol(Integer typeProtocol) {
        this.typeProtocol = typeProtocol;
    }

}
