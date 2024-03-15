package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.EfHandleDao;
import com.bear.reseeding.entity.EfHandle;
import com.bear.reseeding.service.EfHandleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 处理记录表(EfHandle)表服务实现类
 *
 * @author N.
 * @since 2024-03-13 16:14:26
 */
@Service("efHandleService")
public class EfHandleServiceImpl implements EfHandleService {
    @Resource
    private EfHandleDao efHandleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfHandle queryById(Integer id) {
        return this.efHandleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfHandle> queryAllByLimit(int offset, int limit) {
        return this.efHandleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efHandle 实例对象
     * @return 实例对象
     */
    @Override
    public EfHandle insert(EfHandle efHandle) {
        this.efHandleDao.insert(efHandle);
        return efHandle;
    }

    /**
     * 修改数据
     *
     * @param efHandle 实例对象
     * @return 实例对象
     */
    @Override
    public EfHandle update(EfHandle efHandle) {
        this.efHandleDao.update(efHandle);
        return this.queryById(efHandle.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efHandleDao.deleteById(id) > 0;
    }

    /**
     * 根据时间段查询数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 实例对象list
     */
    @Override
    public List<EfHandle> queryByTime(Date startDate, Date endDate) {
        return this.efHandleDao.queryByTime(startDate,endDate);
    }
}
