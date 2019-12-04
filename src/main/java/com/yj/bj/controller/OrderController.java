package com.yj.bj.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yj.bj.constant.EasyConstant;
import com.yj.bj.constant.PlanConstant;
import com.yj.bj.entity.BindEntity;
import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.entity.PlanEntity;
import com.yj.bj.service.plan.BindService;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.service.plan.PlanService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bin on 2018/5/15.
 */
@RestController
@RequestMapping("/order/")
public class OrderController {//订单
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BindService bindService;

    @RequestMapping("/findState")
    public YJResult findState(String orderNo) throws Exception {
        PlanDetailEntity pd = planDetailService.findByPrimaryKey(orderNo);//根据订单号查计划详情
        BindEntity bindEntity = new BindEntity();
        bindEntity.setState("0000");
        bindEntity.setMerchantId(pd.getMerchantId());
        BindEntity bind = bindService.findByObject(bindEntity);  //查开户
        if(bind == null){
            return YJResult.build("0001", "商户未开户");
        }
        if(pd.getExecuteTime()+10*60*1000L>new Date().getTime()){
            return YJResult.build("9582","请在订单发起10分钟后尝试");
        }
        String url = EasyConstant.host+"/order/findState";
        //构建参数
        HashMap<String,Object> map = Maps.newHashMap();
        map.put("institutionId",EasyConstant.spCode);
        map.put("aisleCode",EasyConstant.channelCode);
        map.put("payType",pd.getPayType());
        map.put("orderNo",orderNo);
        map.put("aisleMerId",bind.getAisleMerId());
        map.put("executeTime",pd.getExecuteTime());
        map.put("sign",SignUtil.createYJSign(map));
        String result = HttpClientUtil.doPost(url,map);//调接口
        JSONObject job = JSONObject.parseObject(result);
        if("0000".equals(job.getString("code"))){//调成功
            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
            if("2".equals(pd.getPayType())){//1还款2消费
                if("0000".equals(jobs.getString("sysState"))){//消费成功
                    pd.setState(PlanConstant.FINISH);//"6"完成
                    pd.setFinishTime(new Date().getTime());
                    pd.setPayState(PlanConstant.SUCCESS);//"3"成功
                    pd.setCause(job.getString("msg"));//结果消息
                    pd.setRemarks("补单-"+job.getString("msg"));
                    planDetailService.update(pd);//更新状态
                }
                if("4".startsWith(jobs.getString("sysState"))){//失败
                    pd.setState(PlanConstant.FINISH);//"6"完成
                    pd.setFinishTime(new Date().getTime());
                    pd.setPayState(PlanConstant.FAIL);//"4"失败
                    pd.setCause(job.getString("msg"));
                    pd.setRemarks("补单-"+job.getString("msg"));
                    planDetailService.update(pd);//更新状态
                }
                return YJResult.build(job.getString("code"),job.getString("msg"));
            }else{//1还款
                if("0000".equals(jobs.getString("sysState"))){//成功
                    pd.setState(PlanConstant.FINISH);//"6"完成
                    pd.setFinishTime(new Date().getTime());
                    pd.setRepaymentState(PlanConstant.SUCCESS);//"3"成功
                    planDetailService.update(pd);//更新计划详情
                    PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                    planDetailEntity.setPlanId(pd.getPlanId());
                    planDetailEntity.setPayType("1");
                    planDetailEntity.setRepaymentState(PlanConstant.SUCCESS);//还款状态"3"成功
                    List<PlanDetailEntity> list = planDetailService.queryObjectForList(planDetailEntity);//获取订单详情list
                    Long amount = 0L;
                    for(PlanDetailEntity plan : list){
                        amount += plan.getArrivalAmount();//到账金额+=总金额
                    }
                    PlanEntity planEntity = planService.findByPrimaryKey(pd.getPlanId());
                    planEntity.setAlreadyAmount(amount);//已还金额
                    planService.update(planEntity);
                }
                if("4".startsWith(jobs.getString("sysState"))){
                    pd.setState(PlanConstant.FINISH);//"6"完成
                    pd.setFinishTime(new Date().getTime());
                    pd.setRepaymentState(PlanConstant.FAIL);//还款状态"4"失败
                    planDetailService.update(pd);
                }
            }
        }
        return YJResult.build(job.getString("code"),job.getString("msg"));
//        if("2".equals(pd.getPayType())){
//            // 获取令牌
//            BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//            String token = tokenRes.getData().getToken();
//            // 解密令牌
//            String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//            // 构建签名参数
//            TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//            signParams.put("token", tokenClearText);
//            signParams.put("spCode", EasyConstant.spCode);
//            signParams.put("orderNo", orderNo);
//            signParams.put("merchantCode", bind.getAisleMerId());
//
//
//            // 构建请求参数
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("token", tokenClearText);
//            jsonObj.put("spCode", EasyConstant.spCode);
//            jsonObj.put("orderNo", orderNo);
//            jsonObj.put("merchantCode", bind.getAisleMerId());
//            jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//            // 接口访问
//            String jsonReq = jsonObj.toJSONString();
//            System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//            Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/trans/consumeQuery", jsonReq);
//            if (response.isSuccessful()) {
//                String jsonRsp = response.body().string();
//                System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//                JSONObject job = JSONObject.parseObject(jsonRsp);
//                JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//                boolean isSuccess="2".equals(jobs.getString("orderStatus"));
//                if (isSuccess) {
//                    pd.setState(PlanConstant.FINISH);
//                    pd.setFinishTime(new Date().getTime());
//                    pd.setPayState(PlanConstant.SUCCESS);
//                    pd.setCause(jobs.getString("respMsg"));
//                    pd.setRemarks("补单-"+jobs.getString("respMsg"));
//                    planDetailService.update(pd);
//                }
//                if("5".equals(jobs.getString("orderStatus"))){
//                    pd.setState(PlanConstant.FINISH);
//                    pd.setFinishTime(new Date().getTime());
//                    pd.setPayState(PlanConstant.FAIL);
//                    pd.setCause(jobs.getString("respMsg"));
//                    pd.setRemarks("补单-"+jobs.getString("respMsg"));
//                    planDetailService.update(pd);
//                }
//                return YJResult.build(isSuccess?"0000":jobs.getString("respCode"),jobs.getString("respMsg"));
//            }
//            return YJResult.build(String.valueOf(response.code()),response.message());
//        }else{
//            // 获取令牌
//            BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//            String token = tokenRes.getData().getToken();
//            // 解密令牌
//            String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//            // 构建签名参数
//            TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//            signParams.put("token", tokenClearText);
//            signParams.put("spCode", EasyConstant.spCode);
//            signParams.put("reqFlowNo", orderNo);
//            signParams.put("merchantCode", bind.getAisleMerId());
//
//
//            // 构建请求参数
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("token", tokenClearText);
//            jsonObj.put("spCode", EasyConstant.spCode);
//            jsonObj.put("reqFlowNo", orderNo);
//            jsonObj.put("merchantCode", bind.getAisleMerId());
//            jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//            // 接口访问
//            String jsonReq = jsonObj.toJSONString();
//            System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//            Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/trans/withdrawQuery", jsonReq);
//            if (response.isSuccessful()) {
//                String jsonRsp = response.body().string();
//                System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//                JSONObject job = JSONObject.parseObject(jsonRsp);
//                JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//                boolean isSuccess="1".equals(jobs.getString("remitStatus"));
//                if (isSuccess) {
//                    pd.setState(PlanConstant.FINISH);
//                    pd.setFinishTime(new Date().getTime());
//                    pd.setRepaymentState(PlanConstant.SUCCESS);
//                    planDetailService.update(pd);
//                    PlanDetailEntity planDetailEntity = new PlanDetailEntity();
//                    planDetailEntity.setPlanId(pd.getPlanId());
//                    planDetailEntity.setPayType("1");
//                    planDetailEntity.setRepaymentState(PlanConstant.SUCCESS);
//                    List<PlanDetailEntity> list = planDetailService.queryObjectForList(planDetailEntity);
//                    Long amount = 0L;
//                    for(PlanDetailEntity plan : list){
//                        amount += plan.getArrivalAmount();
//                    }
//                    PlanEntity planEntity = planService.findByPrimaryKey(pd.getPlanId());
//                    planEntity.setAlreadyAmount(amount);
//                    planService.update(planEntity);
//                }
//                if("3".equals(jobs.getString("remitStatus"))){
//                    pd.setState(PlanConstant.FINISH);
//                    pd.setFinishTime(new Date().getTime());
//                    pd.setRepaymentState(PlanConstant.FAIL);
//                    planDetailService.update(pd);
//                }
//                return YJResult.build(isSuccess?"0000":"","");
//            }
//            return YJResult.build(String.valueOf(response.code()),response.message());
//        }
    }

    @RequestMapping("anewPay")
    public YJResult anewPay(Long timestamp, String orderNo, String pwd){
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
        if (!pwd.equals("yjkj123")){
            return YJResult.build("9458","系统异常");
        }
        if ("1".equals(pd.getIsAnew())){
            return YJResult.build("9158","已经补过");
        }
        if (!pd.getAisleCode().equals(PlanConstant.aisleCode)){
            return YJResult.build("6048","通道标识错误");
        }
        if (!pd.getPayType().equals("2")){
            return YJResult.build("0585","不是消费订单");
        }
        if (!pd.getPayState().equals("4")){
            return YJResult.build("1232","不是失败的消费订单");
        }
        if (timestamp==null||timestamp.equals(0L)){
            timestamp= DateUtil.zero(new Date().getTime())+20*60*60*1000L;
            PlanDetailEntity pd8=new PlanDetailEntity();
            pd8.setPlanId(pd.getPlanId());
            pd8.setMerchantId(pd.getMerchantId());
            pd8.setCardNo(pd.getCardNo());
            pd8.setExecuteTime(timestamp);
            pd8.setPayType(PlanConstant.TPYE_pay);
            List<PlanDetailEntity> pd8List=planDetailService.queryObjectForList(pd8);
            if (pd8List.size()>0){
                return YJResult.build("9492","8点有本人本卡订单，请勿重复补单");
            }
        }
        String cycleId= OrderUtil.createOrderNo(idWorker,"CB");
        String rOrderNo= OrderUtil.createOrderNo(idWorker,"RB");
        String payOrderNo= OrderUtil.createOrderNo(idWorker,"PB");

        PlanDetailEntity pdp=new PlanDetailEntity();
        //计划信息
        pdp.setPlanId(pd.getPlanId());
        pdp.setOrderNo(payOrderNo);
        pdp.setState(PlanConstant.EXECUTE);
        pdp.setPayState(PlanConstant.WAIT);
        pdp.setRepaymentState(PlanConstant.WAIT);
        pdp.setRepaymentOrderExpect(rOrderNo);
        pdp.setRepaymentOrderReality(rOrderNo);
        pdp.setDay(0);
        pdp.setTime(0);
        pdp.setNumber(0);
        pdp.setPayType("2");
        /*pdp.setAmount(pd.getAmount());
        pdp.setArrivalAmount(pd.getArrivalAmount());
        pdp.setFee(pd.getFee());
        pdp.setPayFee(pd.getPayFee());*/
        //18-01-09  消费到账金额+1
        Long aramount = pd.getArrivalAmount()+100L;
        Double xiaofei = (BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(aramount),new Double(1-new Double(pd.getRate()))),0) );
        Long payAmount=xiaofei.longValue();
        if (payAmount>=99999){
            pdp.setAmount(pd.getAmount());
            pdp.setArrivalAmount(pd.getArrivalAmount());
            pdp.setFee(pd.getFee());
            pdp.setPayFee(pd.getPayFee());
        }else {
            pdp.setAmount(payAmount);
            pdp.setArrivalAmount(aramount);
            pdp.setFee(payAmount-aramount);
            pdp.setPayFee(payAmount-aramount);
        }
        pdp.setRepaymentFee(0L);
        pdp.setExecuteTime(timestamp);
        //用户信息
        pdp.setMerchantId(pd.getMerchantId());
        pdp.setName(pd.getName());
        pdp.setPhone(pd.getPhone());
        //卡信息
        pdp.setBankCode(pd.getBankCode());
        pdp.setIdCardNo(pd.getIdCardNo());
        pdp.setCardNo(pd.getCardNo());
        pdp.setBindId(pd.getBindId());
        //标识
        pdp.setInstitutionId(pd.getInstitutionId());
        pdp.setAppId(pd.getAppId());
        pdp.setAgentId(pd.getAgentId());

        pdp.setIsLd("1");
        pdp.setCycleId(cycleId);
        pdp.setAisleCode(pd.getAisleCode());
        pdp.setProvince(pd.getProvince());
        pdp.setCity(pd.getCity());

        pdp.setMcc(pd.getMcc());

        pdp.setRate(pd.getRate());
        pdp.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdp);

        Long repaymentFee=100L;
        //还款
        PlanDetailEntity pdr=new PlanDetailEntity();
        pdr.setPlanId(pd.getPlanId());
        pdr.setOrderNo(rOrderNo);
        pdr.setState(PlanConstant.EXECUTE);
        pdr.setPayState(PlanConstant.WAIT);
        pdr.setRepaymentState(PlanConstant.WAIT);
        pdr.setRepaymentOrderExpect(payOrderNo);
        pdr.setRepaymentOrderReality("");
        pdr.setDay(0);
        pdr.setTime(0);
        pdr.setNumber(0);
        pdr.setPayType("1");
        pdr.setAmount(pd.getArrivalAmount());
        pdr.setArrivalAmount(pd.getArrivalAmount()-repaymentFee);
        pdr.setFee(repaymentFee);
        pdr.setPayFee(0L);
        pdr.setRepaymentFee(repaymentFee);
        pdr.setExecuteTime(timestamp+41*60*1000L);
        //用户信息
        pdr.setMerchantId(pd.getMerchantId());
        pdr.setName(pd.getName());
        pdr.setPhone(pd.getPhone());
        //卡信息
        pdr.setBankCode(pd.getBankCode());
        pdr.setIdCardNo(pd.getIdCardNo());
        pdr.setCardNo(pd.getCardNo());
        pdr.setBindId(pd.getBindId());
        //标识
        pdr.setInstitutionId(pd.getInstitutionId());
        pdr.setAppId(pd.getAppId());
        pdr.setAgentId(pd.getAgentId());

        pdr.setIsLd("1");
        pdr.setCycleId(cycleId);
        pdr.setAisleCode(pd.getAisleCode());
        pdr.setProvince(pd.getProvince());
        pdr.setCity(pd.getCity());
        pdr.setRate(pd.getRate());
        pdr.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdr);

        pd.setIsAnew("1");
        pd.setAnewOrderNo(payOrderNo);
        planDetailService.update(pd);
        return YJResult.ok();
    }


    @RequestMapping("anewPay2")
    public YJResult anewPay2(Long timestamp, String orderNo, String orderNo2, String pwd){
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
        PlanDetailEntity pd2=planDetailService.findByPrimaryKey(orderNo2);
        if (!pwd.equals("yjkj123")){
            return YJResult.build("9458","系统异常");
        }
        if ("1".equals(pd.getIsAnew())){
            return YJResult.build("9158","已经补过");
        }
        if ("1".equals(pd2.getIsAnew())){
            return YJResult.build("9158","已经补过");
        }
        if (!pd.getAisleCode().equals(PlanConstant.aisleCode)){
            return YJResult.build("6048","通道标识错误");
        }
        if (!pd.getPayType().equals("2")){
            return YJResult.build("0585","不是消费订单");
        }
        if (!pd.getPayState().equals("4")){
            return YJResult.build("1232","不是失败的消费订单");
        }
        if (!pd.getMerchantId().equals(pd2.getMerchantId())){
            return YJResult.build("1232","不是同一个人");
        }
        if (timestamp==null||timestamp.equals(0L)){
            timestamp= DateUtil.zero(new Date().getTime())+20*60*60*1000L;
            PlanDetailEntity pd8=new PlanDetailEntity();
            pd8.setPlanId(pd.getPlanId());
            pd8.setMerchantId(pd.getMerchantId());
            pd8.setCardNo(pd.getCardNo());
            pd8.setExecuteTime(timestamp);
            pd8.setPayType(PlanConstant.TPYE_pay);
            List<PlanDetailEntity> pd8List=planDetailService.queryObjectForList(pd8);
            if (pd8List.size()>0){
                return YJResult.build("9492","8点有本人本卡订单，请勿重复补单");
            }
        }
        String cycleId= OrderUtil.createOrderNo(idWorker,"CB");
        String rOrderNo= OrderUtil.createOrderNo(idWorker,"RB");
        String payOrderNo= OrderUtil.createOrderNo(idWorker,"PB");
        String payOrderNo2= OrderUtil.createOrderNo(idWorker,"PB");

        PlanDetailEntity pdp=new PlanDetailEntity();
        //计划信息
        pdp.setPlanId(pd.getPlanId());
        pdp.setOrderNo(payOrderNo);
        pdp.setState(PlanConstant.EXECUTE);
        pdp.setPayState(PlanConstant.WAIT);
        pdp.setRepaymentState(PlanConstant.WAIT);
        pdp.setRepaymentOrderExpect(rOrderNo);
        pdp.setRepaymentOrderReality(rOrderNo);
        pdp.setDay(0);
        pdp.setTime(0);
        pdp.setNumber(0);
        pdp.setPayType("2");
        /*pdp.setAmount(pd.getAmount());
        pdp.setArrivalAmount(pd.getArrivalAmount());
        pdp.setFee(pd.getFee());
        pdp.setPayFee(pd.getPayFee());*/
        //18-01-09  消费到账金额+1
        Long aramount = pd.getArrivalAmount()+100L;
        Double xiaofei = (BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(aramount),new Double(1-new Double(pd.getRate()))),0) );
        Long payAmount=xiaofei.longValue();
        if (payAmount>=99999){
            pdp.setAmount(pd.getAmount());
            pdp.setArrivalAmount(pd.getArrivalAmount());
            pdp.setFee(pd.getFee());
            pdp.setPayFee(pd.getPayFee());
        }else {
            pdp.setAmount(payAmount);
            pdp.setArrivalAmount(aramount);
            pdp.setFee(payAmount-aramount);
            pdp.setPayFee(payAmount-aramount);
        }
        pdp.setRepaymentFee(0L);
        pdp.setExecuteTime(timestamp);
        //用户信息
        pdp.setMerchantId(pd.getMerchantId());
        pdp.setName(pd.getName());
        pdp.setPhone(pd.getPhone());
        //卡信息
        pdp.setBankCode(pd.getBankCode());
        pdp.setIdCardNo(pd.getIdCardNo());
        pdp.setCardNo(pd.getCardNo());
        pdp.setBindId(pd.getBindId());
        //标识
        pdp.setInstitutionId(pd.getInstitutionId());
        pdp.setAppId(pd.getAppId());
        pdp.setAgentId(pd.getAgentId());

        pdp.setIsLd("1");
        pdp.setCycleId(cycleId);
        pdp.setAisleCode(pd.getAisleCode());
        pdp.setProvince(pd.getProvince());
        pdp.setCity(pd.getCity());

        pdp.setMcc(pd.getMcc());

        pdp.setRate(pd.getRate());
        pdp.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdp);


        PlanDetailEntity pdp2=new PlanDetailEntity();
        //计划信息
        pdp2.setPlanId(pd2.getPlanId());
        pdp2.setOrderNo(payOrderNo2);
        pdp2.setState(PlanConstant.EXECUTE);
        pdp2.setPayState(PlanConstant.WAIT);
        pdp2.setRepaymentState(PlanConstant.WAIT);
        pdp2.setRepaymentOrderExpect(rOrderNo);
        pdp2.setRepaymentOrderReality(rOrderNo);
        pdp2.setDay(0);
        pdp2.setTime(0);
        pdp2.setNumber(0);
        pdp2.setPayType("2");
        pdp2.setAmount(pd2.getAmount());
        pdp2.setArrivalAmount(pd2.getArrivalAmount());
        pdp2.setFee(pd2.getFee());
        pdp2.setPayFee(pd2.getPayFee());
        pdp2.setRepaymentFee(0L);
        pdp2.setExecuteTime(timestamp+41*60*1000L);
        //用户信息
        pdp2.setMerchantId(pd2.getMerchantId());
        pdp2.setName(pd2.getName());
        pdp2.setPhone(pd2.getPhone());
        //卡信息
        pdp2.setBankCode(pd2.getBankCode());
        pdp2.setIdCardNo(pd2.getIdCardNo());
        pdp2.setCardNo(pd2.getCardNo());
        pdp2.setBindId(pd2.getBindId());
        //标识
        pdp2.setInstitutionId(pd2.getInstitutionId());
        pdp2.setAppId(pd2.getAppId());
        pdp2.setAgentId(pd2.getAgentId());

        pdp2.setIsLd("1");
        pdp2.setCycleId(cycleId);
        pdp2.setAisleCode(pd2.getAisleCode());
        pdp2.setProvince(pd2.getProvince());
        pdp2.setCity(pd2.getCity());

        pdp2.setMcc(pd2.getMcc());

        pdp2.setRate(pd2.getRate());
        pdp2.setD0Fee(pd2.getD0Fee());

        planDetailService.save(pdp2);


        Long repaymentFee=100L;
        //还款
        PlanDetailEntity pdr=new PlanDetailEntity();
        pdr.setPlanId(pd.getPlanId());
        pdr.setOrderNo(rOrderNo);
        pdr.setState(PlanConstant.EXECUTE);
        pdr.setPayState(PlanConstant.WAIT);
        pdr.setRepaymentState(PlanConstant.WAIT);
        pdr.setRepaymentOrderExpect(payOrderNo+","+payOrderNo2);
        pdr.setRepaymentOrderReality("");
        pdr.setDay(0);
        pdr.setTime(0);
        pdr.setNumber(0);
        pdr.setPayType("1");
        pdr.setAmount(pd.getArrivalAmount()+pd2.getArrivalAmount());
        pdr.setArrivalAmount(pd.getArrivalAmount()+pd2.getArrivalAmount()-repaymentFee);
        pdr.setFee(repaymentFee);
        pdr.setPayFee(0L);
        pdr.setRepaymentFee(repaymentFee);
        pdr.setExecuteTime(timestamp+81*60*1000L);
        //用户信息
        pdr.setMerchantId(pd.getMerchantId());
        pdr.setName(pd.getName());
        pdr.setPhone(pd.getPhone());
        //卡信息
        pdr.setBankCode(pd.getBankCode());
        pdr.setIdCardNo(pd.getIdCardNo());
        pdr.setCardNo(pd.getCardNo());
        pdr.setBindId(pd.getBindId());
        //标识
        pdr.setInstitutionId(pd.getInstitutionId());
        pdr.setAppId(pd.getAppId());
        pdr.setAgentId(pd.getAgentId());

        pdr.setIsLd("1");
        pdr.setCycleId(cycleId);
        pdr.setAisleCode(pd.getAisleCode());
        pdr.setProvince(pd.getProvince());
        pdr.setCity(pd.getCity());
        pdr.setRate(pd.getRate());
        pdr.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdr);

        pd.setIsAnew("1");
        pd.setAnewOrderNo(payOrderNo);
        planDetailService.update(pd);

        pd2.setIsAnew("1");
        pd2.setAnewOrderNo(payOrderNo2);
        planDetailService.update(pd2);
        return YJResult.ok();
    }

    @RequestMapping("anewRepayment2")
    public YJResult anewRepayment2(Long timestamp, String orderNo, String orderNo2, String pwd){
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);

        if (!pwd.equals("yjkj123")){
            return YJResult.build("9458","系统异常");
        }
        if ("1".equals(pd.getIsAnew())){
            return YJResult.build("9158","已经补过");
        }
        if (!pd.getAisleCode().equals(PlanConstant.aisleCode)){
            return YJResult.build("6048","通道标识错误");
        }
        if (!pd.getPayType().equals("1")){
            return YJResult.build("0585","不是还款订单");
        }
        if (!pd.getRepaymentState().equals("4")){
            return YJResult.build("1232","不是失败的还款订单");
        }
        if (timestamp==null||timestamp.equals(0L)){
            timestamp=new Date().getTime();
        }


        String rOrderNo= OrderUtil.createOrderNo(idWorker,"RB");

        Long repaymentFee=100L;
        //还款
        PlanDetailEntity pdr=new PlanDetailEntity();

        pdr.setPlanId(pd.getPlanId());
        pdr.setOrderNo(rOrderNo);
        pdr.setState(PlanConstant.EXECUTE);
        pdr.setPayState(PlanConstant.WAIT);
        pdr.setRepaymentState(PlanConstant.WAIT);
        pdr.setRepaymentOrderExpect(pd.getRepaymentOrderExpect());
        pdr.setRepaymentOrderReality("");
        pdr.setDay(0);
        pdr.setTime(0);
        pdr.setNumber(0);
        pdr.setPayType("1");
        pdr.setAmount(pd.getAmount());
        pdr.setArrivalAmount(pd.getArrivalAmount());
        pdr.setFee(repaymentFee);
        pdr.setPayFee(0L);
        pdr.setRepaymentFee(repaymentFee);
        pdr.setExecuteTime(timestamp);
        //用户信息
        pdr.setMerchantId(pd.getMerchantId());
        pdr.setName(pd.getName());
        pdr.setPhone(pd.getPhone());
        //卡信息
        pdr.setBankCode(pd.getBankCode());
        pdr.setIdCardNo(pd.getIdCardNo());
        pdr.setCardNo(pd.getCardNo());
        pdr.setBindId(pd.getBindId());
        //标识
        pdr.setInstitutionId(pd.getInstitutionId());
        pdr.setAppId(pd.getAppId());
        pdr.setAgentId(pd.getAgentId());

        pdr.setIsLd("1");
        pdr.setCycleId(pd.getCycleId());
        pdr.setAisleCode(pd.getAisleCode());
        pdr.setProvince(pd.getProvince());
        pdr.setCity(pd.getCity());
        pdr.setRate(pd.getRate());
        pdr.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdr);

        pd.setIsAnew("1");
        pd.setAnewOrderNo(rOrderNo);
        planDetailService.update(pd);
        return YJResult.ok(pdr);
    }

    @RequestMapping("compelRepayment")
    public YJResult compelRepayment(String orderNo,String pwd){
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
        if (!pwd.equals("yjkj123")){
            return YJResult.build("9458","系统异常");
        }
        if ("1".equals(pd.getIsAnew())){
            return YJResult.build("9158","已经补过");
        }
        if (!pd.getAisleCode().equals(PlanConstant.aisleCode)){
            return YJResult.build("6048","通道标识错误");
        }
        if (!pd.getPayType().equals("2")){
            return YJResult.build("0585","不是消费订单");
        }
        PlanDetailEntity rpd=new PlanDetailEntity();
        rpd.setCycleId(pd.getCycleId());
        rpd.setPayType("1");
        rpd=planDetailService.findByObject(rpd);
        if(new Date().getTime()<rpd.getExecuteTime()+10*60*1000L){
            return YJResult.build("0586","请在正常还款10分钟后尝试");
        }

        String rOrderNo= OrderUtil.createOrderNo(idWorker,"RB");

        //保存订单
        Long repaymentFee=100L;
        //还款
        PlanDetailEntity pdr=new PlanDetailEntity();

        pdr.setPlanId(pd.getPlanId());
        pdr.setOrderNo(rOrderNo);
        pdr.setState(PlanConstant.EXECUTE);
        pdr.setPayState(PlanConstant.WAIT);
        pdr.setRepaymentState(PlanConstant.WAIT);
        pdr.setRepaymentOrderExpect(pd.getRepaymentOrderExpect());
        pdr.setRepaymentOrderReality("");
        pdr.setDay(0);
        pdr.setTime(0);
        pdr.setNumber(0);
        pdr.setPayType("1");
        pdr.setAmount(pd.getAmount());
        pdr.setArrivalAmount(pd.getArrivalAmount());
        pdr.setFee(repaymentFee);
        pdr.setPayFee(0L);
        pdr.setRepaymentFee(repaymentFee);
        pdr.setExecuteTime(new Date().getTime());
        //用户信息
        pdr.setMerchantId(pd.getMerchantId());
        pdr.setName(pd.getName());
        pdr.setPhone(pd.getPhone());
        //卡信息
        pdr.setBankCode(pd.getBankCode());
        pdr.setIdCardNo(pd.getIdCardNo());
        pdr.setCardNo(pd.getCardNo());
        pdr.setBindId(pd.getBindId());
        //标识
        pdr.setInstitutionId(pd.getInstitutionId());
        pdr.setAppId(pd.getAppId());
        pdr.setAgentId(pd.getAgentId());

        pdr.setIsLd("1");
        pdr.setCycleId(pd.getCycleId());
        pdr.setAisleCode(pd.getAisleCode());
        pdr.setProvince(pd.getProvince());
        pdr.setCity(pd.getCity());
        pdr.setRate(pd.getRate());
        pdr.setD0Fee(pd.getD0Fee());

        planDetailService.save(pdr);

        pd.setIsAnew("1");
        pd.setAnewOrderNo(rOrderNo);
        planDetailService.update(pd);

        /*//发起代付
        RepaymentResult rr=null;
        try {
            rr=xsService.repayment(pdr,pd.getOrderNo(),pd.getArrivalAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(rr));*/
        return YJResult.ok(pdr);
    }

    @RequestMapping("select")
    public YJResult selects(String orderNo, String remarks, String cause) {
        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
        pd.setRemarks(remarks);
        pd.setCause(cause);
        planDetailService.update(pd);
        return YJResult.ok();
    }
}
