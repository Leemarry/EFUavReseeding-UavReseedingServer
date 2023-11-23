package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色表(EfRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:47
 */
public interface EfRoleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfRole queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efRole 实例对象
     * @return 对象列表
     */
    List<EfRole> queryAll(EfRole efRole);

    /**
     * 新增数据
     *
     * @param efRole 实例对象
     * @return 影响行数
     */
    int insert(EfRole efRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfRole> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfRole> entities);

    /**
     * 修改数据
     *
     * @param efRole 实例对象
     * @return 影响行数
     */
    int update(EfRole efRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

