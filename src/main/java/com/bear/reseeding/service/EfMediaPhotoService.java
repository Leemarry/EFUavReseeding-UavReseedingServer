package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfMediaPhoto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 实时拍摄照片表(EfMediaPhoto)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:57:33
 */
public interface EfMediaPhotoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfMediaPhoto queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfMediaPhoto> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    EfMediaPhoto insert(EfMediaPhoto efMediaPhoto);

    /**
     * 修改数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    EfMediaPhoto update(EfMediaPhoto efMediaPhoto);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过飞行架次
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    List<EfMediaPhoto> queryByeachsortieIdOruavId(Integer eachsortieId);


    /**
     * 根据时间和编号查找实时图片
     *
     * @param newFileName 图片tag
     * @param UavID       无人机编号
     * @return
     */
    EfMediaPhoto queryByCreatTime(@Param("UavID") String UavID, @Param("newFileName") String newFileName);

    /**
     * 根据时间查询无人机拍照最近的图片
     * @param uavId
     * @param lastTime
     * @return
     */
    EfMediaPhoto queryByUavIdAndLatestTime(@Param("uavId") String uavId, @Param("lastTime") Date lastTime);

}
