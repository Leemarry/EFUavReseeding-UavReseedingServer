package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfHandle;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 处理记录表(EfHandle)表数据库访问层
 *
 * @author cwk
 * @since 2024-03-13 16:10:39
 */
public interface EfHandleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfHandle queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param
     * @param
     * @return 对象列表
     */
    List<EfHandle> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计总行数
     *
     * @param efHandle 查询条件
     * @return 总行数
     */
    long count(EfHandle efHandle);

    /**
     * 新增数据
     *
     * @param efHandle 实例对象
     * @return 影响行数
     */
    int insert(EfHandle efHandle);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfHandle> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfHandle> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfHandle> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<EfHandle> entities);

    /**
     * 修改数据
     *
     * @param efHandle 实例对象
     * @return 影响行数
     */
    int update(EfHandle efHandle);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据时间段查询数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 实例对象list
     */
    List<EfHandle> queryByTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

