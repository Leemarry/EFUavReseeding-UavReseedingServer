package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfMediaPhoto;
import com.bear.reseeding.dao.EfMediaPhotoDao;
import com.bear.reseeding.service.EfMediaPhotoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时拍摄照片表(EfMediaPhoto)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:33
 */
@Service("efMediaPhotoService")
public class EfMediaPhotoServiceImpl implements EfMediaPhotoService {
    @Resource
    private EfMediaPhotoDao efMediaPhotoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto queryById(Integer id) {
        return this.efMediaPhotoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfMediaPhoto> queryAllByLimit(int offset, int limit) {
        return this.efMediaPhotoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto insert(EfMediaPhoto efMediaPhoto) {
        this.efMediaPhotoDao.insert(efMediaPhoto);
        return efMediaPhoto;
    }

    /**
     * 修改数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto update(EfMediaPhoto efMediaPhoto) {
        this.efMediaPhotoDao.update(efMediaPhoto);
        return this.queryById(efMediaPhoto.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efMediaPhotoDao.deleteById(id) > 0;
    }

    /**
     *  通过飞行架次
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    @Override
    public  List<EfMediaPhoto> queryByeachsortieIdOruavId(Integer eachsortieId){
        return  efMediaPhotoDao.queryByeachsortieIdOruavId(eachsortieId);
    }
}
