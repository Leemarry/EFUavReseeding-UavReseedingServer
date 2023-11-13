package com.bear.reseeding.entity;

import java.io.Serializable;

/**
 * 用户表，用户属于部门或区域，可不关联表示未分配部门或区域(TUser)实体类
 *
 * @author makejava
 * @since 2023-11-10 15:51:16
 */
public class TUser implements Serializable {
    private static final long serialVersionUID = -54636169840711689L;
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 用户登录名
     */
    private String userLoginId;
    /**
     * 用户登录密码
     */
    private String userLoginPwd;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户电话
     */
    private String userPhone;
    /**
     * 外键：角色ID，表示系统中的某个角色
     */
    private Integer userRoleId;
    /**
     * 外键：系统ID
     */
    private Integer userSystemId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserLoginPwd() {
        return userLoginPwd;
    }

    public void setUserLoginPwd(String userLoginPwd) {
        this.userLoginPwd = userLoginPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getUserSystemId() {
        return userSystemId;
    }

    public void setUserSystemId(Integer userSystemId) {
        this.userSystemId = userSystemId;
    }

}
