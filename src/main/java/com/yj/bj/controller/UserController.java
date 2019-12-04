package com.yj.bj.controller;

import com.alibaba.fastjson.JSON;
import com.yj.bj.entity.InstitutionEntity;
import com.yj.bj.entity.PlanEntity;
import com.yj.bj.entity.user.CardInformation;
import com.yj.bj.service.plan.InstitutionService;
import com.yj.bj.service.plan.PlanService;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.YJResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bin on 2018/4/8.
 */
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private MerChantsService merChantsService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private PlanService planService;

    @RequestMapping("/getCardList")
    public YJResult getCardList(String cardType, String merchantId, String institutionId, String appId, String token){
        InstitutionEntity institutionEntity=institutionService.findByPrimaryKey(institutionId);
        List<CardInformation> cardList=merChantsService.getCardList(merchantId,cardType,token,institutionEntity.getMerHost());
        for (CardInformation card:cardList){
            PlanEntity plan=new PlanEntity();
            plan.setMerchantId(merchantId);
            plan.setCardNo(card.getCardNumber());
            plan.setAisleCode("ld16");
            List<PlanEntity> planList=planService.queryObjectForList(plan);
            //System.out.println("======"+ JSON.toJSONString(planList));
            if (planList.size()!=0){
                card.setPlan(planList.get(planList.size()-1));
            }

        }
        return YJResult.ok(cardList);
    }
}
