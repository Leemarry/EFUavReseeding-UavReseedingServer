package com.bear.reseeding.dao;


import com.bear.reseeding.entity.EfHandleBlockList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EfHandleBlockListDao {

    Integer insertBatchByList(@Param("blockList") List<EfHandleBlockList> blockList);
}
