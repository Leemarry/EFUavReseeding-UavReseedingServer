package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfRelationUserUav;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机与用户为多对多关系(EfRelationUserUav)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:44
 */
public interface EfRelationUserUavDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRelationUserUav queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRelationUserUav> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efRelationUserUav 实例对象
     * @return 对象列表
     */
    List<EfRelationUserUav> queryAll(EfRelationUserUav efRelationUserUav);

    /**
     * 新增数据
     *
     * @param efRelationUserUav 实例对象
     * @return 影响行数
     */
    int insert(EfRelationUserUav efRelationUserUav);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationUserUav> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfRelationUserUav> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRelationUserUav> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfRelationUserUav> entities);

    /**
     * 修改数据
     *
     * @param efRelationUserUav 实例对象
     * @return 影响行数
     */
    int update(EfRelationUserUav efRelationUserUav);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据用户角色查询无人机信息
     * @param urId
     * @return
     */
    List<EfRelationUserUav> queryByUrid (@Param("urId") Integer urId);

}

