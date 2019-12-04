package com.yj.bj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yj.bj.constant.EasyConstant;
import com.yj.bj.constant.PlanConstant;
import com.yj.bj.entity.BindEntity;
import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.entity.PlanEntity;
import com.yj.bj.entity.SignEntity;
import com.yj.bj.service.plan.BindService;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.service.plan.PlanService;
import com.yj.bj.service.plan.SignService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by gl on 2019/5/31.
 */
@RestController
@RequestMapping("/test/")
public class Test {

    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private SignService signService;
    @Autowired
    private BindService bindService;
    @Autowired
    private PlanService planService;

//    @RequestMapping("test")
//    public String selects(String orderNo) throws Exception {
//        PlanDetailEntity pd = planDetailService.findByPrimaryKey(orderNo);
//
//        SignEntity signDB=new SignEntity();
//        signDB.setSignState("2");
//        signDB.setMerchantId(pd.getMerchantId());
//        signDB.setCreditCardNumber(pd.getCardNo());
//        signDB=signService.findByObject(signDB);
//        if (signDB==null){
//            return "失败";
//        }
//
//        PlanDetailEntity findPd=new PlanDetailEntity();
//        findPd.setCycleId(pd.getCycleId());
//        findPd.setPayType("2");
//        List<PlanDetailEntity> payPdList=planDetailService.queryObjectForList(findPd);
//        String orderNos="";
//        Long repAmount=0L;
//        for (int i=0;i<payPdList.size();i++){
//            PlanDetailEntity ppd=payPdList.get(i);
//            if (PlanConstant.SUCCESS.equals(ppd.getPayState())){
//                if (i!=0)orderNos+=",";
//                orderNos+=ppd.getOrderNo();
//                repAmount+=ppd.getArrivalAmount();
//            }
//        }
//        if (repAmount.equals(0L)){
//            return "还款金额0";
//        }
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//        // 敏感数据3DES加密
//        String bankAccountNoCipher = EncryptUtil.desEncrypt(pd.getCardNo(), EasyConstant.key);
//
//
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("reqFlowNo", orderNo + "1");
//        signParams.put("merchantCode", signDB.getAisleMerId());
//        signParams.put("walletType", "402");
//        signParams.put("amount", repAmount);
//        signParams.put("bankAccountName", pd.getName());
//        signParams.put("bankAccountNo", pd.getCardNo());
//        signParams.put("bankName", "中国银行");
//        signParams.put("bankSubName", "中国银行海淀支行");
//        signParams.put("bankChannelNo", "010200000000");
//
//
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("reqFlowNo", orderNo + "1");
//        jsonObj.put("merchantCode", signDB.getAisleMerId());
//        jsonObj.put("walletType", "402");
//        jsonObj.put("amount", repAmount);
//        jsonObj.put("bankAccountName", pd.getName());
//        jsonObj.put("bankAccountNo", bankAccountNoCipher);
//        jsonObj.put("bankName", "中国银行");
//        jsonObj.put("bankSubName", "中国银行海淀支行");
//        jsonObj.put("bankChannelNo", "010200000000");
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/trans/withdraw", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            pd.setResponseMsg(jsonRsp);
//            planDetailService.update(pd);
//            System.out.println(orderNo + "---------------------------"+jsonRsp);
//        }
//        return "成功";
//    }
//
//    @RequestMapping("/findState")
//    public String findState(String orderNo) throws Exception {
//        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
//        BindEntity bindEntity = new BindEntity();
//        bindEntity.setState("000000");
//        bindEntity.setMerchantId(pd.getMerchantId());
//        BindEntity bind = bindService.findByObject(bindEntity);
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("reqFlowNo",  pd.getOrderNo() + "1");
//        signParams.put("merchantCode", bind.getAisleMerId());
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("reqFlowNo", pd.getOrderNo() + "1");
//        jsonObj.put("merchantCode", bind.getAisleMerId());
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/trans/withdrawQuery", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            JSONObject job = JSONObject.parseObject(jsonRsp);
//            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//            boolean isSuccess="1".equals(jobs.getString("remitStatus"));
//            if (isSuccess) {
//                pd.setState(PlanConstant.FINISH);//执行状态完成
//                pd.setRepaymentState(PlanConstant.SUCCESS);
//                pd.setFinishTime(new Date().getTime());
//                pd.setArrivalAmount(jobs.getLong("receiveAmount"));
//                String result=planDetailService.update(pd);
//
//                PlanDetailEntity findPd=new PlanDetailEntity();
//                findPd.setMerchantId(pd.getMerchantId());
//                findPd.setCycleId(pd.getCycleId());
//                findPd.setPayState(PlanConstant.SUCCESS);
//                findPd.setPayType("2");
//                List<PlanDetailEntity> payList=planDetailService.queryObjectForList(findPd);
//                //Long shareAmount=daifu_amt;
//
//                //已还
//                PlanEntity plan=planService.findByPrimaryKey(pd.getPlanId());
//                PlanDetailEntity findPd3=new PlanDetailEntity();
//                findPd3.setPayType("1");
//                findPd3.setRepaymentState(PlanConstant.SUCCESS);
//                findPd3.setPlanId(plan.getpId());
//                List<PlanDetailEntity> pd3List=planDetailService.queryObjectForList(findPd3);
//                Long repAmount=0L;
//                for (PlanDetailEntity pd3:pd3List){
//                    repAmount+=pd3.getArrivalAmount();
//                }
//                plan.setAlreadyAmount(repAmount);
//                //TODO 计划完成状态
//                String [] timeStrArr=plan.getTimeStampStr().split(",");
//                Long lastDayTime=Long.parseLong(timeStrArr[timeStrArr.length-1]);
//                Long toDayTime= DateUtil.zero(new Date().getTime());
//                //System.out.println("plan-ok:"+pd.getOrderNo()+" "+toDayTime+"  "+lastDayTime);
//                if (lastDayTime.equals(toDayTime)){
//                    plan.setState("6");
//                }
//
//                planService.update(plan);
//
//
//                String[] payOrders=pd.getRepaymentOrderReality().split(",");
//                for (int i = 0; i < payOrders.length; i++){
//                    PlanDetailEntity p=planDetailService.findByPrimaryKey(payOrders[i]);
//                    p.setRepaymentState(PlanConstant.SUCCESS);
//                    planDetailService.update(p);
//                }
//                if("3".equals(jobs.getString("remitStatus"))){
//                    pd.setState(PlanConstant.FINISH);//执行状态完成
//                    pd.setRepaymentState(PlanConstant.FAIL);
//                    pd.setFinishTime(new Date().getTime());
//                    planDetailService.update(pd);
//                }
//            }
//        }
//            return "";
//    }
//
//    public static void main(String[] args) {
//        String s = "";
//        String[] t = s.split(",");
//        System.out.println(t.length);
//        for(int i = 0; i < t.length; i++){
//            HashMap<String, Object> reqMap = new HashMap<>();
//            //reqMap.put("timestamp", System.currentTimeMillis());
//            reqMap.put("orderNo", t[i]);
//            //reqMap.put("orderNo2", t[i+1]);
//            reqMap.put("pwd", "yjkj123");
//            System.out.println(HttpClientUtil.doPost("http://localhost:1173/test/test", reqMap));
//        }
//    }
}
