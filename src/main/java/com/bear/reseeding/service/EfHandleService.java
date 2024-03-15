package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfHandle;

import java.util.Date;
import java.util.List;

/**
 * 处理记录表(EfHandle)表服务接口
 *
 * @author N.
 * @since 2024-03-13 16:10:44
 */
public interface EfHandleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfHandle queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfHandle> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efHandle 实例对象
     * @return 实例对象
     */
    EfHandle insert(EfHandle efHandle);

    /**
     * 修改数据
     *
     * @param efHandle 实例对象
     * @return 实例对象
     */
    EfHandle update(EfHandle efHandle);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 根据时间段查询数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 实例对象list
     */
    List<EfHandle> queryByTime(Date startDate, Date endDate);
}
