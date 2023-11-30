package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUavEachsortie;
import com.bear.reseeding.dao.EfUavEachsortieDao;
import com.bear.reseeding.service.EfUavEachsortieService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 无人机每飞行一个架次，产生一条记录。(EfUavEachsortie)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:59:58
 */
@Service("efUavEachsortieService")
public class EfUavEachsortieServiceImpl implements EfUavEachsortieService {
    @Resource
    private EfUavEachsortieDao efUavEachsortieDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUavEachsortie queryById(Integer id) {
        return this.efUavEachsortieDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUavEachsortie> queryAllByLimit(int offset, int limit) {
        return this.efUavEachsortieDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUavEachsortie 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavEachsortie insert(EfUavEachsortie efUavEachsortie) {
        this.efUavEachsortieDao.insert(efUavEachsortie);
        return efUavEachsortie;
    }

    /**
     * 修改数据
     *
     * @param efUavEachsortie 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavEachsortie update(EfUavEachsortie efUavEachsortie) {
        if (this.efUavEachsortieDao.update(efUavEachsortie) >= 0) {
            return efUavEachsortie;
        } else {
            return null;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUavEachsortieDao.deleteById(id) > 0;
    }

    /**
     * 查询架次列表
     *
     * @param uavId     无人机id
     * @param startTime 架次前时间
     * @param endTime   架次后时间
     * @return
     */
    @Override
    public List<EfUavEachsortie> queryByIdOrTime(String uavId, @Param("startTime") String startTime, @Param("endTime") String endTime) {
        return efUavEachsortieDao.queryByIdOrTime(uavId, startTime, endTime);
    }


    /**
     * 根据拍照时间查询架次
     *
     * @param date
     * @param uavId
     * @return
     */
    @Override
    public EfUavEachsortie queryByPhotoTime(@Param("date") Date date, @Param("uavId") String uavId) {
        return this.efUavEachsortieDao.queryByPhotoTime(date, uavId);
    }
}
