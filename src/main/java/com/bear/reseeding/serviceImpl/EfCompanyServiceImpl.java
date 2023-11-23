package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfCompany;
import com.bear.reseeding.dao.EfCompanyDao;
import com.bear.reseeding.service.EfCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公司信息，c_parent_id 用于区分子公司(EfCompany)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:29
 */
@Service("efCompanyService")
public class EfCompanyServiceImpl implements EfCompanyService {
    @Resource
    private EfCompanyDao efCompanyDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfCompany queryById(Integer id) {
        return this.efCompanyDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfCompany> queryAllByLimit(int offset, int limit) {
        return this.efCompanyDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efCompany 实例对象
     * @return 实例对象
     */
    @Override
    public EfCompany insert(EfCompany efCompany) {
        this.efCompanyDao.insert(efCompany);
        return efCompany;
    }

    /**
     * 修改数据
     *
     * @param efCompany 实例对象
     * @return 实例对象
     */
    @Override
    public EfCompany update(EfCompany efCompany) {
        this.efCompanyDao.update(efCompany);
        return this.queryById(efCompany.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efCompanyDao.deleteById(id) > 0;
    }
}
