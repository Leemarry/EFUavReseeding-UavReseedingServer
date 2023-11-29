package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfCavitySeeding;
import com.bear.reseeding.dao.EfCavitySeedingDao;
import com.bear.reseeding.service.EfCavitySeedingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 草原空洞播种记录表(EfCavitySeeding)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:54:47
 */
@Service("efCavitySeedingService")
public class EfCavitySeedingServiceImpl implements EfCavitySeedingService {
    @Resource
    private EfCavitySeedingDao efCavitySeedingDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfCavitySeeding queryById(Integer id) {
        return this.efCavitySeedingDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfCavitySeeding> queryAllByLimit(int offset, int limit) {
        return this.efCavitySeedingDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efCavitySeeding 实例对象
     * @return 实例对象
     */
    @Override
    public EfCavitySeeding insert(EfCavitySeeding efCavitySeeding) {
        this.efCavitySeedingDao.insert(efCavitySeeding);
        return efCavitySeeding;
    }

    /**
     * 修改数据
     *
     * @param efCavitySeeding 实例对象
     * @return 实例对象
     */
    @Override
    public EfCavitySeeding update(EfCavitySeeding efCavitySeeding) {
        this.efCavitySeedingDao.update(efCavitySeeding);
        return this.queryById(efCavitySeeding.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efCavitySeedingDao.deleteById(id) > 0;
    }


    /**
     * 通过洞斑id查询 该 洞斑播种信息queryBycavityId
     * @param cavityId
     * @return
     */
    @Override
    public List<EfCavitySeeding> queryBycavityId(Integer cavityId){
        return efCavitySeedingDao.queryBycavityId(cavityId);
    }

}
