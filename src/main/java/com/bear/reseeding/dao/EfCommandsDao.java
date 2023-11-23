package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfCommands;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开源无人机命令(EfCommands)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:08
 */
public interface EfCommandsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCommands queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCommands> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efCommands 实例对象
     * @return 对象列表
     */
    List<EfCommands> queryAll(EfCommands efCommands);

    /**
     * 新增数据
     *
     * @param efCommands 实例对象
     * @return 影响行数
     */
    int insert(EfCommands efCommands);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCommands> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfCommands> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCommands> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfCommands> entities);

    /**
     * 修改数据
     *
     * @param efCommands 实例对象
     * @return 影响行数
     */
    int update(EfCommands efCommands);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

