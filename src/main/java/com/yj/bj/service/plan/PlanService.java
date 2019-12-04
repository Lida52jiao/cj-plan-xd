package com.yj.bj.service.plan;


import com.yj.bj.entity.PlanEntity;
import com.yj.bj.service.BaseService;
import com.yj.bj.util.YJResult;

/**
 * Created by bin on 2017/11/6.
 */
public interface PlanService extends BaseService<PlanEntity> {
    YJResult savePlan(String planJsonKey);

    YJResult stopPlan(Long planId);
}
