package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfSysteminfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司信息表，每个登录用户都可能有公司，或没有公司(EfSysteminfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-28 14:21:35
 */
public interface EfSysteminfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfSysteminfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfSysteminfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efSysteminfo 实例对象
     * @return 对象列表
     */
    List<EfSysteminfo> queryAll(EfSysteminfo efSysteminfo);

    /**
     * 新增数据
     *
     * @param efSysteminfo 实例对象
     * @return 影响行数
     */
    int insert(EfSysteminfo efSysteminfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfSysteminfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfSysteminfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfSysteminfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfSysteminfo> entities);

    /**
     * 修改数据
     *
     * @param efSysteminfo 实例对象
     * @return 影响行数
     */
    int update(EfSysteminfo efSysteminfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

