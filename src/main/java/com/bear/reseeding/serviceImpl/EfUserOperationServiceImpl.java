package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUserOperation;
import com.bear.reseeding.dao.EfUserOperationDao;
import com.bear.reseeding.service.EfUserOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户操作记录表，记录修改数据，删除数据等所有的操作。(EfUserOperation)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 19:00:11
 */
@Service("efUserOperationService")
public class EfUserOperationServiceImpl implements EfUserOperationService {
    @Resource
    private EfUserOperationDao efUserOperationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUserOperation queryById(Integer id) {
        return this.efUserOperationDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUserOperation> queryAllByLimit(int offset, int limit) {
        return this.efUserOperationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUserOperation 实例对象
     * @return 实例对象
     */
    @Override
    public EfUserOperation insert(EfUserOperation efUserOperation) {
        this.efUserOperationDao.insert(efUserOperation);
        return efUserOperation;
    }

    /**
     * 修改数据
     *
     * @param efUserOperation 实例对象
     * @return 实例对象
     */
    @Override
    public EfUserOperation update(EfUserOperation efUserOperation) {
        this.efUserOperationDao.update(efUserOperation);
        return this.queryById(efUserOperation.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUserOperationDao.deleteById(id) > 0;
    }
}
