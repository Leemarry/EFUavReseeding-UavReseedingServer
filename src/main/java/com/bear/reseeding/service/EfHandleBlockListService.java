package com.bear.reseeding.service;

import com.bear.reseeding.entity.EfHandleBlockList;

import java.util.List;

public interface EfHandleBlockListService {

    /**
     * 实体数组批量新增
     * @param blockList
     * @return
     */
    Integer insertBatchByList(List<EfHandleBlockList> blockList);



}
