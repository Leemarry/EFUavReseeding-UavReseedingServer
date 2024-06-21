package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfTaskKmz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 航点任务表(EfTaskKmz)表服务接口
 *
 * @author makejava
 * @since 2022-11-18 09:29:45
 */
public interface EfTaskKmzService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfTaskKmz queryById(Integer id);
//
//    /**
//     * 查询多条数据
//     *
//     * @param offset 查询起始位置
//     * @param limit  查询条数
//     * @return 对象列表
//     */
//    List<EfTaskKmz> queryAllByLimit(int offset, int limit);
//
//    /**
//     * 根据公司编号查询
//     *
//     * @param cid 公司编号
//     * @return 对象列表
//     */
//    List<EfTaskKmz> queryAllByCid(int cid);
//
    /**
     * 新增数据
     *
     * @param efTaskKmz 实例对象
     * @return 实例对象
     */
    EfTaskKmz insert(EfTaskKmz efTaskKmz);


    /**
     * 查询航线任务通过公司id与时间段
     *
     * @param UcId
     * @param startTime
     * @param endTime
     * @return
     */
    List<EfTaskKmz> queryByUcidAndTime(Integer UcId, @Param("startTime") String startTime, @Param("endTime") String endTime);
//
//
    /**
     * 检测航线任务是否已经存在
     *
     * @param fileName 航线任务名称
     * @param cid      公司ID
     * @return boolean
     */
    boolean checkKmzExist(String fileName, int cid);


    List<String> includeSamename(String name,Integer cid);

    List<Integer> includeSamenames(String name,Integer cid,Integer id,String startTime,String endTime);

    List<Integer> includeSame(String name,Integer cid,String startTime,String endTime);

//
//    /**
//     * 修改数据
//     *
//     * @param efTaskKmz 实例对象
//     * @return 实例对象
//     */
//    EfTaskKmz update(EfTaskKmz efTaskKmz);
//
    /**
     * 重命名
     */
    EfTaskKmz updateName(int id, String name, int userId);


    /**
     * 重置航线点0
     */
    int updateCurrentWpNo(int id, int CurrentWpNo, int userId);
//
//    /**
//     * 重命名
//     */
//    EfTaskKmz updateNamePath(int id, String name, int userId, String path);
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
    boolean deleteById(Integer id);

}
