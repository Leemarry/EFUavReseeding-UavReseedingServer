package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfRelationUserUav;

import java.util.List;

/**
 * 无人机与用户为多对多关系(EfRelationUserUav)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:42
 */
public interface EfRelationUserUavService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationUserUav queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationUserUav> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efRelationUserUav 实例对象
     * @return 实例对象
     */
    EfRelationUserUav insert(EfRelationUserUav efRelationUserUav);

    /**
     * 修改数据
     *
     * @param efRelationUserUav 实例对象
     * @return 实例对象
     */
    EfRelationUserUav update(EfRelationUserUav efRelationUserUav);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 根据用户角色查询无人机信息
     * @param urId
     * @return
     */
    List<EfRelationUserUav> queryByUrid (Integer urId);

}
