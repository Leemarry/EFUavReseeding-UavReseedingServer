package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUserLogin;
import com.bear.reseeding.dao.EfUserLoginDao;
import com.bear.reseeding.service.EfUserLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户登录记录信息表
 * (EfUserLogin)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 19:00:10
 */
@Service("efUserLoginService")
public class EfUserLoginServiceImpl implements EfUserLoginService {
    @Resource
    private EfUserLoginDao efUserLoginDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUserLogin queryById(Integer id) {
        return this.efUserLoginDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUserLogin> queryAllByLimit(int offset, int limit) {
        return this.efUserLoginDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUserLogin 实例对象
     * @return 实例对象
     */
    @Override
    public EfUserLogin insert(EfUserLogin efUserLogin) {
        this.efUserLoginDao.insert(efUserLogin);
        return efUserLogin;
    }

    /**
     * 修改数据
     *
     * @param efUserLogin 实例对象
     * @return 实例对象
     */
    @Override
    public EfUserLogin update(EfUserLogin efUserLogin) {
        this.efUserLoginDao.update(efUserLogin);
        return this.queryById(efUserLogin.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUserLoginDao.deleteById(id) > 0;
    }
}
