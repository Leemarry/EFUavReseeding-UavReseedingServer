package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfHandleWaypoint;

import java.util.List;

public interface EfHandleWaypointService {

    /**
     * 批量新增数
     *
     * @param efHandleWaypoints 数组
     * @return
     */
    Integer insertBatchByList(List<EfHandleWaypoint> efHandleWaypoints);


    Integer insertByList(List<EfHandleWaypoint> efHandleWaypoints);

    /**
     * 根据HandleId查询播种路径点列表
     *
     * @param handleId 处理记录ID
     * @return 实例对象list
     */
    List<EfHandleWaypoint> queryByHandleId(int handleId);
}
