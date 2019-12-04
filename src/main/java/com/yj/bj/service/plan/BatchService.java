package com.yj.bj.service.plan;


import com.yj.bj.entity.BatchEntity;
import com.yj.bj.service.BaseService;

/**
 * Created by bin on 2017/11/6.
 */
public interface BatchService extends BaseService<BatchEntity> {
    String addNumber(String batch, String aisleCode, Long executeDate);
}
