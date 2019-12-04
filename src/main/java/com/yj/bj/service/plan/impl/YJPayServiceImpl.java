package com.yj.bj.service.plan.impl;

import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.service.plan.PlanService;
import com.yj.bj.service.plan.YJPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by bin on 2017/11/6.
 */
@Service
public class YJPayServiceImpl implements YJPayService {
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private PlanService planService;

    /*@Override
    public String pay(PlanDetailEntity pd){
        pd.setState(PlanConstant.LOCK);
        pd.setPayState(PlanConstant.EXECUTE);
        planDetailService.update(pd);
        PlanEntity plan = new PlanEntity();
        plan.setpId(pd.getPlanId());
        PlanEntity t = planService.findByObject(plan);
        //支付
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("totalMoney", pd.getAmount());
        hashMap.put("agentRepaymentNo", t.getAgentRepaymentNo());
        hashMap.put("agentRepaymentBillNo", pd.getOrderNo());
        hashMap.put("type", "WITHHOLD");
        hashMap.put("fee0", pd.getRate());
        hashMap.put("d0fee", pd.getD0Fee());
        hashMap.put("notifyUrl", EasyConstant.callbackPay);
        hashMap.put("institutionId", EasyConstant.id);
        String signs = SignUtil.createYJSign(hashMap, EasyConstant.key);
        hashMap.put("sign", signs);
        String resultPay= HttpClientUtil.doPost(EasyConstant.easyHost+"/plan/bill",hashMap);
        System.out.println(resultPay);
        return resultPay;
    }

    @Override
    public String repayment(PlanDetailEntity pd){
        PlanDetailEntity fpd=new PlanDetailEntity();
        fpd.setMerchantId(pd.getMerchantId());
        fpd.setCycleId(pd.getCycleId());
        List<PlanDetailEntity> pdList=planDetailService.queryObjectForList(fpd);
        PlanEntity plan = new PlanEntity();
        plan.setpId(pd.getPlanId());
        PlanEntity t = planService.findByObject(plan);
        Long repAmount=0L;
        String RepaymentOrderReality="";
        for (int i=0;i<pdList.size();i++){
            if (PlanConstant.SUCCESS.equals(pdList.get(i).getPayState())){
                repAmount+=pdList.get(i).getArrivalAmount();
            }
            if(i!=0){
                RepaymentOrderReality+=",";
            }
            RepaymentOrderReality+=pdList.get(i).getOrderNo();
        }
        pd.setState(PlanConstant.LOCK);
        pd.setRepaymentState(PlanConstant.EXECUTE);
        pd.setRepaymentOrderReality(RepaymentOrderReality);
        planDetailService.update(pd);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("totalMoney", repAmount - 100L);
        hashMap.put("agentRepaymentNo", t.getAgentRepaymentNo());
        hashMap.put("agentRepaymentBillNo", pd.getOrderNo());
        hashMap.put("type", "PAYMENT");
        hashMap.put("fee0", pd.getRate());
        hashMap.put("d0fee", pd.getD0Fee());
        hashMap.put("notifyUrl", EasyConstant.callbackPep);
        hashMap.put("institutionId", EasyConstant.id);
        String signs = SignUtil.createYJSign(hashMap, EasyConstant.key);
        hashMap.put("sign", signs);
        String resultPay= HttpClientUtil.doPost(EasyConstant.easyHost+"/plan/bill",hashMap);
        System.out.println(resultPay);
        return resultPay;
    }*/
}
