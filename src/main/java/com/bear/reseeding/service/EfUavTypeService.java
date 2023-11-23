package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUavType;

import java.util.List;

/**
 * 无人机类型表(EfUavType)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 19:00:00
 */
public interface EfUavTypeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavType queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavType> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUavType 实例对象
     * @return 实例对象
     */
    EfUavType insert(EfUavType efUavType);

    /**
     * 修改数据
     *
     * @param efUavType 实例对象
     * @return 实例对象
     */
    EfUavType update(EfUavType efUavType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
