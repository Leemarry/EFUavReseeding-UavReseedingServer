package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfCavity;
import com.bear.reseeding.dao.EfCavityDao;
import com.bear.reseeding.service.EfCavityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 草原空洞表，与照片和飞行架次关联(EfCavity)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:51:27
 */
@Service("efCavityService")
public class EfCavityServiceImpl implements EfCavityService {
    @Resource
    private EfCavityDao efCavityDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfCavity queryById(Integer id) {
        return this.efCavityDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfCavity> queryAllByLimit(int offset, int limit) {
        return this.efCavityDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efCavity 实例对象
     * @return 实例对象
     */
    @Override
    public EfCavity insert(EfCavity efCavity) {
        this.efCavityDao.insert(efCavity);
        return efCavity;
    }

    /**
     * 修改数据
     *
     * @param efCavity 实例对象
     * @return 实例对象
     */
    @Override
    public EfCavity update(EfCavity efCavity) {
        this.efCavityDao.update(efCavity);
        return this.queryById(efCavity.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efCavityDao.deleteById(id) > 0;
    }

    /**
     * 通过飞行架次 查询 洞斑信息 queryByeachsortieIdOruavId
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    public List<EfCavity> queryByeachsortieIdOruavId(Integer eachsortieId){
        return  efCavityDao.queryByeachsortieIdOruavId(eachsortieId);
    }
}
