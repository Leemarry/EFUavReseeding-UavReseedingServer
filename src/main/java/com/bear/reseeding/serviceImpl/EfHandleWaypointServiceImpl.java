package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.EfHandleWaypointDao;
import com.bear.reseeding.entity.EfHandleWaypoint;
import com.bear.reseeding.service.EfHandleWaypointService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("EfHandleWaypointService")
public class EfHandleWaypointServiceImpl implements EfHandleWaypointService {

    @Resource
    private EfHandleWaypointDao efHandleWaypointDao;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Integer insertBatchByList(List<EfHandleWaypoint> efHandleWaypoints) {
        return efHandleWaypointDao.insertBatchByList(efHandleWaypoints);
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


}
