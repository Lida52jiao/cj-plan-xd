package com.yj.bj.service.plan.impl;


import com.yj.bj.entity.BatchEntity;
import com.yj.bj.mapper.BatchMapper;
import com.yj.bj.service.BaseServiceImpl;
import com.yj.bj.service.plan.BatchService;
import com.yj.bj.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by bin on 2017/11/6.
 */
@Service
public class BatchServiceImpl extends BaseServiceImpl<BatchEntity> implements BatchService {
    @Autowired
    private BatchMapper batchMapper;
    @Override
    public String addNumber(String batch,String aisleCode,Long executeDate){
        executeDate= DateUtil.zero(executeDate);
        createBatch(batch,aisleCode,executeDate);
        return batchMapper.addNumber(batch,aisleCode,executeDate) > 0 ? SUCCESS : ERROR;
    }
    public int createBatch(String batch,String aisleCode,Long executeDate){
        BatchEntity batchEntity=new BatchEntity();
        batchEntity.setBatch(batch);
        batchEntity.setAisleCode(aisleCode);
        batchEntity.setExecuteDate(executeDate);
        batchEntity=batchMapper.selectOne(batchEntity);
        if(null==batchEntity){
            BatchEntity batchNew=new BatchEntity();
            batchNew.setBatch(batch);
            batchNew.setAisleCode(aisleCode);
            batchNew.setExecuteDate(executeDate);
            batchNew.setNumber(0L);
            return batchMapper.insert(batchNew);
        }
        return 0;
    }
}
