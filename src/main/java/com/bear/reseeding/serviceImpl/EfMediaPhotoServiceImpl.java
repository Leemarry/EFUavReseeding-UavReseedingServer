package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.entity.EfMediaPhoto;
import com.bear.reseeding.dao.EfMediaPhotoDao;
import com.bear.reseeding.service.EfMediaPhotoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 实时拍摄照片表(EfMediaPhoto)表服务实现类
 *
 * @author makejava
 * @since 2023-11-23 18:57:33
 */
@Service("efMediaPhotoService")
public class EfMediaPhotoServiceImpl implements EfMediaPhotoService {
    @Resource
    private EfMediaPhotoDao efMediaPhotoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto queryById(Integer id) {
        return this.efMediaPhotoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<EfMediaPhoto> queryAllByLimit(int offset, int limit) {
        return this.efMediaPhotoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto insert(EfMediaPhoto efMediaPhoto) {
        this.efMediaPhotoDao.insert(efMediaPhoto);
        return efMediaPhoto;
    }

    /**
     * 修改数据
     *
     * @param efMediaPhoto 实例对象
     * @return 实例对象
     */
    @Override
    public EfMediaPhoto update(EfMediaPhoto efMediaPhoto) {
        this.efMediaPhotoDao.update(efMediaPhoto);
        return this.queryById(efMediaPhoto.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.efMediaPhotoDao.deleteById(id) > 0;
    }

    /**
     *  通过飞行架次
     *
     * @param eachsortieId 飞行架次id
     * @return
     */
    @Override
    public  List<EfMediaPhoto> queryByeachsortieIdOruavId(Integer eachsortieId){
        return  efMediaPhotoDao.queryByeachsortieIdOruavId(eachsortieId);
    }


    /**
     * 根据时间和编号查找图片
     *
     * @param newFileName 图片tag
     * @param UavID       无人机编号
     * @return
     */
    @Override
    public EfMediaPhoto queryByCreatTime(String UavID, String newFileName) {
        return efMediaPhotoDao.queryByCreatTime(UavID, newFileName);
    }

    /**
     * 用于查询 无人机该时间最近的图片数据
     * @param uavId
     * @param lastTime
     * @return
     */
    @Override
    public  EfMediaPhoto queryByUavIdAndLatestTime(String uavId, Date lastTime){
        return efMediaPhotoDao.queryByUavIdAndLatestTime(uavId,lastTime);
    }

    @Override
    public EfMediaPhoto fuzzyQuery (String fuzzyTag, String deviceId){
        return efMediaPhotoDao.fuzzyQuery(fuzzyTag,deviceId);
    }

    @Override
    public EfMediaPhoto fuzzyQueryOrAdd(String fuzzyTag,EfMediaPhoto efMediaPhoto){
        //先查询是否有相同的图片
        EfMediaPhoto efMediaPhoto1 =fuzzyQuery(fuzzyTag, efMediaPhoto.getDeviceid());
        if(efMediaPhoto1!=null){
            efMediaPhoto1.setPathImageAnalysis(efMediaPhoto.getPathImageAnalysis());
            efMediaPhoto.setSizeImageAnalysis(efMediaPhoto.getSizeImageAnalysis());  // 分析图片大小
            efMediaPhoto =  update(efMediaPhoto1);
        }else{
            efMediaPhoto = insert(efMediaPhoto);        //没有相同的图片，则新增
        }
        return efMediaPhoto;
    }

}
