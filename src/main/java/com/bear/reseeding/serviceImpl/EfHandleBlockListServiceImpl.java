package com.bear.reseeding.serviceImpl;

import com.bear.reseeding.dao.EfHandleBlockListDao;
import com.bear.reseeding.entity.EfHandleBlockList;
import com.bear.reseeding.service.EfHandleBlockListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("EfHandleBlockListService")
public class EfHandleBlockListServiceImpl implements EfHandleBlockListService {

    @Resource
    private EfHandleBlockListDao efHandleBlockListDao;

    @Override
    public Integer insertBatchByList(List<EfHandleBlockList> blockList) {
        return efHandleBlockListDao.insertBatchByList(blockList);
    }



}
