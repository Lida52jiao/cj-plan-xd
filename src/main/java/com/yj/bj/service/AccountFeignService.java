package com.yj.bj.service;

import com.yj.bj.constant.UrlConstant;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by bin on 2018/3/27.
 */
@FeignClient(name = "account-server",url = UrlConstant.ACCOUNT_EASY_PAY)
@RequestMapping("/yj-account/")
public interface AccountFeignService {
    //易生交易分润
    @RequestMapping("callback/easyPayCallback")
    String easyPayCallback(@RequestParam(value = "institutionId") String institutionId,
                           @RequestParam(value = "merchantId") String merchantId,
                           @RequestParam(value = "agentId") String agentId,
                           @RequestParam(value = "appId") String appId,
                           @RequestParam(value = "orderNo") String orderNo,
                           @RequestParam(value = "payType") String payType,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "phone") String phone,
                           @RequestParam(value = "planId") Long planId,
                           @RequestParam(value = "trade_state") String trade_state,
                           @RequestParam(value = "total_fee") Long total_fee,
                           @RequestParam(value = "sign") String sign,
                           @RequestParam(value = "aisleCode") String aisleCode);


}
