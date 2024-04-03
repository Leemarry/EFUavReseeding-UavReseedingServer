package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUavRealtimedata;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 19:00:00
 */
public interface EfUavRealtimedataDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavRealtimedata queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavRealtimedata> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUavRealtimedata 实例对象
     * @return 对象列表
     */
    List<EfUavRealtimedata> queryAll(EfUavRealtimedata efUavRealtimedata);

    /**
     * 新增数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 影响行数
     */
    int insert(EfUavRealtimedata efUavRealtimedata);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavRealtimedata> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavRealtimedata> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavRealtimedata>实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUavRealtimedata> entities);

    /**
     * 修改数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 影响行数
     */
    int update(EfUavRealtimedata efUavRealtimedata);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

