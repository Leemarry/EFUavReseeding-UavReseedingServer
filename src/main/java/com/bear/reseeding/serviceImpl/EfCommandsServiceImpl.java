package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfCommands;
import com.bear.reseeding.dao.EfCommandsDao;
import com.bear.reseeding.service.EfCommandsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 开源无人机命令(EfCommands)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:06
 */
@Service("efCommandsService")
public class EfCommandsServiceImpl implements EfCommandsService {
    @Resource
    private EfCommandsDao efCommandsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfCommands queryById(Integer id) {
        return this.efCommandsDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfCommands> queryAllByLimit(int offset, int limit) {
        return this.efCommandsDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efCommands 实例对象
     * @return 实例对象
     */
    @Override
    public EfCommands insert(EfCommands efCommands) {
        this.efCommandsDao.insert(efCommands);
        return efCommands;
    }

    /**
     * 修改数据
     *
     * @param efCommands 实例对象
     * @return 实例对象
     */
    @Override
    public EfCommands update(EfCommands efCommands) {
        this.efCommandsDao.update(efCommands);
        return this.queryById(efCommands.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efCommandsDao.deleteById(id) > 0;
    }
}
