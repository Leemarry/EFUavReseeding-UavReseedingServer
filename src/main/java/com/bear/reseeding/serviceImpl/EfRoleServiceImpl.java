package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfRole;
import com.bear.reseeding.dao.EfRoleDao;
import com.bear.reseeding.service.EfRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色表(EfRole)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:45
 */
@Service("efRoleService")
public class EfRoleServiceImpl implements EfRoleService {
    @Resource
    private EfRoleDao efRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfRole queryById(Integer id) {
        return this.efRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfRole> queryAllByLimit(int offset, int limit) {
        return this.efRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efRole 实例对象
     * @return 实例对象
     */
    @Override
    public EfRole insert(EfRole efRole) {
        this.efRoleDao.insert(efRole);
        return efRole;
    }

    /**
     * 修改数据
     *
     * @param efRole 实例对象
     * @return 实例对象
     */
    @Override
    public EfRole update(EfRole efRole) {
        this.efRoleDao.update(efRole);
        return this.queryById(efRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efRoleDao.deleteById(id) > 0;
    }
}
