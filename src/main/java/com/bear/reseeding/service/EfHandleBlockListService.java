package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfHandleBlockList;

import java.util.List;

public interface EfHandleBlockListService {

    /**
     * 实体数组批量新增
     *
     * @param blockList
     * @return
     */
    List<EfHandleBlockList> insertBatchByList(List<EfHandleBlockList> blockList);

    /**
     * 根据HandleId查询作业地块处理信息
     *
     * @param handleId 处理记录Id
     * @return 实例对象list
     */
    List<EfHandleBlockList> queryByHandleId(Integer handleId);


}
