package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfCavitySeeding;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 草原空洞播种记录表(EfCavitySeeding)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:54:49
 */
public interface EfCavitySeedingDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCavitySeeding queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCavitySeeding> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efCavitySeeding 实例对象
     * @return 对象列表
     */
    List<EfCavitySeeding> queryAll(EfCavitySeeding efCavitySeeding);

    /**
     * 新增数据
     *
     * @param efCavitySeeding 实例对象
     * @return 影响行数
     */
    int insert(EfCavitySeeding efCavitySeeding);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCavitySeeding> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfCavitySeeding> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCavitySeeding> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfCavitySeeding> entities);

    /**
     * 修改数据
     *
     * @param efCavitySeeding 实例对象
     * @return 影响行数
     */
    int update(EfCavitySeeding efCavitySeeding);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过洞斑id查询 该 洞斑播种信息
     * @param cavityId
     * @return
     */
    List<EfCavitySeeding> queryBycavityId(@Param("cavityId") Integer cavityId);
}

