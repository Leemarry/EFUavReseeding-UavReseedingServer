package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUavAlternateHome;
import com.bear.reseeding.dao.EfUavAlternateHomeDao;
import com.bear.reseeding.service.EfUavAlternateHomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机备降点表，一个无人机可设置多个备降点(EfUavAlternateHome)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:59:49
 */
@Service("efUavAlternateHomeService")
public class EfUavAlternateHomeServiceImpl implements EfUavAlternateHomeService {
    @Resource
    private EfUavAlternateHomeDao efUavAlternateHomeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUavAlternateHome queryById(Integer id) {
        return this.efUavAlternateHomeDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUavAlternateHome> queryAllByLimit(int offset, int limit) {
        return this.efUavAlternateHomeDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavAlternateHome insert(EfUavAlternateHome efUavAlternateHome) {
        this.efUavAlternateHomeDao.insert(efUavAlternateHome);
        return efUavAlternateHome;
    }

    /**
     * 修改数据
     *
     * @param efUavAlternateHome 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavAlternateHome update(EfUavAlternateHome efUavAlternateHome) {
        this.efUavAlternateHomeDao.update(efUavAlternateHome);
        return this.queryById(efUavAlternateHome.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUavAlternateHomeDao.deleteById(id) > 0;
    }
}
