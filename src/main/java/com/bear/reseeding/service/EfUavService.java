package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfUav;

import java.util.List;

/**
 * 无人机信息表(EfUav)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:59:48
 */
public interface EfUavService {

    /**
     * 通过ID查询单条数据
     *
     * @param uavId 主键
     * @return 实例对象
     */
    EfUav queryById(String uavId);



    /**
     * 通过ID查询单条数据并且获取无人机类型信息
     *
     * @param uavId 主键
     * @return 实例对象
     */
    EfUav queryByIdAndType(String uavId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUav> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efUav 实例对象
     * @return 实例对象
     */
    EfUav insert(EfUav efUav);

    /**
     * 修改数据
     *
     * @param efUav 实例对象
     * @return 实例对象
     */
    EfUav update(EfUav efUav);

    /**
     * 通过主键删除数据
     *
     * @param uavId 主键
     * @return 是否成功
     */
    boolean deleteById(String uavId);

    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId
     * @param Urid
     * @return
     */
    List<EfUav> queryAllUavByCIdOrUrId(Integer cId,Integer Urid);

}
