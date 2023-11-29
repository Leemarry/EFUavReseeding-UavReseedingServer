package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfCavitySeeding;

import java.util.List;

/**
 * 草原空洞播种记录表(EfCavitySeeding)表服务接口
 *
 * @author makejava
 * @since 2023-11-23 18:54:45
 */
public interface EfCavitySeedingService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCavitySeeding queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCavitySeeding> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param efCavitySeeding 实例对象
     * @return 实例对象
     */
    EfCavitySeeding insert(EfCavitySeeding efCavitySeeding);

    /**
     * 修改数据
     *
     * @param efCavitySeeding 实例对象
     * @return 实例对象
     */
    EfCavitySeeding update(EfCavitySeeding efCavitySeeding);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过洞斑id查询 该 洞斑播种信息
     * @param cavityId
     * @return
     */
    List<EfCavitySeeding> queryBycavityId(Integer cavityId);

}
