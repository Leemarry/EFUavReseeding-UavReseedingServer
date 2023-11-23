package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUavAnnouncement;
import com.bear.reseeding.dao.EfUavAnnouncementDao;
import com.bear.reseeding.service.EfUavAnnouncementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机管制公告发布(EfUavAnnouncement)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:59:50
 */
@Service("efUavAnnouncementService")
public class EfUavAnnouncementServiceImpl implements EfUavAnnouncementService {
    @Resource
    private EfUavAnnouncementDao efUavAnnouncementDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfUavAnnouncement queryById(Integer id) {
        return this.efUavAnnouncementDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUavAnnouncement> queryAllByLimit(int offset, int limit) {
        return this.efUavAnnouncementDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavAnnouncement insert(EfUavAnnouncement efUavAnnouncement) {
        this.efUavAnnouncementDao.insert(efUavAnnouncement);
        return efUavAnnouncement;
    }

    /**
     * 修改数据
     *
     * @param efUavAnnouncement 实例对象
     * @return 实例对象
     */
    @Override
    public EfUavAnnouncement update(EfUavAnnouncement efUavAnnouncement) {
        this.efUavAnnouncementDao.update(efUavAnnouncement);
        return this.queryById(efUavAnnouncement.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efUavAnnouncementDao.deleteById(id) > 0;
    }
}
