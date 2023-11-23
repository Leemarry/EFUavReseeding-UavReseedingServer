package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfSysteminfo;

import java.util.List;

/**
 * 公司信息表，每个登录用户都可能有公司，或没有公司(EfSysteminfo)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:47
 */
public interface EfSysteminfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfSysteminfo queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfSysteminfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efSysteminfo 实例对象
     * @return 实例对象
     */
    EfSysteminfo insert(EfSysteminfo efSysteminfo);

    /**
     * 修改数据
     *
     * @param efSysteminfo 实例对象
     * @return 实例对象
     */
    EfSysteminfo update(EfSysteminfo efSysteminfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
