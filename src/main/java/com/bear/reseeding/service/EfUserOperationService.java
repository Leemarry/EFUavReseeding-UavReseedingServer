package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUserOperation;

import java.util.List;

/**
 * 用户操作记录表，记录修改数据，删除数据等所有的操作。(EfUserOperation)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 19:00:11
 */
public interface EfUserOperationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUserOperation queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUserOperation> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUserOperation 实例对象
     * @return 实例对象
     */
    EfUserOperation insert(EfUserOperation efUserOperation);

    /**
     * 修改数据
     *
     * @param efUserOperation 实例对象
     * @return 实例对象
     */
    EfUserOperation update(EfUserOperation efUserOperation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
