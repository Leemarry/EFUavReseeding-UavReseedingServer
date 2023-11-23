package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUavType;
import com.bear.reseeding.dao.EfUavTypeDao;
import com.bear.reseeding.service.EfUavTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机类型表(EfUavType)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 19:00:00
 */
@Service("efUavTypeService")
public class EfUavTypeServiceImpl implements EfUavTypeService {
    @Resource
    private EfUavTypeDao efUavTypeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUavType queryById(Integer id) {
        return this.efUavTypeDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUavType> queryAllByLimit(int offset, int limit) {
        return this.efUavTypeDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUavType 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavType insert(EfUavType efUavType) {
        this.efUavTypeDao.insert(efUavType);
        return efUavType;
    }

    /**
     * 修改数据
     *
     * @param efUavType 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavType update(EfUavType efUavType) {
        this.efUavTypeDao.update(efUavType);
        return this.queryById(efUavType.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUavTypeDao.deleteById(id) > 0;
    }
}
