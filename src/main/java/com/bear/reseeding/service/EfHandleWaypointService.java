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
    List<EfHandleWaypoint> insertBatchByList(List<EfHandleWaypoint> efHandleWaypoints);


    Integer insertByList(List<EfHandleWaypoint> efHandleWaypoints);

    /**
     * 根据HandleId查询播种路径点列表
     *
     * @param handleId 处理记录ID
     * @return 实例对象list
     */
    List<EfHandleWaypoint> queryByHandleId(int handleId);

    List<EfHandleWaypoint> queryByHandleIdAndtime(int handleId ,List<Integer> flyTimes);

    List<EfHandleWaypoint> queryByHandleIdandFlyed(int handleId ,Integer flyTimes);

    List<EfHandleWaypoint> queryByHandleIdNofly(int handleId ,Integer flyTimes);


    int updateHandleNmu(int id);
}
