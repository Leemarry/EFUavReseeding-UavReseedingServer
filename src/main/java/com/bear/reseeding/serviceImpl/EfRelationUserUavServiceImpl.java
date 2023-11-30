package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfRelationUserUav;
import com.bear.reseeding.dao.EfRelationUserUavDao;
import com.bear.reseeding.service.EfRelationUserUavService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机与用户为多对多关系(EfRelationUserUav)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:43
 */
@Service("efRelationUserUavService")
public class EfRelationUserUavServiceImpl implements EfRelationUserUavService {
    @Resource
    private EfRelationUserUavDao efRelationUserUavDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfRelationUserUav queryById(Integer id) {
        return this.efRelationUserUavDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfRelationUserUav> queryAllByLimit(int offset, int limit) {
        return this.efRelationUserUavDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efRelationUserUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationUserUav insert(EfRelationUserUav efRelationUserUav) {
        this.efRelationUserUavDao.insert(efRelationUserUav);
        return efRelationUserUav;
    }

    /**
     * 修改数据
     *
     * @param efRelationUserUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationUserUav update(EfRelationUserUav efRelationUserUav) {
        this.efRelationUserUavDao.update(efRelationUserUav);
        return this.queryById(efRelationUserUav.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efRelationUserUavDao.deleteById(id) > 0;
    }

    /**
     * 根据用户角色查询无人机信息
     * @param urId
     * @return
     */
    @Override
    public List<EfRelationUserUav> queryByUrid (Integer urId){
        return  efRelationUserUavDao.queryByUrid(urId);
    }

}
