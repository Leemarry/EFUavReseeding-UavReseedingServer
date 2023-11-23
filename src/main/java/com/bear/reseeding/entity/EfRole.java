package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户角色表(EfRole)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:44
 */
public class EfRole implements Serializable {
    private static final long serialVersionUID = 818676190253172451L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 自定义角色编号
     */
    private Integer roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述信息
     */
    private String roleDescription;
    /**
     * 角色创建日期
     */
    private Date roleCreateDate;
    /**
     * 谁去创建的角色
     */
    private String roleCreateByUser;
    /**
     * 角色修改日期
     */
    private Date roleUpdateDate;
    /**
     * 谁去修改的角色
     */
    private String roleUpdateByUser;
    /**
     * 描述
     */
    private String roleDes;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Date getRoleCreateDate() {
        return roleCreateDate;
    }

    public void setRoleCreateDate(Date roleCreateDate) {
        this.roleCreateDate = roleCreateDate;
    }

    public String getRoleCreateByUser() {
        return roleCreateByUser;
    }

    public void setRoleCreateByUser(String roleCreateByUser) {
        this.roleCreateByUser = roleCreateByUser;
    }

    public Date getRoleUpdateDate() {
        return roleUpdateDate;
    }

    public void setRoleUpdateDate(Date roleUpdateDate) {
        this.roleUpdateDate = roleUpdateDate;
    }

    public String getRoleUpdateByUser() {
        return roleUpdateByUser;
    }

    public void setRoleUpdateByUser(String roleUpdateByUser) {
        this.roleUpdateByUser = roleUpdateByUser;
    }

    public String getRoleDes() {
        return roleDes;
    }

    public void setRoleDes(String roleDes) {
        this.roleDes = roleDes;
    }

}
