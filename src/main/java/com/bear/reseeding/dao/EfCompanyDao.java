package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfCompany;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司信息，c_parent_id 用于区分子公司(EfCompany)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:31
 */
public interface EfCompanyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCompany queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCompany> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efCompany 实例对象
     * @return 对象列表
     */
    List<EfCompany> queryAll(EfCompany efCompany);

    /**
     * 新增数据
     *
     * @param efCompany 实例对象
     * @return 影响行数
     */
    int insert(EfCompany efCompany);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCompany> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfCompany> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCompany> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfCompany> entities);

    /**
     * 修改数据
     *
     * @param efCompany 实例对象
     * @return 影响行数
     */
    int update(EfCompany efCompany);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

