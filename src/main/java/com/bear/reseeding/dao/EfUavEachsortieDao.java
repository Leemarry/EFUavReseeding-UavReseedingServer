package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUavEachsortie;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 无人机每飞行一个架次，产生一条记录。(EfUavEachsortie)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:59:59
 */
public interface EfUavEachsortieDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfUavEachsortie queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUavEachsortie> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUavEachsortie 实例对象
     * @return 对象列表
     */
    List<EfUavEachsortie> queryAll(EfUavEachsortie efUavEachsortie);

    /**
     * 新增数据
     *
     * @param efUavEachsortie 实例对象
     * @return 影响行数
     */
    int insert(EfUavEachsortie efUavEachsortie);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavEachsortie>实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUavEachsortie> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavEachsortie>实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUavEachsortie> entities);

    /**
     * 修改数据
     *
     * @param efUavEachsortie 实例对象
     * @return 影响行数
     */
    int update(EfUavEachsortie efUavEachsortie);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 查询架次列表
     *
     * @param uavId 无人机id
     * @param startTime 架次前时间
     * @param endTime  架次后时间
     * @return
     */
    List<EfUavEachsortie> queryByIdOrTime(@Param("uavId") String uavId,@Param("startTime") String startTime, @Param("endTime") String endTime);

       /**
     * 根据拍照时间查询架次
     *
     * @param date
     * @param uavId
     * @return
     */
    EfUavEachsortie queryByPhotoTime(@Param("date") Date date, @Param("uavId") String uavId);

}

