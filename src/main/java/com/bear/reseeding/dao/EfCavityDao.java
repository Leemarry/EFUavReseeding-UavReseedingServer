package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfCavity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 草原空洞表，与照片和飞行架次关联(EfCavity)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:51:52
 */
public interface EfCavityDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EfCavity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfCavity> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efCavity 实例对象
     * @return 对象列表
     */
    List<EfCavity> queryAll(EfCavity efCavity);

    /**
     * 新增数据
     *
     * @param efCavity 实例对象
     * @return 影响行数
     */
    int insert(EfCavity efCavity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCavity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfCavity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfCavity> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfCavity> entities);

    /**
     * 修改数据
     *
     * @param efCavity 实例对象
     * @return 影响行数
     */
    int update(EfCavity efCavity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过飞行架次 查询 洞斑信息
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    List<EfCavity> queryByeachsortieIdOruavId(@Param("eachsortieId") Integer eachsortieId);

}

