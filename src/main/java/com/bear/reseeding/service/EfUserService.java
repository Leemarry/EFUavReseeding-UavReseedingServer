package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EfUser)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 19:00:08
 */
public interface EfUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUser queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUser> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUser 实例对象
     * @return 实例对象
     */
    EfUser insert(EfUser efUser);

    /**
     * 修改数据
     *
     * @param efUser 实例对象
     * @return 实例对象
     */
    EfUser update(EfUser efUser);

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
     * @param userId  登录名
     * @param userPwd 密码
     * @return
     */
    EfUser login(@Param("userId") String userId, @Param("userPwd") String userPwd);


    /**
     * 查询所有管理员账户，role=1,2,3
     *
     * @return 对象列表
     */
    List<EfUser> queryAllAdmin();

    //endregion
}
