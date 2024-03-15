package com.bear.reseeding.dao;


import com.bear.reseeding.entity.EfHandleBlockList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EfHandleBlockListDao {

    Integer insertBatchByList(@Param("blockList") List<EfHandleBlockList> blockList);

    /**
     * 根据HandleId查询作业地块处理信息
     *
     * @param handleId 处理记录Id
     * @return 实例对象list
     */
    List<EfHandleBlockList> queryByHandleId(@Param("handleId") Integer handleId);
}
