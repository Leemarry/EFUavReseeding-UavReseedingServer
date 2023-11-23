package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfRole;

import java.util.List;

/**
 * 用户角色表(EfRole)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:45
 */
public interface EfRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRole queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efRole 实例对象
     * @return 实例对象
     */
    EfRole insert(EfRole efRole);

    /**
     * 修改数据
     *
     * @param efRole 实例对象
     * @return 实例对象
     */
    EfRole update(EfRole efRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
