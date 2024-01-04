package com.bear.reseeding.service.impl;

import com.bear.reseeding.entity.EfUav;
import com.bear.reseeding.dao.EfUavDao;
import com.bear.reseeding.service.EfUavService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 无人机信息表(EfUav)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:59:48
 */
@Service("efUavService")
public class EfUavServiceImpl implements EfUavService {
    @Resource
    private EfUavDao efUavDao;

    /**
     * 通过ID查询单条数据
     *
     * @param uavId 主键
     * @return 实例对象
     */
    @Override
    public EfUav queryById(String uavId) {
        return this.efUavDao.queryById(uavId);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param uavId 主键
     * @return 实例对象
     */
    @Override
    public EfUav queryByIdAndType(String uavId) {
        return this.efUavDao.queryByIdAndType(uavId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfUav> queryAllByLimit(int offset, int limit) {
        return this.efUavDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfUav insert(EfUav efUav) {
        this.efUavDao.insert(efUav);
        return efUav;
    }

    /**
     * 修改数据
     *
     * @param efUav 实例对象
     * @return 实例对象
     */
    @Override
    public EfUav update(EfUav efUav) {
        this.efUavDao.update(efUav);
        return this.queryById(efUav.getUavId());
    }

    /**
     * 通过主键删除数据
     *
     * @param uavId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String uavId) {
        return this.efUavDao.deleteById(uavId) > 0;
    }

    /**
     * 通过无人机所属公司id或者用户角色id查询
     * @param cId
     * @param Urid
     * @return
     */
    @Override
    public  List<EfUav> queryAllUavByCIdOrUrId(Integer cId,Integer Urid){
        return  efUavDao.queryAllUavByCIdOrUrId(cId,Urid);
    }

}
