package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfRelationUavsn;

import java.util.List;

/**
 * 无人机原始唯一编号与系统唯一编号对应表，原始唯一编号很长，在系统中需要缩短(EfRelationUavsn)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:39
 */
public interface EfRelationUavsnService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationUavsn queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationUavsn> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efRelationUavsn 实例对象
     * @return 实例对象
     */
    EfRelationUavsn insert(EfRelationUavsn efRelationUavsn);

    /**
     * 修改数据
     *
     * @param efRelationUavsn 实例对象
     * @return 实例对象
     */
    EfRelationUavsn update(EfRelationUavsn efRelationUavsn);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
