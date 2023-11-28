package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfSysteminfo;
import com.bear.reseeding.dao.EfSysteminfoDao;
import com.bear.reseeding.service.EfSysteminfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公司信息表，每个登录用户都可能有公司，或没有公司(EfSysteminfo)表服务实现类
 *
 * @author makejava
 * @since 2023-11-28 14:21:35
 */
@Service("efSysteminfoService")
public class EfSysteminfoServiceImpl implements EfSysteminfoService {
    @Resource
    private EfSysteminfoDao efSysteminfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfSysteminfo queryById(Integer id) {
        return this.efSysteminfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfSysteminfo> queryAllByLimit(int offset, int limit) {
        return this.efSysteminfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efSysteminfo 实例对象
     * @return 实例对象
     */
    @Override
    public EfSysteminfo insert(EfSysteminfo efSysteminfo) {
        this.efSysteminfoDao.insert(efSysteminfo);
        return efSysteminfo;
    }

    /**
     * 修改数据
     *
     * @param efSysteminfo 实例对象
     * @return 实例对象
     */
    @Override
    public EfSysteminfo update(EfSysteminfo efSysteminfo) {
        this.efSysteminfoDao.update(efSysteminfo);
        return this.queryById(efSysteminfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efSysteminfoDao.deleteById(id) > 0;
    }
}
