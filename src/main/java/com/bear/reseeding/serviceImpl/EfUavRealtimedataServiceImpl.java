package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUavRealtimedata;
import com.bear.reseeding.dao.EfUavRealtimedataDao;
import com.bear.reseeding.service.EfUavRealtimedataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:59:59
 */
@Service("efUavRealtimedataService")
public class EfUavRealtimedataServiceImpl implements EfUavRealtimedataService {
    @Resource
    private EfUavRealtimedataDao efUavRealtimedataDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUavRealtimedata queryById(Integer id) {
        return this.efUavRealtimedataDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUavRealtimedata> queryAllByLimit(int offset, int limit) {
        return this.efUavRealtimedataDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavRealtimedata insert(EfUavRealtimedata efUavRealtimedata) {
        this.efUavRealtimedataDao.insert(efUavRealtimedata);
        return efUavRealtimedata;
    }

    /**
     * 修改数据
     *
     * @param efUavRealtimedata 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavRealtimedata update(EfUavRealtimedata efUavRealtimedata) {
        this.efUavRealtimedataDao.update(efUavRealtimedata);
        return this.queryById(efUavRealtimedata.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUavRealtimedataDao.deleteById(id) > 0;
    }


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EfUavRealtimedata> 实例对象列表
     * @return 影响行数
     */
    @Override
    public int insertBatch(List<EfUavRealtimedata> entities) {
        return this.efUavRealtimedataDao.insertBatch(entities);
    }
}
