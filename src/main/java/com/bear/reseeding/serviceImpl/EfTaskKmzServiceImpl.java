package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.EfTaskKmzDao;
import com.bear.reseeding.entity.EfTaskKmz;
import com.bear.reseeding.service.EfTaskKmzService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 航点任务表(EfTaskKmz)表服务实现类
 *
 * @author makejava
 * @since 2022-11-18 09:29:46
 */
@Service("efTaskKmzService")
public class EfTaskKmzServiceImpl implements EfTaskKmzService {
    @Resource
    private EfTaskKmzDao efTaskKmzDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfTaskKmz queryById(Integer id) {
        return this.efTaskKmzDao.queryById(id);
    }
//
//    /**
//     * 查询多条数据
//     *
//     * @param offset 查询起始位置
//     * @param limit  查询条数
//     * @return 对象列表
//     */
//    @Override
//    public List<EfTaskKmz> queryAllByLimit(int offset, int limit) {
//        return this.efTaskKmzDao.queryAllByLimit(offset, limit);
//    }
//
//
//    /**
//     * 根据公司编号查询
//     *
//     * @param cid 公司编号
//     * @return 对象列表
//     */
//    @Override
//    public List<EfTaskKmz> queryAllByCid(int cid) {
//        return this.efTaskKmzDao.queryAllByCid(cid);
//    }
//
    /**
     * 新增数据
     *
     * @param efTaskKmz 实例对象
     * @return 实例对象
     */
    @Override
    public EfTaskKmz insert(EfTaskKmz efTaskKmz) {
        if (this.efTaskKmzDao.insert(efTaskKmz) > 0) {
            return efTaskKmz;
        } else {
            return null;
        }
    }

    /**
     *通过公司id与时间段查询航线任务列表
     *
     * @param UcId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public  List<EfTaskKmz> queryByUcidAndTime(Integer UcId,String startTime,String endTime){
       List<EfTaskKmz>  efTaskKmzList =  efTaskKmzDao.queryByUcidAndTime(UcId, startTime, endTime);
       return  efTaskKmzList;
    }
//
//
    /**
     * 检测航线任务是否已经存在
     *
     * @param fileName 航线任务名称
     * @param cid      公司ID
     * @return boolean
     */
    @Override
    public boolean checkKmzExist(String fileName, int cid) {
        return (this.efTaskKmzDao.checkKmzExist(fileName, cid) > 0);
    }


    @Override
    public List<String> includeSamename(String name,Integer cid){
        return this.efTaskKmzDao.includeSamename(name,cid);
    }

    @Override
    public List<Integer> includeSamenames(String name,Integer cid,Integer id,String startTime,String endTime){
        return this.efTaskKmzDao.includeSamenames(name,cid,id,startTime,endTime);
    }

    @Override
    public List<Integer> includeSame(String name,Integer cid,String startTime,String endTime){
        return this.efTaskKmzDao.includeSame(name,cid,startTime,endTime);
    }



//
//
//    /**
//     * 修改数据
//     *
//     * @param efTaskKmz 实例对象
//     * @return 实例对象
//     */
//    @Override
//    public EfTaskKmz update(EfTaskKmz efTaskKmz) {
//        int count = this.efTaskKmzDao.update(efTaskKmz);
//        if (count > 0) {
//            return this.queryById(efTaskKmz.getId());
//        } else {
//            return null;
//        }
//    }
//
//
    /**
     * 重命名
     */
    @Override
    public EfTaskKmz updateName(int id, String name, int userId) {
        int count = this.efTaskKmzDao.updateName(id, name, userId, new Date());
        if (count > 0) {
            return this.queryById(id);
        } else {
            return null;
        }
    }

    /**
     * 重命名
     */
    @Override
    public int updateCurrentWpNo(int id, int CurrentWpNo, int userId) {
        return  this.efTaskKmzDao.updateCurrentWpNo(id, CurrentWpNo, userId, new Date());
    }

//
//    /**
//     * 重命名
//     */
//    @Override
//    public EfTaskKmz updateNamePath(int id, String name, int userId, String path) {
//        int count = this.efTaskKmzDao.updateNamePath(id, name, userId, path, new Date());
//        if (count > 0) {
//            return this.queryById(id);
//        } else {
//            return null;
//        }
//    }
//
//
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efTaskKmzDao.deleteById(id) > 0;
    }
}
