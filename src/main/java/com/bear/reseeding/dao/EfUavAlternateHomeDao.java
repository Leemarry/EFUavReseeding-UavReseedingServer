package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUavAlternateHome;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机备降点表，一个无人机可设置多个备降点(EfUavAlternateHome)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:59:50
 */
public interface EfUavAlternateHomeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavAlternateHome queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavAlternateHome> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUavAlternateHome 实例对象
     * @return 对象列表
     */
    List<EfUavAlternateHome> queryAll(EfUavAlternateHome efUavAlternateHome);

    /**
     * 新增数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 影响行数
     */
    int insert(EfUavAlternateHome efUavAlternateHome);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavAlternateHome> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavAlternateHome> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavAlternateHome> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUavAlternateHome> entities);

    /**
     * 修改数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 影响行数
     */
    int update(EfUavAlternateHome efUavAlternateHome);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

