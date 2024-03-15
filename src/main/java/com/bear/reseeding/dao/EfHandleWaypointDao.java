package com.bear.reseeding.dao;

import com.bear.reseeding.entity.EfHandleWaypoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EfHandleWaypointDao {

    int insertBatchByList(@Param("entities") List<EfHandleWaypoint> entities);

    /**
     * 根据HandleId查询播种路径点列表
     *
     * @param handleId 处理记录ID
     * @return 实例对象list
     */
    List<EfHandleWaypoint> queryByHandleId(@Param("handleId") int handleId);
}
