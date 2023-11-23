package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUavAnnouncement;

import java.util.List;

/**
 * 无人机管制公告发布(EfUavAnnouncement)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:59:50
 */
public interface EfUavAnnouncementService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavAnnouncement queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavAnnouncement> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 实例对象
     */
    EfUavAnnouncement insert(EfUavAnnouncement efUavAnnouncement);

    /**
     * 修改数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 实例对象
     */
    EfUavAnnouncement update(EfUavAnnouncement efUavAnnouncement);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
