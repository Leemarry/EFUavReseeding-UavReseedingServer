package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUserOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户操作记录表，记录修改数据，删除数据等所有的操作。(EfUserOperation)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 19:00:11
 */
public interface EfUserOperationDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUserOperation queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUserOperation> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUserOperation 实例对象
     * @return 对象列表
     */
    List<EfUserOperation> queryAll(EfUserOperation efUserOperation);

    /**
     * 新增数据
     *
     * @param efUserOperation 实例对象
     * @return 影响行数
     */
    int insert(EfUserOperation efUserOperation);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities 实例对象列表;
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUserOperation> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUserOperation> entities);

    /**
     * 修改数据
     *
     * @param efUserOperation 实例对象
     * @return 影响行数
     */
    int update(EfUserOperation efUserOperation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

