package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.FfHandleContinuousDao;
import com.bear.reseeding.entity.EfHandleWaypoint;
import com.bear.reseeding.entity.continuousWaypoints;
import com.bear.reseeding.service.FfHandleContinuousService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("FfHandleContinuousService")
public class FfHandleContinuousServiceImpl implements FfHandleContinuousService {

    @Resource
    private FfHandleContinuousDao continuousWaypointsDao;

    @Override
    public List<continuousWaypoints> insertBatchByList(List<continuousWaypoints> list) {
        continuousWaypointsDao.insertBatchByList(list);
        return list;
    }

    @Override
    public List<continuousWaypoints> queryByHandleId(int handleId) {
        List<continuousWaypoints> list = continuousWaypointsDao.queryByHandleId(handleId);
        return list == null? Collections.emptyList() : list;
    }

    @Override
    public List<continuousWaypoints> queryByHandleIdandFlyed(int handleId, Integer flyTimes) {
        return continuousWaypointsDao.queryByHandleIdandFlyed(handleId,flyTimes);
    }

    @Override
    public List<continuousWaypoints> queryByHandleIdNofly(int handleId,Integer flyTimes) {
        return continuousWaypointsDao.queryByHandleIdNofly(handleId,flyTimes);
    }


}
