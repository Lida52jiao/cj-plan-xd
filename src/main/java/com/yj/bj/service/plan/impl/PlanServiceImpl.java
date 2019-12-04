package com.yj.bj.service.plan.impl;

import com.alibaba.fastjson.JSON;
import com.yj.bj.constant.PlanConstant;
import com.yj.bj.constant.YJConstant;
import com.yj.bj.entity.InstitutionEntity;
import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.entity.PlanEntity;
import com.yj.bj.entity.perameter.PlanParmeter;
import com.yj.bj.entity.user.MerChants;
import com.yj.bj.mapper.PlanMapper;
import com.yj.bj.service.BaseServiceImpl;
import com.yj.bj.service.plan.BatchService;
import com.yj.bj.service.plan.InstitutionService;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.service.plan.PlanService;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.DateUtil;
import com.yj.bj.util.RedisUtils;
import com.yj.bj.util.YJResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by bin on 2017/11/6.
 */
@Service
public class PlanServiceImpl extends BaseServiceImpl<PlanEntity> implements PlanService {
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private MerChantsService merChantsService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private BatchService batchService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public YJResult savePlan(String planJsonKey) {
        Jedis jedis= RedisUtils.getJedis();
        String planJson=jedis.get(planJsonKey);
        String uid= UUID.randomUUID().toString();
        PlanParmeter p= JSON.parseObject(planJson,PlanParmeter.class);
        PlanEntity plan=p.getPlan();
        InstitutionEntity in=institutionService.findByPrimaryKey(plan.getInstitutionId());
        MerChants mer=merChantsService.getMerRate(plan.getMerchantId(),plan.getAisleCode(),in.getMerHost());
        if(mer==null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }

        PlanEntity findPlan=new PlanEntity();
        findPlan.setMerchantId(plan.getMerchantId());
        findPlan.setCardNo(plan.getCardNo());
        findPlan.setState("2");
        findPlan.setIsLd("1");
        findPlan.setAisleCode("ld16");
        List<PlanEntity> planList=planMapper.select(findPlan);
        if (planList.size()>0){
            //System.out.println(planList.size());
            return YJResult.build("9395","已有正在执行的计划");
        }
        //plan.setAgentRepaymentNo(agentRepaymentNo);
        plan.setRemarks(uid);
        planMapper.insertSelective(plan);
        PlanEntity newp=new PlanEntity();
        newp.setRemarks(plan.getRemarks());
        newp=planMapper.selectOne(newp);
        List<PlanDetailEntity> pdList=p.getList();
        for (PlanDetailEntity pd:pdList){
            pd.setPlanId(newp.getpId());
            if (pd.getBatch()==null){
                pd.setBatch("1");
            }
            batchService.addNumber(pd.getBatch(),pd.getAisleCode(),pd.getExecuteTime());
            planDetailService.save(pd);
        }
        RedisUtils.returnResource(jedis);
        return YJResult.ok();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public YJResult stopPlan(Long planId) {
        PlanEntity plan=planMapper.selectByPrimaryKey(planId);
        plan.setState(PlanConstant.FAIL);
        plan.setFinishTime(new Date().getTime());
        plan.setRemarks("人为中断");
        planMapper.updateByPrimaryKeySelective(plan);
        PlanDetailEntity planDetailEntity=new PlanDetailEntity();
        planDetailEntity.setPlanId(planId);
        List<PlanDetailEntity> planDetailList= planDetailService.queryObjectForList(planDetailEntity);
        for(PlanDetailEntity planDetail:planDetailList){
            if(PlanConstant.LOCK.equals(planDetail.getState())|| PlanConstant.FINISH.equals(planDetail.getState())){
                continue;
            }
            if(planDetail.getPayType().equals("1")){
                if (planDetail.getExecuteTime()< DateUtil.zero(new Date().getTime())+24*60*60*1000L){
                    continue;
                }
            }
            planDetail.setState(PlanConstant.FAIL);
            planDetail.setRemarks("人为中断");
            planDetailService.update(planDetail);
        }
        return YJResult.ok();
    }
}
