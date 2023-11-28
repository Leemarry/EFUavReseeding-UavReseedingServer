package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfCavity;

import java.util.List;

/**
 * 草原空洞表，与照片和飞行架次关联(EfCavity)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:51:09
 */
public interface EfCavityService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCavity queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCavity> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efCavity 实例对象
     * @return 实例对象
     */
    EfCavity insert(EfCavity efCavity);

    /**
     * 修改数据
     *
     * @param efCavity 实例对象
     * @return 实例对象
     */
    EfCavity update(EfCavity efCavity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过飞行架次 查询 洞斑信息 queryByeachsortieIdOruavId
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    List<EfCavity> queryByeachsortieIdOruavId(Integer eachsortieId);

}
