package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfRelationUavsn;
import com.bear.reseeding.dao.EfRelationUavsnDao;
import com.bear.reseeding.service.EfRelationUavsnService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机原始唯一编号与系统唯一编号对应表，原始唯一编号很长，在系统中需要缩短(EfRelationUavsn)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:40
 */
@Service("efRelationUavsnService")
public class EfRelationUavsnServiceImpl implements EfRelationUavsnService {
    @Resource
    private EfRelationUavsnDao efRelationUavsnDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfRelationUavsn queryById(Integer id) {
        return this.efRelationUavsnDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfRelationUavsn> queryAllByLimit(int offset, int limit) {
        return this.efRelationUavsnDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efRelationUavsn 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationUavsn insert(EfRelationUavsn efRelationUavsn) {
        this.efRelationUavsnDao.insert(efRelationUavsn);
        return efRelationUavsn;
    }

    /**
     * 修改数据
     *
     * @param efRelationUavsn 实例对象
     * @return 实例对象
     */
    @Override
    public EfRelationUavsn update(EfRelationUavsn efRelationUavsn) {
        this.efRelationUavsnDao.update(efRelationUavsn);
        return this.queryById(efRelationUavsn.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efRelationUavsnDao.deleteById(id) > 0;
    }
}
