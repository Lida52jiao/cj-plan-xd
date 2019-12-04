package com.yj.bj.controller;

import com.yj.bj.service.AccountFeignService;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.service.plan.PlanService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bin on 2018/6/22.
 */
@RestController
@RequestMapping("/callback/")
public class CallbackController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CallbackController.class);
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private AccountFeignService accountFeignService;

    /**
     * 支付回调
     * @return
     */
    /*@RequestMapping("payCallback")
    public String payCallback(String agentOrderNo, Long totalMoney, String billStatus, String sign){
        System.out.println("agentOrderNo"+agentOrderNo);
        System.out.println("totalMoney"+totalMoney);
        System.out.println("billStatus"+billStatus);
        System.out.println("sign"+sign);
        HashMap<String, Object> st = new HashMap<>();
        st.put("agentOrderNo", agentOrderNo);
        st.put("totalMoney", totalMoney);
        st.put("billStatus", billStatus);
        String string = SignUtil.createYJSign(st, EasyConstant.key);
        st.put("sign", string);
        logger.info("sign="+sign+"  mySign="+string);
        logger.info(string.equals(sign)+"");
        if(!string.equals(sign)){
            return "ERROR";
        }
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(agentOrderNo);
        //更新订单状态
        if (PlanConstant.FINISH.equals(pd.getState())){
            return YJConstant.SUCCESS_BIG;
        }
        if (YJConstant.SUCCESS_XJ.equals(billStatus)) {
            //通知分润系统
            HashMap<String,Object> accountMap=new HashMap<>();
            accountMap.put("institutionId",pd.getInstitutionId());
            accountMap.put("merchantId",pd.getMerchantId());
            accountMap.put("agentId",pd.getAgentId());
            accountMap.put("appId",pd.getAppId());
            accountMap.put("orderNo",pd.getOrderNo());
            accountMap.put("payType","4");
            accountMap.put("name",pd.getName());
            accountMap.put("phone",pd.getPhone());
            accountMap.put("planId",pd.getPlanId());
            accountMap.put("trade_state",YJConstant.SUCCESS_BIG);
            accountMap.put("total_fee",totalMoney);
            String accountSign=SignUtil.createYJSign(accountMap);
            accountMap.put("sign",accountSign);
            accountMap.put("aisleCode","ld03");
            RepaymentNotify notify=null;
            try {
                notify=(RepaymentNotify)MapUtil.mapToObject(accountMap,RepaymentNotify.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(accountSign);
            if (notify==null){
                return "ERROR";
            }
            boolean mqRsult=sendShare.send2(notify);
            if(mqRsult){
                pd.setIsShare(mqRsult+"");
            }

            pd.setState(PlanConstant.FINISH);//执行状态完成
            pd.setPayState(PlanConstant.SUCCESS);
            pd.setFinishTime(new Date().getTime());
            planDetailService.update(pd);
            //短信
            DecimalFormat df =new DecimalFormat("#.00");
            ConmonUtil.send(BankCode.getName(pd.getBankCode()),df.format(pd.getAmount()/100D)+"",pd.getPhone(),pd.getPlanId()+"",pd.getAppId(),pd.getAgentId(),pd.getInstitutionId(),"2");
            return mqRsult?"SUCCESS":mqRsult+"";
        }
        return "ERROR";
    }

    *//**
     * 代付回调
     * @return
     *//*
    @RequestMapping("repCallback")
    public String repCallback(String agentOrderNo, Long totalMoney, String billStatus, String sign){
        HashMap<String, Object> st = new HashMap<>();
        st.put("agentOrderNo", agentOrderNo);
        st.put("totalMoney", totalMoney);
        st.put("billStatus", billStatus);
        String string = SignUtil.createYJSign(st, EasyConstant.key);
        st.put("sign", string);
        logger.info("sign="+sign+"  mySign="+string);
        logger.info(string.equals(sign)+"");
        if (!string.equals(sign)){
            return "ERROR";
        }
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(agentOrderNo);
        if (PlanConstant.FINISH.equals(pd.getState())){
            return YJConstant.SUCCESS_BIG;
        }
        if (YJConstant.SUCCESS_XJHK.equals(billStatus)) {
            pd.setState(PlanConstant.FINISH);//执行状态完成
            pd.setRepaymentState(PlanConstant.SUCCESS);
            pd.setFinishTime(new Date().getTime());
            pd.setArrivalAmount(totalMoney);
            String result=planDetailService.update(pd);

            PlanDetailEntity findPd=new PlanDetailEntity();
            findPd.setMerchantId(pd.getMerchantId());
            findPd.setCycleId(pd.getCycleId());
            findPd.setPayState(PlanConstant.SUCCESS);
            findPd.setPayType("2");
            List<PlanDetailEntity> payList=planDetailService.queryObjectForList(findPd);
            //Long shareAmount=daifu_amt;

            //已还
            PlanEntity plan=planService.findByPrimaryKey(pd.getPlanId());
            PlanDetailEntity findPd3=new PlanDetailEntity();
            findPd3.setPayType("2");
            findPd3.setPayState(PlanConstant.SUCCESS);
            findPd3.setPlanId(plan.getpId());
            List<PlanDetailEntity> pd3List=planDetailService.queryObjectForList(findPd3);
            Long payAmount=0L;
            for (PlanDetailEntity pd3:pd3List){
                payAmount+=pd3.getArrivalAmount();
            }
            plan.setAlreadyAmount(payAmount);
            //TODO 计划完成状态
            String [] timeStrArr=plan.getTimeStampStr().split(",");
            Long lastDayTime=Long.parseLong(timeStrArr[timeStrArr.length-1]);
            Long toDayTime= DateUtil.zero(new Date().getTime());
            System.out.println("plan-ok:"+pd.getOrderNo()+" "+toDayTime+"  "+lastDayTime);
            if (lastDayTime.equals(toDayTime)){
                plan.setState("6");
            }

            planService.update(plan);
            return YJConstant.SUCCESS_BIG;
        }
        if (YJConstant.FAIL_XJHK.equals(billStatus)) {
            pd.setState(PlanConstant.FINISH);//执行状态完成
            pd.setRepaymentState(PlanConstant.FAIL);
            pd.setFinishTime(new Date().getTime());
            String result=planDetailService.update(pd);
            //短信
            DecimalFormat df =new DecimalFormat("#.00");
            ConmonUtil.send(BankCode.getName(pd.getBankCode()),df.format(pd.getAmount()/100D)+"",pd.getPhone(),pd.getPlanId()+"",pd.getAppId(),pd.getAgentId(),pd.getInstitutionId(),"5");
            return YJConstant.SUCCESS_BIG;
        }
        return "";
    }*/
}
