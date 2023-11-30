package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfRelationCompanyUav;
import com.bear.reseeding.entity.EfUav;

import java.util.List;

/**
 * 公司与无人机绑定关系表，通常情况下，一个公司可包含多架无人机，但一架无人机只能属于一个公司，特殊情况下允许无人机属于多个公司(EfRelationCompanyUav)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:36
 */
public interface EfRelationCompanyUavService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationCompanyUav queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationCompanyUav> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 实例对象
     */
    EfRelationCompanyUav insert(EfRelationCompanyUav efRelationCompanyUav);

    /**
     * 修改数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 实例对象
     */
    EfRelationCompanyUav update(EfRelationCompanyUav efRelationCompanyUav);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

        /**
     * 查询所有
     *
     * @return
     */
    List<EfRelationCompanyUav> queryAll2();


    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId 属公司id
     * @param urId 用户角色id
     * @return
     */
    List<EfRelationCompanyUav> queryAllUavByCIdOrUrId(Integer cId, Integer urId);
}
