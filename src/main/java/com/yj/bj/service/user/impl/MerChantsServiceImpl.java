package com.yj.bj.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yj.bj.constant.UrlConstant;
import com.yj.bj.entity.user.CardInformation;
import com.yj.bj.entity.user.MerChants;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.HttpClientUtil;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by bin on 2017/11/7.
 */
@Service
public class MerChantsServiceImpl implements MerChantsService {

    @Override
    public MerChants getMer(String merId, String merHost) {
        String url= merHost+ UrlConstant.GET_MER+merId;
        //System.out.println(url);
        String result=new HttpClientUtil().doGet(url);
        MerChants mer= JSON.parseObject(result,MerChants.class);
        //System.out.println(result);
        return mer;
    }
    @Override
    public MerChants getMerRate(String merId,String aisleCode, String merHost) {
        LinkedHashMap<String,Object> hashMap=new LinkedHashMap<String,Object>();
        hashMap.put("merChantId",merId);
        hashMap.put("aisleCode",aisleCode);
        String result=new HttpClientUtil().doPost(merHost+ UrlConstant.GET_MER_RATE,hashMap);
        MerChants mer= JSON.parseObject(result,MerChants.class);
        //System.out.println(result);
        return mer;
    }
    @Override
    public CardInformation getCard(Long cardId, String merHost) {
        String url= merHost+ UrlConstant.GET_CARD+cardId;
        //System.out.println(url);
        String result=new HttpClientUtil().doGet(url);
        CardInformation card= JSON.parseObject(result,CardInformation.class);
        //System.out.println(result);
        return card;
    }
    @Override
    public List<CardInformation> getCardList(String merId, String cardType, String token, String merHost){
        LinkedHashMap<String,Object> hashMap=new LinkedHashMap<String,Object>();
        hashMap.put("merChantId",merId);
        hashMap.put("cardType",cardType);
        hashMap.put("token",token);
        String result=new HttpClientUtil().doPost(merHost+ UrlConstant.GET_CARD_LIST,hashMap);
        //System.out.println(result);
        JSONObject job = JSONObject.parseObject(result);
        List<CardInformation> cardList=JSON.parseArray(job.getString("data"),CardInformation.class);
        return cardList;
    }
}
