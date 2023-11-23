package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfCompany;

import java.util.List;

/**
 * 公司信息，c_parent_id 用于区分子公司(EfCompany)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:27
 */
public interface EfCompanyService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCompany queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCompany> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efCompany 实例对象
     * @return 实例对象
     */
    EfCompany insert(EfCompany efCompany);

    /**
     * 修改数据
     *
     * @param efCompany 实例对象
     * @return 实例对象
     */
    EfCompany update(EfCompany efCompany);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
