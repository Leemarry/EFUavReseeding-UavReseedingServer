package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.TUser;
import com.bear.reseeding.dao.TUserDao;
import com.bear.reseeding.service.TUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表，用户属于部门或区域，可不关联表示未分配部门或区域(TUser)表服务实现类
 *
 * @author makejava
 * @since 2023-11-10 15:51:16
 */
@Service("tUserService")
public class TUserServiceImpl implements TUserService {
    @Resource
    private TUserDao tUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TUser queryById(Integer id) {
        return this.tUserDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<TUser> queryAllByLimit(int offset, int limit) {
        return this.tUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser insert(TUser tUser) {
        this.tUserDao.insert(tUser);
        return tUser;
    }

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser update(TUser tUser) {
        this.tUserDao.update(tUser);
        return this.queryById(tUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tUserDao.deleteById(id) > 0;
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
    public TUser login(@Param("userId") String userId, @Param("userPwd") String userPwd) {
        return this.tUserDao.login(userId, userPwd);
    }

    //endregion
}
