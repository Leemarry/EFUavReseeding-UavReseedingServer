package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfMediaPhoto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 实时拍摄照片表(EfMediaPhoto)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:57:35
 */
public interface EfMediaPhotoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfMediaPhoto queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfMediaPhoto> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efMediaPhoto 实例对象
     * @return 对象列表
     */
    List<EfMediaPhoto> queryAll(EfMediaPhoto efMediaPhoto);

    /**
     * 新增数据
     *
     * @param efMediaPhoto 实例对象
     * @return 影响行数
     */
    int insert(EfMediaPhoto efMediaPhoto);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfMediaPhoto> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfMediaPhoto> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfMediaPhoto> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfMediaPhoto> entities);

    /**
     * 修改数据
     *
     * @param efMediaPhoto 实例对象
     * @return 影响行数
     */
    int update(EfMediaPhoto efMediaPhoto);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     *  通过飞行架次
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    List<EfMediaPhoto> queryByeachsortieIdOruavId(@Param("eachsortieId") Integer eachsortieId);


    /**
     * 根据时间和编号查找图片
     *
     * @param newFileName 图片tag
     * @param UavID       无人机编号
     * @return
     */
    EfMediaPhoto queryByCreatTime(@Param("UavID") String UavID, @Param("newFileName") String newFileName);


    EfMediaPhoto queryByUavIdAndLatestTime(@Param(value = "uavId") String uavId, @Param(value = "lastTime") Date lastTime);


    EfMediaPhoto fuzzyQuery( @Param(value = "tag") String tag,@Param(value = "uavId") String uavId);

}

