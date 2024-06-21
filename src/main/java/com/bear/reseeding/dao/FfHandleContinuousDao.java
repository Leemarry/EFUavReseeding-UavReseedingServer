package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfHandleWaypoint;
import com.bear.reseeding.entity.continuousWaypoints;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FfHandleContinuousDao {

    int insertBatchByList(@Param("entities") List<continuousWaypoints> entities);

    List<continuousWaypoints> queryByHandleId(@Param("handleId") int handleId);


    List<continuousWaypoints> queryByHandleIdandFlyed(@Param("handleId") int handleId,@Param("flyTimes") Integer flyTimes);

    List<continuousWaypoints> queryByHandleIdNofly(@Param("handleId") int handleId,@Param("flyTimes") Integer flyTimes);

}
