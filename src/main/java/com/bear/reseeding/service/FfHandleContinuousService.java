package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfHandleWaypoint;
import com.bear.reseeding.entity.continuousWaypoints;

import java.util.List;

public interface FfHandleContinuousService {

    List<continuousWaypoints> insertBatchByList(List<continuousWaypoints> list);

    List<continuousWaypoints> queryByHandleId(int handleId);

    List<continuousWaypoints> queryByHandleIdandFlyed(int handleId , Integer flyTimes);

    List<continuousWaypoints> queryByHandleIdNofly(int handleId ,Integer flyTimes);
}
