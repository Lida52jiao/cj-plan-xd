package com.yj.bj.util;



import com.yj.bj.constant.UrlConstant;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by bin on 2018/2/1.
 */
public class ConmonUtil {
    public static void send(String bank,String amount,String phone,String planId,String appId,String agentId,String institutionId,String type){
        //1计划失败   2消费成功    3还款成功

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("bank",bank);
        hashMap.put("number",amount);
        hashMap.put("phone",phone);
        hashMap.put("planId",planId);
        hashMap.put("appId",appId);
        hashMap.put("agentId",agentId);
        hashMap.put("institutionId",institutionId);
        hashMap.put("type",type);
        String result=new HttpClientUtil().doPost(UrlConstant.SEND_SMS,hashMap);
    }

    public static void main(String[] args) {
        //merChantId=M45577392153952256012367&institutionId=T00000000&appId=0000&token=193603a6bc1cbec6cc168a569452359c
        /*HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("token","193603a6bc1cbec6cc168a569452359c");
        hashMap.put("merChantId","M45577392153952256012367");
        hashMap.put("merName","袁玲");
        hashMap.put("cardNumber","6217000010031620779");
        hashMap.put("certNo","430722199606085908");
        String result=HttpClientUtil.doGet("http://user.1818pay.cn/yj-mer/Attestation/attestation.shtml",hashMap);
        System.out.println(result);*/
        String [] timeStrArr="1528819200000,1528905600000".split(",");
        Long lastDayTime=Long.parseLong(timeStrArr[timeStrArr.length-1]);
        Long time=new Date().getTime();
        Long toDayTime= DateUtil.zero(time);
        System.out.println(time+"  "+toDayTime+"   "+lastDayTime);
        if (lastDayTime.equals(toDayTime-24*3600*1000L)){
            System.out.println(1);
        }

    }
}
