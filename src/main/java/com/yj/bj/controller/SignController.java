package com.yj.bj.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.internal.SignUtils;
import com.google.common.collect.Maps;
import com.yj.bj.constant.EasyConstant;
import com.yj.bj.constant.YJConstant;
import com.yj.bj.entity.InstitutionEntity;
import com.yj.bj.entity.SignEntity;
import com.yj.bj.entity.user.MerChants;
import com.yj.bj.service.plan.BindService;
import com.yj.bj.service.plan.InstitutionService;
import com.yj.bj.service.plan.SignService;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bin on 2018/4/8.
 */
@RestController
@RequestMapping("/sign/")
public class SignController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SignController.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    private SignService signService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private MerChantsService merChantsService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private BindService bindService;

    @RequestMapping("send")
    public YJResult sends(String merchantId,
                          String aisleCode,
                          String aisleMerId,
                          String cardNo,
                          String phone,
                          String institutionId) throws Exception {
        InstitutionEntity in = institutionService.findByPrimaryKey(institutionId);
        MerChants mer = merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer == null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        String orderNo = OrderUtil.createOrderNo(idWorker,"T");
        String keyMD5 = EncryptUtil.md5Encrypt(EasyConstant.key);
        String merNameCipher = EncryptUtil.desEncrypt(mer.getMerName(),keyMD5);
        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(),keyMD5);

        String url = EasyConstant.host+"/sign/signSMS";
        HashMap<String,Object> map = Maps.newHashMap();
        map.put("institutionId",EasyConstant.spCode);
        map.put("aisleCode",EasyConstant.channelCode);
        map.put("orderNo",orderNo);
        map.put("aisleMerId",aisleMerId);
        map.put("outMerId",merchantId);
        map.put("merNameCipher",merNameCipher);
        map.put("idCardNoCipher",idCardNoCipher);
        map.put("creditCardNo",cardNo);
        map.put("creditCardCode","");
        map.put("creditCardPhone",phone);
        map.put("cv2Cipher","");
        map.put("yearCipher","");
        map.put("monthCipher","");
        map.put("sign",SignUtil.createYJSign(map));
        String result = HttpClientUtil.doPost(url,map);
        JSONObject job = JSONObject.parseObject(result);
        if("0000".equals(job.getString("code"))){
            SignEntity signEntity = new SignEntity();
            signEntity.setOrderNo(orderNo);
            signEntity.setAisleMerId(aisleMerId);
            signEntity.setInstitutionId(mer.getInstitutionId());
            signEntity.setAgentId(mer.getAgentId());
            signEntity.setAppId(mer.getAppId());
            signEntity.setMerchantId(merchantId);
            signEntity.setName(mer.getMerName());
            signEntity.setIdCard(mer.getCertNo());
            signEntity.setCreditCardNumber(cardNo);
            signEntity.setCreditPhone(phone);
            signService.save(signEntity);
            return YJResult.ok(orderNo);
        }
        SignEntity signEntity = new SignEntity();
        signEntity.setOrderNo(orderNo);
        signEntity.setAisleMerId(aisleMerId);
        signEntity.setInstitutionId(mer.getInstitutionId());
        signEntity.setAgentId(mer.getAgentId());
        signEntity.setAppId(mer.getAppId());
        signEntity.setMerchantId(merchantId);
        signEntity.setName(mer.getMerName());
        signEntity.setIdCard(mer.getCertNo());
        signEntity.setCreditCardNumber(cardNo);
        signEntity.setCreditPhone(phone);
        signEntity.setRemarks(job.getString("msg"));
        signService.save(signEntity);
        return YJResult.build(job.getString("code"),job.getString("msg"));
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//        // 敏感数据3DES加密
//        String bankAccountNoCipher = EncryptUtil.desEncrypt(cardNo, EasyConstant.key);
//        String mobileCipher = EncryptUtil.desEncrypt(phone, EasyConstant.key);
//        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(), EasyConstant.key);
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("orderNo", orderNo);
//        signParams.put("merchantCode", aisleMerId);
//        signParams.put("channelCode", EasyConstant.channelCode);
//        signParams.put("bankAccountName", mer.getMerName());
//        signParams.put("bankAccountNo", cardNo);
//        signParams.put("idCardNo", mer.getCertNo());
//        signParams.put("mobile", phone);
//
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("orderNo", orderNo);
//        jsonObj.put("channelCode", EasyConstant.channelCode);
//        jsonObj.put("merchantCode", aisleMerId);
//        jsonObj.put("bankAccountName", mer.getMerName());
//        jsonObj.put("bankAccountNo", bankAccountNoCipher);
//        jsonObj.put("idCardNo", idCardNoCipher);
//        jsonObj.put("mobile", mobileCipher);
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/sign/merchantSignSms", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            JSONObject job = JSONObject.parseObject(jsonRsp);
//            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//            if("000000".equals(job.getString("code"))){
//                SignEntity signEntity = new SignEntity();
//                signEntity.setOrderNo(orderNo);
//                signEntity.setAisleMerId(aisleMerId);
//                signEntity.setSignState(jobs.getString("signStatus"));
//                signEntity.setInstitutionId(mer.getInstitutionId());
//                signEntity.setAgentId(mer.getAgentId());
//                signEntity.setAppId(mer.getAppId());
//                signEntity.setMerchantId(merchantId);
//                signEntity.setName(mer.getMerName());
//                signEntity.setIdCard(mer.getCertNo());
//                signEntity.setCreditCardNumber(cardNo);
//                signEntity.setCreditPhone(phone);
//                signService.save(signEntity);
//                return YJResult.ok(orderNo);
//            }
//            SignEntity signEntity = new SignEntity();
//            signEntity.setOrderNo(orderNo);
//            signEntity.setAisleMerId(aisleMerId);
//            signEntity.setInstitutionId(mer.getInstitutionId());
//            signEntity.setAgentId(mer.getAgentId());
//            signEntity.setAppId(mer.getAppId());
//            signEntity.setMerchantId(merchantId);
//            signEntity.setName(mer.getMerName());
//            signEntity.setIdCard(mer.getCertNo());
//            signEntity.setCreditCardNumber(cardNo);
//            signEntity.setCreditPhone(phone);
//            signEntity.setRemarks(job.getString("message"));
//            signService.save(signEntity);
//            return YJResult.build(job.getString("code"),job.getString("message"));
//        }
//        return YJResult.build(String.valueOf(response.code()),response.message());
    }

    @RequestMapping("agreement")
    public YJResult agreements(String merchantId,
                               String aisleCode,
                               String orderNo,
                               String month,
                               String year,
                               String cvv2,
                               String smsCode,
                               String institutionId) throws Exception {
        InstitutionEntity in = institutionService.findByPrimaryKey(institutionId);
        MerChants mer = merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer == null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        SignEntity signEntity = signService.findByPrimaryKey(orderNo);
        String keyMD5 = EncryptUtil.md5Encrypt(EasyConstant.key);

        String merNameCipher = EncryptUtil.desEncrypt(mer.getMerName(),keyMD5);
        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(),keyMD5);

        String cv2Cipher = EncryptUtil.desEncrypt(cvv2,keyMD5);
        String yearCipher = EncryptUtil.desEncrypt(year,keyMD5);
        String monthCipher = EncryptUtil.desEncrypt(month,keyMD5);
        String url = EasyConstant.host+"/sign/sign";
        HashMap<String,Object> map = Maps.newHashMap();
        map.put("institutionId",EasyConstant.spCode);
        map.put("aisleCode",EasyConstant.channelCode);
        map.put("orderNo",orderNo);
        map.put("smsCode",smsCode);
        map.put("aisleMerId",signEntity.getAisleMerId());
        map.put("merNameCipher",merNameCipher);
        map.put("idCardNoCipher",idCardNoCipher);
        map.put("creditCardNo",signEntity.getCreditCardNumber());
        map.put("creditCardCode","");
        map.put("creditCardPhone",signEntity.getCreditPhone());
        map.put("cv2Cipher",cv2Cipher);
        map.put("yearCipher",yearCipher);
        map.put("monthCipher",monthCipher);
        map.put("sign",SignUtil.createYJSign(map));
        String result = HttpClientUtil.doPost(url,map);
        //{{"code":"0000","msg":"处理成功","data":{"aisleState":"0000","sysState":"0000","signId":"201908271502011042"}}
        JSONObject job = JSONObject.parseObject(result);
        if("0000".equals(job.getString("code"))){
            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
            signEntity.setSignId(jobs.getString("signId"));
            signEntity.setSignState(jobs.getString("sysState"));
            signEntity.setCardValidDate(year + month);
            signEntity.setCv2(cvv2);
            signService.update(signEntity);
            return YJResult.ok();
        }
        signEntity.setSignState(job.getString("code"));
        signEntity.setCardValidDate(year + month);
        signEntity.setCv2(cvv2);
        signEntity.setRemarks(job.getString("msg"));
        signService.update(signEntity);
        return YJResult.build(job.getString("code"),job.getString("msg"));
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//        // 敏感数据3DES加密
//        String bankAccountNoCipher = EncryptUtil.desEncrypt(signEntity.getCreditCardNumber(), EasyConstant.key);
//        String mobileCipher = EncryptUtil.desEncrypt(signEntity.getCreditPhone(), EasyConstant.key);
//        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(), EasyConstant.key);
//        String cvn2Cipher = EncryptUtil.desEncrypt(cvv2, EasyConstant.key);
//        String expiredCipher = EncryptUtil.desEncrypt(year + month, EasyConstant.key);
//
//
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("orderNo", orderNo);
//        signParams.put("channelCode", EasyConstant.channelCode);
//        signParams.put("merchantCode", signEntity.getAisleMerId());
//        signParams.put("bankAccountName", mer.getMerName());
//        signParams.put("bankAccountNo", signEntity.getCreditCardNumber());
//        signParams.put("idCardNo", mer.getCertNo());
//        signParams.put("mobile", signEntity.getCreditPhone());
//        signParams.put("cvn2", cvv2);
//        signParams.put("expired", year + month);
//        signParams.put("smsCode", smsCode);
//        signParams.put("isNeedSms", "1");
//
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("orderNo", orderNo);
//        jsonObj.put("channelCode", EasyConstant.channelCode);
//        jsonObj.put("merchantCode", signEntity.getAisleMerId());
//        jsonObj.put("bankAccountName", mer.getMerName());
//        jsonObj.put("bankAccountNo", bankAccountNoCipher);
//        jsonObj.put("idCardNo", idCardNoCipher);
//        jsonObj.put("mobile", mobileCipher);
//        jsonObj.put("cvn2", cvn2Cipher);
//        jsonObj.put("expired", expiredCipher);
//        jsonObj.put("smsCode", smsCode);
//        jsonObj.put("isNeedSms", "1");
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/sign/merchantSign", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            JSONObject job = JSONObject.parseObject(jsonRsp);
//            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//            if("000000".equals(job.getString("code"))){
//                signEntity.setSignState(jobs.getString("signStatus"));
//                signEntity.setCardValidDate(year + month);
//                signEntity.setCv2(cvv2);
//                signService.update(signEntity);
//                return YJResult.ok();
//            }
//            signEntity.setSignState(jobs.getString("signStatus"));
//            signEntity.setCardValidDate(year + month);
//            signEntity.setCv2(cvv2);
//            signEntity.setRemarks(job.getString("message"));
//            signService.update(signEntity);
//            return YJResult.build(job.getString("code"),job.getString("message"));
//        }
//        return YJResult.build(String.valueOf(response.code()),response.message());
    }

    @RequestMapping("select")
    public YJResult selects(String merchantId, String cardNo) {
        SignEntity signEntity = new SignEntity();
        signEntity.setMerchantId(merchantId);
        signEntity.setCreditCardNumber(cardNo);
        signEntity.setSignState("0000");
        SignEntity sign = signService.findByObject(signEntity);
        if(sign == null){
            return YJResult.build("0001", "商户未激活");
        }
        return YJResult.ok(sign);
    }

    @RequestMapping("remove")
    public YJResult remove(String merchantId, String cardNo) {
        SignEntity signEntity = new SignEntity();
        signEntity.setMerchantId(merchantId);
        signEntity.setCreditCardNumber(cardNo);
        signEntity.setSignState("0000");
        SignEntity sign = signService.findByObject(signEntity);
        if(sign == null){
            return YJResult.build("0001", "商户未激活");
        }
        sign.setSignState("");
        signService.update(sign);
        return YJResult.ok();
    }
}
