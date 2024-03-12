package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUavType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机类型表(EfUavType)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 19:00:00
 */
public interface EfUavTypeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavType queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavType> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUavType 实例对象
     * @return 对象列表
     */
    List<EfUavType> queryAll(EfUavType efUavType);

    /**
     * 新增数据
     *
     * @param efUavType 实例对象
     * @return 影响行数
     */
    int insert(EfUavType efUavType);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavType> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavType> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavType>实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUavType> entities);

    /**
     * 修改数据
     *
     * @param efUavType 实例对象
     * @return 影响行数
     */
    int update(EfUavType efUavType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

