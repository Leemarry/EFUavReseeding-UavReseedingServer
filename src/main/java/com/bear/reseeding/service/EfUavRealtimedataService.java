package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUavRealtimedata;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:59:59
 */
public interface EfUavRealtimedataService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavRealtimedata queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavRealtimedata> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 实例对象
     */
    EfUavRealtimedata insert(EfUavRealtimedata efUavRealtimedata);

    /**
     * 修改数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 实例对象
     */
    EfUavRealtimedata update(EfUavRealtimedata efUavRealtimedata);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavRealtimedata> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavRealtimedata> entities);
}
