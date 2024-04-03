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
    public List<EfHandleBlockList> insertBatchByList(List<EfHandleBlockList> blockList) {
        efHandleBlockListDao.insertBatchByList(blockList);
        return blockList;
    }

    /**
     * 根据HandleId查询作业地块处理信息
     *
     * @param handleId 处理记录Id
     * @return 实例对象list
     */
    @Override
    public List<EfHandleBlockList> queryByHandleId(Integer handleId) {
        return efHandleBlockListDao.queryByHandleId(handleId);
    }


}
