package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUavEachsortie;

import java.util.List;

/**
 * 无人机每飞行一个架次，产生一条记录。(EfUavEachsortie)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:59:58
 */
public interface EfUavEachsortieService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavEachsortie queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavEachsortie> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUavEachsortie 实例对象
     * @return 实例对象
     */
    EfUavEachsortie insert(EfUavEachsortie efUavEachsortie);

    /**
     * 修改数据
     *
     * @param efUavEachsortie 实例对象
     * @return 实例对象
     */
    EfUavEachsortie update(EfUavEachsortie efUavEachsortie);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}