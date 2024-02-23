package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfUav;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机信息表(EfUav)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-23 18:59:49
 */
public interface EfUavDao {

    /**
     * 通过ID查询单条数据
     *
     * @param uavId 主键
     * @return 实例对象
     */
    EfUav queryById(String uavId);


    /**
     * 通过ID查询单条数据 并且获取无人机类型信息
     *
     * @param uavId 主键
     * @return 实例对象
     */
    EfUav queryByIdAndType(String uavId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EfUav> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param efUav 实例对象
     * @return 对象列表
     */
    List<EfUav> queryAll(EfUav efUav);

    /**
     * 新增数据
     *
     * @param efUav 实例对象
     * @return 影响行数
     */
    int insert(EfUav efUav);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUav>实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EfUav> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUav>实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<EfUav> entities);

    /**
     * 修改数据
     *
     * @param efUav 实例对象
     * @return 影响行数
     */
    int update(EfUav efUav);

    /**
     * 通过主键删除数据
     *
     * @param uavId 主键
     * @return 影响行数
     */
    int deleteById(String uavId);

    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId
     * @param Urid
     * @return
     */
    public  List<EfUav> queryAllUavByCIdOrUrId(@Param("cId") Integer cId,@Param("Urid") Integer Urid);

}

