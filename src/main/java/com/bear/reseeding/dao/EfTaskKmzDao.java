package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfTaskKmz;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.apache.ibatis.annotations.Param;
import org.docx4j.wml.P;

import java.util.Date;
import java.util.List;

/**
 * 航点任务表(EfTaskKmz)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-18 09:29:47
 */
public interface EfTaskKmzDao {

//    /**
//     * 通过ID查询单条数据
//     *
//     * @param id 主键
//     * @return 实例对象
//     */
//    EfTaskKmz queryById(Integer id);
//
//    /**
//     * 查询指定行数据
//     *
//     * @param offset 查询起始位置
//     * @param limit  查询条数
//     * @return 对象列表
//     */
//    List<EfTaskKmz> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
//
//    /**
//     * 根据公司编号查询
//     *
//     * @param cid 公司编号
//     * @return 对象列表
//     */
//    List<EfTaskKmz> queryAllByCid(@Param("cid") int cid);
//
//
//    /**
//     * 通过实体作为筛选条件查询
//     *
//     * @param efTaskKmz 实例对象
//     * @return 对象列表
//     */
//    List<EfTaskKmz> queryAll(EfTaskKmz efTaskKmz);
//
    /**
     * 新增数据
     *
     * @param efTaskKmz 实例对象
     * @return 影响行数
     */
    int insert(EfTaskKmz efTaskKmz);

    /**
     * 通过公司id与时间段查询
     *
     * @param UcId
     * @param startTime
     * @param endTime
     * @return
     */
    List<EfTaskKmz> queryByUcidAndTime(@Param(value = "UcId") Integer UcId, @Param("startTime") String startTime, @Param("endTime") String endTime);
//
//    /**
//     * 检测航线任务是否已经存在
//     *
//     * @param fileName 航线任务名称
//     * @param cid      公司ID
//     * @return 查询行数
//     */
//    int checkKmzExist(@Param("fileName") String fileName, @Param("cid") int cid);
//
//    /**
//     * 批量新增数据（MyBatis原生foreach方法）
//     *
//     * @param entities List<EfTaskKmz> 实例对象列表
//     * @return 影响行数
//     */
//    int insertBatch(@Param("entities") List<EfTaskKmz> entities);
//
//    /**
//     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
//     *
//     * @param entities List<EfTaskKmz> 实例对象列表
//     * @return 影响行数
//     */
//    int insertOrUpdateBatch(@Param("entities") List<EfTaskKmz> entities);
//
//    /**
//     * 修改数据
//     *
//     * @param efTaskKmz 实例对象
//     * @return 影响行数
//     */
//    int update(EfTaskKmz efTaskKmz);
//
//
//    /**
//     * 重命名
//     */
//    int updateName(@Param("id") int id, @Param("name") String name, @Param("userId") int userId, @Param("updateTime") Date updateTime);
//
//    /**
//     * 重命名
//     */
//    int updateNamePath(@Param("id") int id, @Param("name") String name, @Param("userId") int userId, @Param("path") String path, @Param("updateTime") Date updateTime);
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 影响行数
//     */
//    int deleteById(Integer id);

}
