package com.yj.bj.service.plan.impl;


import com.yj.bj.constant.PlanConstant;
import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.mapper.PlanDetailMapper;
import com.yj.bj.service.BaseServiceImpl;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by bin on 2017/11/6.
 */
@Service
public class PlanDetailServiceImpl extends BaseServiceImpl<PlanDetailEntity> implements PlanDetailService {
    @Autowired
    private PlanDetailMapper planDetailMapper;

    @Override
    public List<PlanDetailEntity> getExpireList() {
        Long date=new Date().getTime();
        return planDetailMapper.getExpireList(date,date-1*60*60*1000L, PlanConstant.EXECUTE);
    }

    @Override
    public String save(PlanDetailEntity entity) {
        //获取当天日期
        String createDateStr= DateUtil.longToString(entity.getExecuteTime(),"yyyy-MM-dd");
        int result = 0;
        entity.setExecuteDateStr(createDateStr);
        result = this.planDetailMapper.insertSelective(entity);
        return result > 0 ? SUCCESS : ERROR;
    }
}
