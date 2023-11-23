package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUavAlternateHome;

import java.util.List;

/**
 * 无人机备降点表，一个无人机可设置多个备降点(EfUavAlternateHome)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:59:49
 */
public interface EfUavAlternateHomeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavAlternateHome queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavAlternateHome> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 实例对象
     */
    EfUavAlternateHome insert(EfUavAlternateHome efUavAlternateHome);

    /**
     * 修改数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 实例对象
     */
    EfUavAlternateHome update(EfUavAlternateHome efUavAlternateHome);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
