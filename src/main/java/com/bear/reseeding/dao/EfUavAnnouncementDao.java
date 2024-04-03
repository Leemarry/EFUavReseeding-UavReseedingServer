package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUavAnnouncement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机管制公告发布(EfUavAnnouncement)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:59:51
 */
public interface EfUavAnnouncementDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavAnnouncement queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavAnnouncement> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUavAnnouncement 实例对象
     * @return 对象列表
     */
    List<EfUavAnnouncement> queryAll(EfUavAnnouncement efUavAnnouncement);

    /**
     * 新增数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 影响行数
     */
    int insert(EfUavAnnouncement efUavAnnouncement);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavAnnouncement>实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavAnnouncement> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavAnnouncement>实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUavAnnouncement> entities);

    /**
     * 修改数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 影响行数
     */
    int update(EfUavAnnouncement efUavAnnouncement);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

