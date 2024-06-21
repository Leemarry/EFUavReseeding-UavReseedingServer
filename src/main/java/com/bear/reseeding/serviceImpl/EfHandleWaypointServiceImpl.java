package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.EfHandleWaypointDao;
import com.bear.reseeding.entity.EfHandleWaypoint;
import com.bear.reseeding.service.EfHandleWaypointService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("EfHandleWaypointService")

public class EfHandleWaypointServiceImpl implements EfHandleWaypointService {

    @Resource
    private EfHandleWaypointDao efHandleWaypointDao;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<EfHandleWaypoint> insertBatchByList(List<EfHandleWaypoint> efHandleWaypoints) {
       efHandleWaypointDao.insertBatchByList(efHandleWaypoints);
        return efHandleWaypoints;
    }

    @Override
    public Integer insertByList(List<EfHandleWaypoint> efHandleWaypoints) {
//        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
//        Efh studentMapperNew = sqlSession.getMapper(EfHandleWaypointDao.class);
//        studentList.stream().forEach(student -> studentMapperNew.insert(student));
//        sqlSession.commit();
//        sqlSession.clearCache();
        return efHandleWaypointDao.insertBatchByList(efHandleWaypoints);

//        return efHandleWaypointDao.insertByList(efHandleWaypoints);
    }

    /**
     * 根据HandleId查询播种路径点列表
     *
     * @param handleId 处理记录ID
     * @return 实例对象list
     */
    @Override
    public List<EfHandleWaypoint> queryByHandleId(int handleId) {
        return efHandleWaypointDao.queryByHandleId(handleId);
    }

    @Override
    public List<EfHandleWaypoint> queryByHandleIdAndtime(int handleId,List<Integer> flyTimes) {
        return efHandleWaypointDao.queryByHandleIdAndtime(handleId,flyTimes);
    }

    @Override
    public List<EfHandleWaypoint> queryByHandleIdandFlyed(int handleId,Integer flyTimes) {
        return efHandleWaypointDao.queryByHandleIdandFlyed(handleId,flyTimes);
    }


    @Override
    public List<EfHandleWaypoint> queryByHandleIdNofly(int handleId,Integer flyTimes) {
        return efHandleWaypointDao.queryByHandleIdNofly(handleId,flyTimes);
    }





    @Override
    public int updateHandleNmu(int id){
        return  efHandleWaypointDao.updateHandleNmu(id);
    }


}
