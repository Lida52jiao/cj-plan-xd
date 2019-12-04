package com.yj.bj.service.plan;


import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.service.BaseService;

/**
 * Created by bin on 2017/11/6.
 */
public interface PlanDetailService extends BaseService<PlanDetailEntity> {
    java.util.List<PlanDetailEntity> getExpireList();

    String save(PlanDetailEntity entity);
}
