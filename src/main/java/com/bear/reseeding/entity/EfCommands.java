package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 开源无人机命令(EfCommands)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:56:13
 */
public class EfCommands implements Serializable {
    private static final long serialVersionUID = -31380564560186361L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 命令值
     */
    private Integer cmdId;
    /**
     * 命令英文名称
     */
    private String cmdNameEn;
    /**
     * 命令中文名称
     */
    private String cmdNameZh;
    /**
     * 命令描述
     */
    private String cmdDes;
    /**
     * 参数1描述
     */
    private String cmdP1Des;
    /**
     * 参数2描述
     */
    private String cmdP2Des;
    /**
     * 参数3描述
     */
    private String cmdP3Des;
    /**
     * 参数4描述
     */
    private String cmdP4Des;
    /**
     * 参数x描述
     */
    private String cmdXDes;
    /**
     * 参数y描述
     */
    private String cmdYDes;
    /**
     * 参数z描述
     */
    private String cmdZDes;
    /**
     * 是否使用该命令，默认0使用，-1不使用
     */
    private Integer cmdStatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCmdId() {
        return cmdId;
    }

    public void setCmdId(Integer cmdId) {
        this.cmdId = cmdId;
    }

    public String getCmdNameEn() {
        return cmdNameEn;
    }

    public void setCmdNameEn(String cmdNameEn) {
        this.cmdNameEn = cmdNameEn;
    }

    public String getCmdNameZh() {
        return cmdNameZh;
    }

    public void setCmdNameZh(String cmdNameZh) {
        this.cmdNameZh = cmdNameZh;
    }

    public String getCmdDes() {
        return cmdDes;
    }

    public void setCmdDes(String cmdDes) {
        this.cmdDes = cmdDes;
    }

    public String getCmdP1Des() {
        return cmdP1Des;
    }

    public void setCmdP1Des(String cmdP1Des) {
        this.cmdP1Des = cmdP1Des;
    }

    public String getCmdP2Des() {
        return cmdP2Des;
    }

    public void setCmdP2Des(String cmdP2Des) {
        this.cmdP2Des = cmdP2Des;
    }

    public String getCmdP3Des() {
        return cmdP3Des;
    }

    public void setCmdP3Des(String cmdP3Des) {
        this.cmdP3Des = cmdP3Des;
    }

    public String getCmdP4Des() {
        return cmdP4Des;
    }

    public void setCmdP4Des(String cmdP4Des) {
        this.cmdP4Des = cmdP4Des;
    }

    public String getCmdXDes() {
        return cmdXDes;
    }

    public void setCmdXDes(String cmdXDes) {
        this.cmdXDes = cmdXDes;
    }

    public String getCmdYDes() {
        return cmdYDes;
    }

    public void setCmdYDes(String cmdYDes) {
        this.cmdYDes = cmdYDes;
    }

    public String getCmdZDes() {
        return cmdZDes;
    }

    public void setCmdZDes(String cmdZDes) {
        this.cmdZDes = cmdZDes;
    }

    public Integer getCmdStatus() {
        return cmdStatus;
    }

    public void setCmdStatus(Integer cmdStatus) {
        this.cmdStatus = cmdStatus;
    }

}
