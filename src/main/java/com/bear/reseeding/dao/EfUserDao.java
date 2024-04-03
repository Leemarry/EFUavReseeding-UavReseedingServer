package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EfUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 19:00:09
 */
public interface EfUserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUser queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUser> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUser 实例对象
     * @return 对象列表
     */
    List<EfUser> queryAll(EfUser efUser);

    /**
     * 新增数据
     *
     * @param efUser 实例对象
     * @return 影响行数
     */
    int insert(EfUser efUser);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUser> entities);

    /**
     * 修改数据
     *
     * @param efUser 实例对象
     * @return 影响行数
     */
    int update(EfUser efUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


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

