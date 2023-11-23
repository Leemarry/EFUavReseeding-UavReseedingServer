package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUserLogin;

import java.util.List;

/**
 * 用户登录记录信息表
 * (EfUserLogin)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 19:00:10
 */
public interface EfUserLoginService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUserLogin queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUserLogin> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUserLogin 实例对象
     * @return 实例对象
     */
    EfUserLogin insert(EfUserLogin efUserLogin);

    /**
     * 修改数据
     *
     * @param efUserLogin 实例对象
     * @return 实例对象
     */
    EfUserLogin update(EfUserLogin efUserLogin);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
