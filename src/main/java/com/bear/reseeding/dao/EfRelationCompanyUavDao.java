package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfRelationCompanyUav;
import com.bear.reseeding.entity.EfUav;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司与无人机绑定关系表，通常情况下，一个公司可包含多架无人机，但一架无人机只能属于一个公司，特殊情况下允许无人机属于多个公司(EfRelationCompanyUav)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:38
 */
public interface EfRelationCompanyUavDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationCompanyUav queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationCompanyUav> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efRelationCompanyUav 实例对象
     * @return 对象列表
     */
    List<EfRelationCompanyUav> queryAll(EfRelationCompanyUav efRelationCompanyUav);

    /**
     * 新增数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 影响行数
     */
    int insert(EfRelationCompanyUav efRelationCompanyUav);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationCompanyUav> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfRelationCompanyUav> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationCompanyUav> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfRelationCompanyUav> entities);

    /**
     * 修改数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 影响行数
     */
    int update(EfRelationCompanyUav efRelationCompanyUav);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 查询所有
     *
     * @return
     */
    List<EfRelationCompanyUav> queryAll2();

    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId
     * @param urId
     * @return
     */
    public  List<EfRelationCompanyUav> queryAllUavByCIdOrUrId(@Param("cId") Integer cId, @Param("urId") Integer urId);
}

