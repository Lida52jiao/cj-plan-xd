package com.yj.bj.controller;

import com.alibaba.fastjson.JSONObject;
import com.yj.bj.entity.BindEntity;
import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.service.plan.BindService;
import com.yj.bj.service.plan.PlanDetailService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import static com.yj.bj.constant.EasyConstant.key;
import static com.yj.bj.constant.EasyConstant.spCode;

/**
 * Created by gl on 2019/1/29.
 */
@RestController
@RequestMapping("/select/")
public class QkWalletQuery {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QkWalletQuery.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    private BindService bindService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private PlanDetailService planDetailService;

//    @RequestMapping("select")
//    public String selects(String orderNo) throws Exception {
//        String url = Constants.getServerUrl() + "/v2/wallet/walletQuery"; // 接口地址
//        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
//        BindEntity bindEntity = new BindEntity();
//        bindEntity.setState("000000");
//        bindEntity.setMerchantId(pd.getMerchantId());
//        BindEntity bind = bindService.findByObject(bindEntity);
//        if(bind == null){
//            return "商户未开户";
//        }
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(key, spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, key);
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", spCode);
//        signParams.put("merchantCode", bind.getAisleMerId());
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", spCode);
//        jsonObj.put("merchantCode", bind.getAisleMerId());
//        jsonObj.put("sign", SignUtil.signByMap(key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(url, jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            return jsonRsp;
//        } else {
//            logger.info(sdf.format(new Date()) + "响应码: " + response.code());
//            //System.out.println(sdf.format(new Date()) + "响应码: " + response.code());
//            throw new IOException("Unexpected code " + response.message());
//        }
//    }
//
//    @RequestMapping("withdrawal")
//    public String withdrawal(String orderNo, String cardNo, String amounts, String type) throws Exception {
//        String url = Constants.getServerUrl() + "/v2/trans/withdraw"; // 接口地址
//        PlanDetailEntity pd=planDetailService.findByPrimaryKey(orderNo);
//        BindEntity bindEntity = new BindEntity();
//        bindEntity.setState("000000");
//        bindEntity.setMerchantId(pd.getMerchantId());
//        BindEntity bind = bindService.findByObject(bindEntity);
//        if(bind == null){
//            return "商户未开户";
//        }
//        String reqFlowNo = System.currentTimeMillis()+""; 		// 请求流水号
//        String merchantCode = bind.getAisleMerId();		// 商户编号
//        String amount = amounts; 				// 提现金额
//        String bankAccountName = pd.getName(); 		// 银行卡户名
//        String bankAccountNo = cardNo;	// 银行卡号，3DES加密
//        String idCardNo = pd.getIdCardNo();// 身份证号，3DES加密
//        String walletType = type;				// 钱包类型，402、快捷支付T1钱包 400、快捷支付D0钱包
//        String bankName = "中国银行";			// 银行名称
//        String bankSubName = "中国银行海淀支行";	// 支行名称
//        String bankChannelNo = "010200000000";	// 联行号
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(key, spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, key);
//
//        // 敏感数据3DES加密
//        String bankAccountNoCipher = EncryptUtil.desEncrypt(bankAccountNo, key);
//
//
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", spCode);
//        signParams.put("reqFlowNo", reqFlowNo);
//        signParams.put("merchantCode", merchantCode);
//        signParams.put("walletType", walletType);
//        signParams.put("amount", amount);
//        signParams.put("bankAccountName", bankAccountName);
//        signParams.put("bankAccountNo", bankAccountNo);
//        signParams.put("bankName", bankName);
//        signParams.put("bankSubName", bankSubName);
//        signParams.put("bankChannelNo", bankChannelNo);
//
//
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", spCode);
//        jsonObj.put("reqFlowNo", reqFlowNo);
//        jsonObj.put("merchantCode", merchantCode);
//        jsonObj.put("walletType", walletType);
//        jsonObj.put("amount", amount);
//        jsonObj.put("bankAccountName", bankAccountName);
//        jsonObj.put("bankAccountNo", bankAccountNoCipher);
//        jsonObj.put("bankName", bankName);
//        jsonObj.put("bankSubName", bankSubName);
//        jsonObj.put("bankChannelNo", bankChannelNo);
//        jsonObj.put("sign", SignUtil.signByMap(key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(url, jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //{"code":"000000","message":"SUCCESS","data":null}
//            return jsonRsp;
//        } else {
//            logger.info(sdf.format(new Date()) + "响应码: " + response.code());
//            //System.out.println(sdf.format(new Date()) + "响应码: " + response.code());
//            throw new IOException("Unexpected code " + response.message());
//        }
//    }
}
