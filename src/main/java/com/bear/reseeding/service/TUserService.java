package com.bear.reseeding.service;

import com.bear.reseeding.entity.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表，用户属于部门或区域，可不关联表示未分配部门或区域(TUser)表服务接口
 *
 * @author makejava
 * @since 2023-11-10 15:51:16
 */
public interface TUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TUser queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TUser> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser insert(TUser tUser);

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser update(TUser tUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    //region 新增

    /**
     * 登录
     *
     * @param userId 登录名
     * @param userPwd 密码
     * @return
     */
    TUser login(@Param("userId") String userId, @Param("userPwd") String userPwd);

    //endregion
}
