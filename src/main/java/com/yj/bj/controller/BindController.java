package com.yj.bj.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yj.bj.constant.BankCode;
import com.yj.bj.constant.EasyConstant;
import com.yj.bj.constant.YJConstant;
import com.yj.bj.entity.BindEntity;
import com.yj.bj.entity.InstitutionEntity;
import com.yj.bj.entity.user.CardInformation;
import com.yj.bj.entity.user.MerChants;
import com.yj.bj.service.plan.BindService;
import com.yj.bj.service.plan.InstitutionService;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yj.bj.constant.EasyConstant.key;
import static com.yj.bj.util.EncryptUtil.md5Encrypt;

/**
 * Created by bin on 2018/4/8.
 */
@RestController
@RequestMapping("/bind/")
public class BindController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CallbackController.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    private BindService bindService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private MerChantsService merChantsService;
    @Autowired
    private InstitutionService institutionService;

    @RequestMapping("bind")
    public YJResult binds(String merchantId,//商户号
                          String aisleCode,//通道标识
                          String tokens,
                          String institutionId) throws Exception { //机构号
        InstitutionEntity in = institutionService.findByPrimaryKey(institutionId); //机构
        MerChants mer = merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());//商户
        if(mer == null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        List<CardInformation> card = merChantsService.getCardList(mer.getMerChantId(),"SC",tokens,in.getMerHost());
        if(card.size() == 0){
            return YJResult.build(YJConstant.CARD_NULL_CODE, YJConstant.CARD_NULL_MSG,null);
        }
        String orderNo = OrderUtil.createOrderNo(idWorker,"T");//创建订单
        String keyMD5 = EncryptUtil.md5Encrypt(EasyConstant.key);//生产加密key
        //加密数据
        String merNameCipher = EncryptUtil.desEncrypt(mer.getMerName(),keyMD5);
        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(),keyMD5);

        String url = EasyConstant.host+"/bind/bind";
        //创建接口参数map
        HashMap<String,Object> map = Maps.newHashMap();
        map.put("institutionId",EasyConstant.spCode);
        map.put("aisleCode",EasyConstant.channelCode);
        map.put("orderNo",orderNo);
        map.put("outMerId",merchantId);
        map.put("merNameCipher",merNameCipher);
        map.put("idCardNoCipher",idCardNoCipher);
        map.put("debitCardNo",card.get(0).getCardNumber());
        map.put("debitCardPhone",card.get(0).getMerMp());
        map.put("debitBankCode",card.get(0).getIssuingBank());
        map.put("rate",mer.getMerChantFee());
        map.put("d0Fee","100");
        map.put("sign",SignUtil.createYJSign(map));
        String result = HttpClientUtil.doPost(url,map); //调接口
        JSONObject job = JSONObject.parseObject(result);
        if("0000".equals(job.getString("code"))){//调用成功
            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
            BindEntity bindEntity = new BindEntity();
            bindEntity.setOrderNo(orderNo);
            bindEntity.setState(job.getString("code"));
            bindEntity.setMerchantId(merchantId);
            bindEntity.setAgentId(mer.getAgentId());
            bindEntity.setInstitutionId(mer.getInstitutionId());
            bindEntity.setAppId(bindEntity.getAppId());
            bindEntity.setCardNo(card.get(0).getCardNumber());
            bindEntity.setName(mer.getMerName());
            bindEntity.setPhone(card.get(0).getMerMp());
            bindEntity.setIdCardNo(mer.getCertNo());
            bindEntity.setAisleMerId(jobs.getString("aisleMerId"));
            bindService.save(bindEntity);
            return YJResult.ok(jobs.getString("aisleMerId"));
        }
        BindEntity bindEntity = new BindEntity();
        bindEntity.setOrderNo(orderNo);
        bindEntity.setState(job.getString("code"));
        bindEntity.setMerchantId(merchantId);
        bindEntity.setAgentId(mer.getAgentId());
        bindEntity.setInstitutionId(mer.getInstitutionId());
        bindEntity.setAppId(bindEntity.getAppId());
        bindEntity.setCardNo(card.get(0).getCardNumber());
        bindEntity.setName(mer.getMerName());
        bindEntity.setPhone(card.get(0).getMerMp());
        bindEntity.setIdCardNo(mer.getCertNo());
        bindEntity.setRemark(job.getString("msg"));
        bindService.save(bindEntity);
        return YJResult.build(job.getString("code"),job.getString("msg"));
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(EasyConstant.key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, EasyConstant.key);
//
//        // 敏感数据3DES加密
//        String bankAccountNoCipher = EncryptUtil.desEncrypt(card.get(0).getCardNumber(), EasyConstant.key);
//        String mobileCipher = EncryptUtil.desEncrypt(card.get(0).getMerMp(), EasyConstant.key);
//        String idCardNoCipher = EncryptUtil.desEncrypt(mer.getCertNo(), EasyConstant.key);
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("channelCode", EasyConstant.channelCode);
//        signParams.put("merName", mer.getMerName());
//        signParams.put("merAbbr", mer.getMerName());
//        signParams.put("idCardNo", mer.getCertNo());
//        signParams.put("bankAccountNo", card.get(0).getCardNumber());
//        signParams.put("mobile", card.get(0).getMerMp());
//        signParams.put("bankAccountName", mer.getMerName());
//        signParams.put("bankAccountType", "2");
//        signParams.put("bankName", BankCode.getName(card.get(0).getIssuingBank()));
//        signParams.put("bankSubName", BankCode.getName(card.get(0).getIssuingBank()));
//        signParams.put("bankCode", BankCode.getEasyCode(card.get(0).getIssuingBank()));
//        signParams.put("bankAbbr", BankCode.getCjCode(card.get(0).getIssuingBank()));
//        signParams.put("bankChannelNo", "000000000000");
//        signParams.put("bankProvince", "北京市");
//        signParams.put("bankCity", "北京市");
//        signParams.put("debitRate", mer.getMerChantFee());
//        signParams.put("debitCapAmount", "99999999");
//        signParams.put("creditRate", mer.getMerChantFee());
//        signParams.put("creditCapAmount", "99999999");
//        signParams.put("withdrawDepositRate", "0");
//        signParams.put("withdrawDepositSingleFee", "100");
//        signParams.put("reqFlowNo", orderNo);
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("channelCode", EasyConstant.channelCode);
//        jsonObj.put("merName", mer.getMerName());
//        jsonObj.put("merAbbr", mer.getMerName());
//        jsonObj.put("idCardNo", idCardNoCipher);
//        jsonObj.put("bankAccountNo", bankAccountNoCipher);
//        jsonObj.put("mobile", mobileCipher);
//        jsonObj.put("bankAccountName", mer.getMerName());
//        jsonObj.put("bankAccountType", "2");
//        jsonObj.put("bankName", BankCode.getName(card.get(0).getIssuingBank()));
//        jsonObj.put("bankSubName", BankCode.getName(card.get(0).getIssuingBank()));
//        jsonObj.put("bankCode", BankCode.getEasyCode(card.get(0).getIssuingBank()));
//        jsonObj.put("bankAbbr", BankCode.getCjCode(card.get(0).getIssuingBank()));
//        jsonObj.put("bankChannelNo", "000000000000");
//        jsonObj.put("bankProvince", "北京市");
//        jsonObj.put("bankCity", "北京市");
//        jsonObj.put("debitRate", mer.getMerChantFee());
//        jsonObj.put("debitCapAmount", "99999999");
//        jsonObj.put("creditRate", mer.getMerChantFee());
//        jsonObj.put("creditCapAmount", "99999999");
//        jsonObj.put("withdrawDepositRate", "0");
//        jsonObj.put("withdrawDepositSingleFee", "100");
//        jsonObj.put("reqFlowNo", orderNo);
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/merchant/merchantReg", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            JSONObject job = JSONObject.parseObject(jsonRsp);
//            JSONObject jobs = JSONObject.parseObject(job.getString("data"));
//            if("000000".equals(job.getString("code"))){
//                BindEntity bindEntity = new BindEntity();
//                bindEntity.setOrderNo(orderNo);
//                bindEntity.setState(job.getString("code"));
//                bindEntity.setMerchantId(merchantId);
//                bindEntity.setAgentId(mer.getAgentId());
//                bindEntity.setInstitutionId(mer.getInstitutionId());
//                bindEntity.setAppId(bindEntity.getAppId());
//                bindEntity.setCardNo(card.get(0).getCardNumber());
//                bindEntity.setName(mer.getMerName());
//                bindEntity.setPhone(card.get(0).getMerMp());
//                bindEntity.setIdCardNo(mer.getCertNo());
//                bindEntity.setAisleMerId(jobs.getString("merchantCode"));
//                bindService.save(bindEntity);
//                return YJResult.ok(jobs.getString("merchantCode"));
//            }
//            BindEntity bindEntity = new BindEntity();
//            bindEntity.setOrderNo(orderNo);
//            bindEntity.setState(job.getString("code"));
//            bindEntity.setMerchantId(merchantId);
//            bindEntity.setAgentId(mer.getAgentId());
//            bindEntity.setInstitutionId(mer.getInstitutionId());
//            bindEntity.setAppId(bindEntity.getAppId());
//            bindEntity.setCardNo(card.get(0).getCardNumber());
//            bindEntity.setName(mer.getMerName());
//            bindEntity.setPhone(card.get(0).getMerMp());
//            bindEntity.setIdCardNo(mer.getCertNo());
//            bindEntity.setRemark(job.getString("message"));
//            bindService.save(bindEntity);
//            return YJResult.build(job.getString("code"),job.getString("message"));
//        }
    }

    @RequestMapping("select")
    public YJResult selects(String merchantId) {
        BindEntity bindEntity = new BindEntity();
        bindEntity.setState("0000");
        bindEntity.setMerchantId(merchantId);
        BindEntity bind = bindService.findByObject(bindEntity);
        if(bind == null){
            return YJResult.build("0001", "商户未开户");
        }
        return YJResult.ok(bind);
    }
}
