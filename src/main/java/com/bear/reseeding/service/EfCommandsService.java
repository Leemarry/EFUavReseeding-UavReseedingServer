package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfCommands;

import java.util.List;

/**
 * 开源无人机命令(EfCommands)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:56:15
 */
public interface EfCommandsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCommands queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCommands> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efCommands 实例对象
     * @return 实例对象
     */
    EfCommands insert(EfCommands efCommands);

    /**
     * 修改数据
     *
     * @param efCommands 实例对象
     * @return 实例对象
     */
    EfCommands update(EfCommands efCommands);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
