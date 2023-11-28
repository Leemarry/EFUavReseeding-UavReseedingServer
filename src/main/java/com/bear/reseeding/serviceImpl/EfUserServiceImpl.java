package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUser;
import com.bear.reseeding.dao.EfUserDao;
import com.bear.reseeding.service.EfUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EfUser)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 19:00:08
 */
@Service("efUserService")
public class EfUserServiceImpl implements EfUserService {
    @Resource
    private EfUserDao efUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUser queryById(Integer id) {
        return this.efUserDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUser> queryAllByLimit(int offset, int limit) {
        return this.efUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUser 实例对象
     * @return 实例对象
     */
    @Override
    public EfUser insert(EfUser efUser) {
        this.efUserDao.insert(efUser);
        return efUser;
    }

    /**
     * 修改数据
     *
     * @param efUser 实例对象
     * @return 实例对象
     */
    @Override
    public EfUser update(EfUser efUser) {
        this.efUserDao.update(efUser);
        return this.queryById(efUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUserDao.deleteById(id) > 0;
    }


    //region 新增

    /**
     * 登录
     *
     * @param userId  登录名
     * @param userPwd 密码
     * @return
     */
    @Override
    public EfUser login(@Param("userId") String userId, @Param("userPwd") String userPwd) {
        return this.efUserDao.login(userId, userPwd);
    }

    /**
     * 查询所有管理员账户，role=1,2,3
     *
     * @return 对象列表
     */
    @Override
    public List<EfUser> queryAllAdmin() {
        return efUserDao.queryAllAdmin();
    }

    //endregion
}
