package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfRelationUavsn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机原始唯一编号与系统唯一编号对应表，原始唯一编号很长，在系统中需要缩短(EfRelationUavsn)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:41
 */
public interface EfRelationUavsnDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationUavsn queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationUavsn> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efRelationUavsn 实例对象
     * @return 对象列表
     */
    List<EfRelationUavsn> queryAll(EfRelationUavsn efRelationUavsn);

    /**
     * 新增数据
     *
     * @param efRelationUavsn 实例对象
     * @return 影响行数
     */
    int insert(EfRelationUavsn efRelationUavsn);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationUavsn> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfRelationUavsn> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationUavsn> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfRelationUavsn> entities);

    /**
     * 修改数据
     *
     * @param efRelationUavsn 实例对象
     * @return 影响行数
     */
    int update(EfRelationUavsn efRelationUavsn);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

