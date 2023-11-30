package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfRelationCompanyUav;
import com.bear.reseeding.dao.EfRelationCompanyUavDao;
import com.bear.reseeding.entity.EfUav;
import com.bear.reseeding.service.EfRelationCompanyUavService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公司与无人机绑定关系表，通常情况下，一个公司可包含多架无人机，但一架无人机只能属于一个公司，特殊情况下允许无人机属于多个公司(EfRelationCompanyUav)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:37
 */
@Service("efRelationCompanyUavService")
public class EfRelationCompanyUavServiceImpl implements EfRelationCompanyUavService {
    @Resource
    private EfRelationCompanyUavDao efRelationCompanyUavDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfRelationCompanyUav queryById(Integer id) {
        return this.efRelationCompanyUavDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfRelationCompanyUav> queryAllByLimit(int offset, int limit) {
        return this.efRelationCompanyUavDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationCompanyUav insert(EfRelationCompanyUav efRelationCompanyUav) {
        this.efRelationCompanyUavDao.insert(efRelationCompanyUav);
        return efRelationCompanyUav;
    }

    /**
     * 修改数据
     *
     * @param efRelationCompanyUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationCompanyUav update(EfRelationCompanyUav efRelationCompanyUav) {
        this.efRelationCompanyUavDao.update(efRelationCompanyUav);
        return this.queryById(efRelationCompanyUav.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efRelationCompanyUavDao.deleteById(id) > 0;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<EfRelationCompanyUav> queryAll2() {
        return this.efRelationCompanyUavDao.queryAll2();
    }
    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId
     * @param urId
     * @return
     */
    @Override
    public  List<EfRelationCompanyUav> queryAllUavByCIdOrUrId(Integer cId, Integer urId){
        return  efRelationCompanyUavDao.queryAllUavByCIdOrUrId(cId,urId);
    }
}
