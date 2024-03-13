package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfHandleWaypoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EfHandleWaypointDao {

    int insertBatchByList(@Param("entities") List<EfHandleWaypoint> entities);



}
