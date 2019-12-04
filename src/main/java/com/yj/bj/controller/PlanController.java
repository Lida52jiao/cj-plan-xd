package com.yj.bj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yj.bj.constant.EasyConstant;
import com.yj.bj.constant.PlanConstant;
import com.yj.bj.constant.YJConstant;
import com.yj.bj.entity.*;
import com.yj.bj.entity.perameter.PlanParmeter;
import com.yj.bj.entity.user.CardInformation;
import com.yj.bj.entity.user.MerChants;
import com.yj.bj.service.plan.*;
import com.yj.bj.service.user.MerChantsService;
import com.yj.bj.util.*;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yj.bj.constant.EasyConstant.key;

/**
 * Created by bin on 2018/5/4.
 */
@RestController
@RequestMapping("/plan/")
public class PlanController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlanController.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private MerChantsService merChantsService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BatchService batchService;
    @Autowired
    private SignService signService;
    @Autowired
    private BindService bindService;

    /*//计算还款的天数
    @RequestMapping("/select")
    public YJResult selects(String merchantId, String institutionId, Long cardId, Long amount, Long balance) throws ParseException {
        String aisleCode="ld09";
        InstitutionEntity in=institutionService.findByPrimaryKey(institutionId);
        MerChants mer=merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer==null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        CardInformation card=merChantsService.getCard(cardId,in.getMerHost());
        if(card==null){
            return YJResult.build(YJConstant.CARD_NULL_CODE, YJConstant.CARD_NULL_MSG,null);
        }
        *//**生成还款日期时间戳*//*
        List<Long> timeList = PlanUtil.countPeriod(Integer.parseInt(card.getStatementDate()),Integer.parseInt(card.getRepaymentDate()),null);
        for(Long l:timeList){
            System.out.println(l);
        }
        //剩余的还款天数
        int day=timeList.size();
        System.out.println("剩余的还款天数"+day);
        if(day == 0){
            return YJResult.build("0001", "可用天数不足，请修改还款日");
        }
        //手续费
        Double totalFee = (BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(amount),new Double(1-new Double(mer.getMerChantFee()))),0) + day * 2) - amount;
        System.out.println("手续费"+totalFee);
        //平均最小的还款金额
        Long min = (long)(amount/(2 * day));
        System.out.println("平均最小的还款金额"+totalFee);
        //平均最大的还款金额
        Long max = balance - totalFee.longValue();
        System.out.println("平均最大的还款金额"+max);
        //最大还款的天数
        Integer maxDay = Integer.parseInt(amount/(min.longValue() * 2)+"");
        //最小还款的天数
        Integer minDay = Integer.parseInt(amount/(max * 2)+"");
        if(minDay >= maxDay){
            return YJResult.build("0001", "可用的天数不足", null);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("maxDay", maxDay);
        map.put("minDay", minDay);
        return YJResult.ok(JSON.toJSONString(map));
    }

    //计算还款的天数
    @RequestMapping("/selectTimeList")
    public YJResult select(String merchantId, String institutionId, Long cardId) throws ParseException {
        String aisleCode="ld09";
        InstitutionEntity in=institutionService.findByPrimaryKey(institutionId);
        MerChants mer=merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer==null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        CardInformation card=merChantsService.getCard(cardId,in.getMerHost());
        if(card==null){
            return YJResult.build(YJConstant.CARD_NULL_CODE, YJConstant.CARD_NULL_MSG,null);
        }
        *//**生成还款日期时间戳*//*
        List<Long> timeList = PlanUtil.countPeriods(Integer.parseInt(card.getStatementDate()),Integer.parseInt(card.getRepaymentDate()),null);
        for(Long l:timeList){
            System.out.println(l);
        }

        return YJResult.ok(JSON.toJSONString(timeList));
    }

    @RequestMapping("createPlan")
    public YJResult createPlan(String merchantId, String institutionId, String appId, Long cardId, Long amount, Long balance, String timeLists, String province, String city, String merchant_province, String merchant_city) throws ParseException {
        String aisleCode="ld09";
        InstitutionEntity in=institutionService.findByPrimaryKey(institutionId);
        MerChants mer=merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer==null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        CardInformation card=merChantsService.getCard(cardId,in.getMerHost());
        if(card==null){
            return YJResult.build(YJConstant.CARD_NULL_CODE, YJConstant.CARD_NULL_MSG,null);
        }
        *//**生成还款日期时间戳*//*
        //List<Long> timeList = PlanUtil.countPeriod(Integer.parseInt(card.getStatementDate()),Integer.parseInt(card.getRepaymentDate()),null);
        List<Frequency> hList = JSON.parseArray(timeLists,Frequency.class);
        List<Long> timeList = new ArrayList<>();
        //还款次数
        int totalRepaymentNumber = 0;
        for(Frequency l:hList){
            System.out.println(l);
            timeList.add(l.getTime());
            totalRepaymentNumber += l.getRepayment();
        }
        //剩余的还款天数
        int day = timeList.size();
        //手续费
        Double totalFee = (BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(amount),new Double(1-new Double(mer.getMerChantFee()))),0) + totalRepaymentNumber) - amount;
        //判断天数
        //Integer s = Integer.parseInt(BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(amount),Double.parseDouble(2*(balance - totalFee.longValue())+""),0)) + "");
        //获取平均还款的金额
        Long num = amount/totalRepaymentNumber;
        //平均最小的还款金额
        //Long min = (long)(amount/(2 * day));
        Long min = 10000L;
        //平均最大的还款金额
        Long max = balance - totalFee.longValue();
        //最大还款的天数
        //Integer maxDay = Integer.parseInt(amount/(min.longValue() * 2)+"");
        //最小还款的天数
        //Integer minDay = Integer.parseInt(amount/(max * 2)+"");
        //计算还款的次数
        //Long n = Long.parseLong(time*2 + "");
        Long n = Long.parseLong(totalRepaymentNumber + "");
        //获得还款的列表
        System.out.println(totalFee);
        System.out.println(amount);
        System.out.println(max);
        System.out.println(min);
        System.out.println(n);
        List<Long> list = null;
        try {
            list = PlanUtil.createAmountList(amount, max, min, n);
        }catch(Exception e){
            return YJResult.build("0001", "生成计划失败，请修改金额或天数后重试");
        }
        System.out.println(list);
        List<Long> longList = new ArrayList<>();
        longList.addAll(list);
        System.out.println(longList);
        List<PlanDetailEntity> pdList=new ArrayList<>();
        Long hk = 0L;
        Long xf = 0L;
        *//* 生成消费的信息 *//*
        for(int i = 0; i < hList.size(); i++){
            List<Long> repAmountList = new ArrayList<>();
            List<PlanDetailEntity> planDetailEntities = null;
            if(hList.get(i).getRepayment() == 1){
                repAmountList.add(list.get(0));
                list.remove(0);
                planDetailEntities = PlanUtil.createOneDay(repAmountList,hList.get(i).getConsume());
            }else{
                repAmountList.add(list.get(0));
                repAmountList.add(list.get(1));
                list.remove(0);
                list.remove(0);
                planDetailEntities = PlanUtil.createOneDay(repAmountList,hList.get(i).getConsume());
            }
            //pdList.addAll(planDetailEntities);
            Long batch1Number=getBatch("1",timeList.get(i));
            Long batch2Number=getBatch("2",timeList.get(i));
            int batch = 2;
            if (batch1Number - batch2Number > 0){
                batch = 1;
            }
            Long timeStamp=timeList.get(i);
            List<Integer> integerList = PlanUtil.get(Integer.parseInt(hList.get(i).getConsume() + hList.get(i).getRepayment()+""));
            Long arrivalAmount = 0L;

            for(int j = 0; j < planDetailEntities.size(); j++){
                String cycleId= OrderUtil.createOrderNo(idWorker,"HJC");
                String rOrderNo= OrderUtil.createOrderNo(idWorker,"HJR");
                Long timeRep = timeStamp+9*60*60*1000L+PlanUtil.random(1,39)*60*1000L+80*60*1000L*integerList.get(j);
                if(batch == 2){
                    timeRep += 40*60*1000L;
                }
                //还款
                if("1".equals(planDetailEntities.get(j).getPayType())){
                    //Long timeRep=timeStamp+(9 + 1*j)*60*60*1000L+PlanUtil.random(31,59)*60*1000L;
                    PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                    planDetailEntity.setOrderNo(rOrderNo);
                    planDetailEntity.setState(PlanConstant.EXECUTE);
                    planDetailEntity.setPayState(PlanConstant.WAIT);
                    planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                    planDetailEntity.setRepaymentOrderExpect(rOrderNo);
                    planDetailEntity.setRepaymentOrderReality(rOrderNo);
                    planDetailEntity.setPayType("1");
                    planDetailEntity.setAmount(planDetailEntities.get(j).getAmount());
                    planDetailEntity.setArrivalAmount(planDetailEntities.get(j).getAmount() - 100L);
                    planDetailEntity.setFee(100L);
                    planDetailEntity.setPayFee(0L);
                    planDetailEntity.setRepaymentFee(100L);
                    planDetailEntity.setExecuteTime(timeRep);
                    planDetailEntity.setMerchantId(merchantId);
                    planDetailEntity.setName(mer.getMerName());
                    planDetailEntity.setPhone(mer.getMerMp());
                    planDetailEntity.setBankCode(card.getIssuingBank());
                    planDetailEntity.setIdCardNo(mer.getCertNo());
                    planDetailEntity.setCardNo(card.getCardNumber());
                    planDetailEntity.setYear(card.getEffectiveYear());
                    planDetailEntity.setMonth(card.getEffectiveMonth());
                    planDetailEntity.setCvv2(card.getCv2());
                    planDetailEntity.setInstitutionId(institutionId);
                    planDetailEntity.setAppId(appId);
                    planDetailEntity.setIsLd("1");
                    planDetailEntity.setAgentId(mer.getAgentId());
                    planDetailEntity.setAisleCode(aisleCode);
                    planDetailEntity.setCycleId(cycleId);
                    planDetailEntity.setRate(mer.getMerChantFee());
                    planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                    planDetailEntity.setBatch(batch+"");
                    planDetailEntity.setMerProvince(mer.getProvince());
                    planDetailEntity.setMerCity(mer.getCity());
                    planDetailEntity.setMerCounty(mer.getCounty());
                    planDetailEntity.setProvince(province);
                    planDetailEntity.setCity(city);
                    pdList.add(planDetailEntity);
                }else {
                    if("2".equals(planDetailEntities.get(j + 1).getPayType())){
                        //计算支付的金额
                        Double rate=new Double(mer.getMerChantFee());

                        //Double f=BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(planDetailEntities.get(j).getArrivalAmount()),1D-rate),0);
                        //手续费等于支付的手续费
                        //Long fee = f.longValue() - planDetailEntities.get(j).getArrivalAmount();
                        Long xiaofei = planDetailEntities.get(j).getArrivalAmount()/100 * 100;
                        System.out.println("xiaofei=======================================" + xiaofei);
                        Double fee=BigDecilmalUtil.round(BigDecilmalUtil.mul(new Double(xiaofei),rate),0);
                        System.out.println("fee=======================================" + fee);

                        //Long timePay = timeStamp+(9+1*j)*60*60*1000L+PlanUtil.random(1,29)*60*1000L;

                        String payOrderNo= OrderUtil.createOrderNo(idWorker,"HJP");
                        PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                        planDetailEntity.setOrderNo(payOrderNo);
                        planDetailEntity.setState(PlanConstant.EXECUTE);
                        planDetailEntity.setPayState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentOrderExpect(rOrderNo);
                        planDetailEntity.setRepaymentOrderReality(rOrderNo);
                        planDetailEntity.setPayType("2");
                        planDetailEntity.setAmount(xiaofei);
                        planDetailEntity.setArrivalAmount(xiaofei - fee.longValue());
                        planDetailEntity.setFee(fee.longValue());
                        planDetailEntity.setPayFee(fee.longValue());
                        planDetailEntity.setRepaymentFee(0L);
                        planDetailEntity.setExecuteTime(timeRep);
                        planDetailEntity.setMerchantId(merchantId);
                        planDetailEntity.setName(mer.getMerName());
                        planDetailEntity.setPhone(mer.getMerMp());
                        planDetailEntity.setBankCode(card.getIssuingBank());
                        planDetailEntity.setIdCardNo(mer.getCertNo());
                        planDetailEntity.setCardNo(card.getCardNumber());
                        planDetailEntity.setYear(card.getEffectiveYear());
                        planDetailEntity.setMonth(card.getEffectiveMonth());
                        planDetailEntity.setCvv2(card.getCv2());
                        planDetailEntity.setInstitutionId(institutionId);
                        planDetailEntity.setAppId(appId);
                        planDetailEntity.setIsLd("1");
                        planDetailEntity.setAgentId(mer.getAgentId());
                        planDetailEntity.setAisleCode(aisleCode);
                        planDetailEntity.setCycleId(cycleId);
                        planDetailEntity.setRate(mer.getMerChantFee());
                        planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                        planDetailEntity.setBatch(batch+"");
                        planDetailEntity.setMerProvince(mer.getProvince());
                        planDetailEntity.setMerCity(mer.getCity());
                        planDetailEntity.setMerCounty(mer.getCounty());
                        planDetailEntity.setProvince(province);
                        planDetailEntity.setCity(city);
                        pdList.add(planDetailEntity);
                        arrivalAmount += xiaofei - fee.longValue();
                    }else{
                        //计算支付的金额
                        Double rate=new Double(mer.getMerChantFee());
                        System.out.println("planDetailEntities.get(j + 1).getAmount()============================"+planDetailEntities.get(j + 1).getAmount());
                        System.out.println("arrivalAmount============================"+arrivalAmount);
                        System.out.println("test============================"+(planDetailEntities.get(j + 1).getAmount() - arrivalAmount));

                        Double f=BigDecilmalUtil.round(BigDecilmalUtil.div(new Double((planDetailEntities.get(j + 1).getAmount() - arrivalAmount)),1D-rate),0);
                        System.out.println("f===================================="+f);
                        //手续费等于支付的手续费
                        Long fee = f.longValue() - (planDetailEntities.get(j + 1).getAmount() - arrivalAmount);

                        //Long timePay = timeStamp+(9+1*j)*60*60*1000L+PlanUtil.random(1,29)*60*1000L;

                        String payOrderNo= OrderUtil.createOrderNo(idWorker,"HJP");
                        PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                        planDetailEntity.setOrderNo(payOrderNo);
                        planDetailEntity.setState(PlanConstant.EXECUTE);
                        planDetailEntity.setPayState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentOrderExpect(rOrderNo);
                        planDetailEntity.setRepaymentOrderReality(rOrderNo);
                        planDetailEntity.setPayType("2");
                        planDetailEntity.setAmount(f.longValue());
                        planDetailEntity.setArrivalAmount(planDetailEntities.get(j + 1).getAmount() - arrivalAmount);
                        planDetailEntity.setFee(fee);
                        planDetailEntity.setPayFee(fee);
                        planDetailEntity.setRepaymentFee(0L);
                        planDetailEntity.setExecuteTime(timeRep);
                        planDetailEntity.setMerchantId(merchantId);
                        planDetailEntity.setName(mer.getMerName());
                        planDetailEntity.setPhone(mer.getMerMp());
                        planDetailEntity.setBankCode(card.getIssuingBank());
                        planDetailEntity.setIdCardNo(mer.getCertNo());
                        planDetailEntity.setCardNo(card.getCardNumber());
                        planDetailEntity.setYear(card.getEffectiveYear());
                        planDetailEntity.setMonth(card.getEffectiveMonth());
                        planDetailEntity.setCvv2(card.getCv2());
                        planDetailEntity.setInstitutionId(institutionId);
                        planDetailEntity.setAppId(appId);
                        planDetailEntity.setIsLd("1");
                        planDetailEntity.setAgentId(mer.getAgentId());
                        planDetailEntity.setAisleCode(aisleCode);
                        planDetailEntity.setCycleId(cycleId);
                        planDetailEntity.setRate(mer.getMerChantFee());
                        planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                        planDetailEntity.setBatch(batch+"");
                        planDetailEntity.setMerProvince(mer.getProvince());
                        planDetailEntity.setMerCity(mer.getCity());
                        planDetailEntity.setMerCounty(mer.getCounty());
                        planDetailEntity.setProvince(province);
                        planDetailEntity.setCity(city);
                        pdList.add(planDetailEntity);
                        arrivalAmount = 0L;
                    }
                }
            }
        }
        //生成整体的计划
        Long totalFees = 0L;
        Long totalPayFees = 0L;
        Long totalRepaymentFees = 0L;
        Long numbers = 0L;//还款笔数
        Long payNumbers = 0L;//支付笔数
        //1还款2消费
        for(int s = 0; s < pdList.size(); s++){
            totalFees += pdList.get(s).getFee();
            totalPayFees += pdList.get(s).getPayFee();
            totalRepaymentFees += pdList.get(s).getRepaymentFee();
            if("1".equals(pdList.get(s).getPayType())){
                numbers++;
            }
            if("2".equals(pdList.get(s).getPayType())){
                payNumbers++;
            }
        }
        String timeStampStr="";
        for(int w = 0; w < timeList.size(); w++){
            if (w!=0){
                timeStampStr+=",";
            }
            timeStampStr += timeList.get(w);
        }
        Collections.sort(longList);
        String cycleId = OrderUtil.createOrderNo(idWorker,"HJC");
        for (PlanDetailEntity pd:pdList){
            pd.setCycleId(cycleId);
            if (pd.getPayType().equals("1")){
                cycleId=OrderUtil.createOrderNo(idWorker,"HJC");
            }
        }
        PlanEntity plan=new PlanEntity();
        plan.setStartTime(pdList.get(0).getExecuteTime());
        plan.setState(PlanConstant.EXECUTE);
        plan.setTimeStampStr(timeStampStr);
        plan.setTotalPayFee(totalPayFees);
        plan.setTotalRepaymentFee(totalRepaymentFees);
        plan.setTotalFee(totalFees);
        plan.setTotalAmount(amount);
        plan.setMaxAmount(longList.get(longList.size() - 1));
        plan.setBasicAmount(balance);
        plan.setAlreadyAmount(0L);
        plan.setTotalDay(day+"");
        plan.setNumber(numbers+"");
        plan.setPayNumber(payNumbers+"");
        plan.setMerchantId(merchantId);
        plan.setName(mer.getMerName());
        plan.setPhone(mer.getMerMp());
        plan.setBankCode(card.getIssuingBank());
        plan.setIdCardNo(mer.getCertNo());
        plan.setCardNo(card.getCardNumber());
        plan.setYear(card.getEffectiveYear());
        plan.setMonth(card.getEffectiveMonth());
        plan.setCvv2(card.getCv2());
        plan.setAgentId(mer.getAgentId());
        plan.setInstitutionId(institutionId);
        plan.setAppId(appId);
        plan.setIsLd("1");
        plan.setAisleCode(aisleCode);
        plan.setProvince(merchant_province);
        plan.setCity(merchant_city);

        PlanParmeter pp=new PlanParmeter();
        String planJsonKey= UUID.randomUUID().toString();
        pp.setPlanJsonKey(planJsonKey);
        pp.setPlan(plan);
        pp.setList(pdList);
        String planJson = JSON.toJSONString(pp);
        System.out.println(planJson);
        Jedis jedis= RedisUtils.getJedis();
        jedis.set(planJsonKey,planJson);
        jedis.expire(planJsonKey,3600);
        RedisUtils.returnResource(jedis);
        return YJResult.ok(pp);
    }*/

    @RequestMapping("createPlan") //创建计划
    public YJResult createPlan(@RequestParam(defaultValue = "3") String type, String province, String city, String merchantId, String institutionId, String appId, Long cardId, Long amount, String cardNumber, String bankCode) throws ParseException {
        String aisleCode = PlanConstant.aisleCode; //通道标识
        //type 1 本金最少  2时间最短 3听天由命1
        if (amount < 200000L) {//判断金额
            return YJResult.build("1234", "本功能暂不支持2000元以下，请使用其他功能", null);
        }
        InstitutionEntity in = institutionService.findByPrimaryKey(institutionId);//机构（不同的软件）
        MerChants mer = merChantsService.getMerRate(merchantId, aisleCode, in.getMerHost());//商户
        if (mer == null) {
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG, null);
        }
        CardInformation card = merChantsService.getCard(cardId, in.getMerHost());//获取卡信息
        if (card == null) {
            return YJResult.build(YJConstant.CARD_NULL_CODE, YJConstant.CARD_NULL_MSG, null);
        }
        //限额
        Long min = 25000L;
        Long max = 95000L;
        //**生成还款日期时间戳*//*
        List<Long> timeList = PlanUtil.countPeriod(Integer.parseInt(card.getStatementDate()), Integer.parseInt(card.getRepaymentDate()), null);
        /*for (Long l : timeList) {
            System.out.println(l);
        }*/
        //还款天数
        int day = timeList.size();
        int maxNumber = (int) (amount / min - 1L);//?
        int minNumber = (int) (amount / max + 1L);

        int dayNum = 3;//TODO 5

        //System.out.println("  minNumber=" + minNumber + "  maxNumber=" + maxNumber + "  " + day);
        if (minNumber > day * dayNum) {
            return YJResult.build("6002", "可用时间不足");
        }
        //System.out.println(minNumber + " " + maxNumber);
        maxNumber -= maxNumber % dayNum;
        //System.out.println(minNumber % dayNum);
        if (minNumber % 3 != 0) {//TODO
            minNumber += 3 - minNumber % dayNum;
        }
        //System.out.println(minNumber + " " + maxNumber);
        //还款笔数
        int number = 0;

        if ("1".equals(type)) {
            number = maxNumber > day * dayNum ? day * dayNum : maxNumber;
        } else if ("2".equals(type)) {
            number = minNumber;
        } else {
            if (maxNumber > day * dayNum) {
                number = PlanUtil.random(minNumber, day * dayNum);
            } else {
                number = PlanUtil.random(minNumber, maxNumber);
            }
        }

        //number-=number%3;

        //**生成还款金额*//*
        //平均还款金额
        Long pj = amount / number;
        //
        //System.out.println("pj=" + pj + " number=" + number);
        if (pj < min || pj > max) {
            return YJResult.build("6001", "平均还款金额不在可用范围（500-1000）");
        }
        List<Long> amountList = new ArrayList<>();
        //计算浮动值
        Long m = pj - min < max - pj ? pj - min : max - pj;
        Long n = m / 2;
        Long ta = 0L;
        Long sub = 0L;
        for (int i = 0; i < number; i++) {
            if (i + 1 == number) {
                //最后一笔
                amountList.add(amount - ta);
                ta += amount - ta;
            } else {
                Long a = PlanUtil.countAmount(pj, n, sub);
                sub = a - pj + sub;
                a = a / 100 * 100;
                amountList.add(a);
                ta += a;
            }
        }
        List<Long> repList=new ArrayList<>();
        Long ca=0L;
        for (int i = 0; i < amountList.size(); i++) {
            //System.out.println(amountList.get(i));
            if (i%3==0&&i!=0){
                repList.add(ca+100L);//TODO
                ca=0L;
            }
            ca+=amountList.get(i);
            if (i==amountList.size()-1&&ca>0L){
                repList.add(ca+100L);//TODO
            }

        }
        //System.out.println(JSON.toJSONString(repList));
        timeList=PlanUtil.randomTimeList(timeList,repList.size());

        List<Long> longList=new ArrayList<>();
        longList.addAll(repList);

        List<PlanDetailEntity> pdList=new ArrayList<>();
        Long hk = 0L;
        Long xf = 0L;

        List<Frequency> hList=new ArrayList<>();
        for (int i = 0; i < repList.size(); i++){
            Frequency f=new Frequency();
            f.setTime(timeList.get(i));
            f.setRepayment(1);
            f.setConsume(3);
            hList.add(f);
        }
        //System.out.println(JSON.toJSONString(hList));
        //System.out.println("===="+repList.size()+"====="+hList.size());
        //生成消费的信息
        for(int i = 0; i < hList.size(); i++){
            List<Long> repAmountList = new ArrayList<>();
            List<PlanDetailEntity> planDetailEntities = null;
            if(hList.get(i).getRepayment() == 1){
                repAmountList.add(repList.get(0));
                repList.remove(0);
                planDetailEntities = PlanUtil.createOneDay(repAmountList,hList.get(i).getConsume());
            }else{
                repAmountList.add(repList.get(0));
                repAmountList.add(repList.get(1));
                repList.remove(0);
                repList.remove(0);
                planDetailEntities = PlanUtil.createOneDay(repAmountList,hList.get(i).getConsume());
            }
            //pdList.addAll(planDetailEntities);
            Long batch1NumberG=getBatch("1",timeList.get(i));
            Long batch2NumberG=getBatch("2",timeList.get(i));
            int batchG = 2;
            if (batch1NumberG - batch2NumberG > 0){
                batchG = 1;
            }
            Long timeStamp=timeList.get(i);
            List<Integer> integerList = PlanUtil.get(Integer.parseInt(hList.get(i).getConsume() + hList.get(i).getRepayment()+""));
            Long arrivalAmount = 0L;
            for(int j = 0; j < planDetailEntities.size(); j++){

                String cycleIdG= OrderUtil.createOrderNo(idWorker,"SC");
                String rOrderNoG= OrderUtil.createOrderNo(idWorker,"SR");

                Long timeRep = timeStamp+9*60*60*1000L+PlanUtil.random(1,39)*60*1000L+80*60*1000L*integerList.get(j);
                if(batchG == 2){
                    timeRep += 40*60*1000L;
                }
                //还款
                if("1".equals(planDetailEntities.get(j).getPayType())){
                    //Long timeRep=timeStamp+(9 + 1*j)*60*60*1000L+PlanUtil.random(31,59)*60*1000L;
                    PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                    planDetailEntity.setOrderNo(rOrderNoG);
                    planDetailEntity.setState(PlanConstant.EXECUTE);
                    planDetailEntity.setPayState(PlanConstant.WAIT);
                    planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                    planDetailEntity.setRepaymentOrderExpect(rOrderNoG);
                    planDetailEntity.setRepaymentOrderReality(rOrderNoG);
                    planDetailEntity.setPayType("1");
                    planDetailEntity.setAmount(planDetailEntities.get(j).getAmount());
                    planDetailEntity.setArrivalAmount(planDetailEntities.get(j).getAmount() - 100L);
                    planDetailEntity.setFee(100L);
                    planDetailEntity.setPayFee(0L);
                    planDetailEntity.setRepaymentFee(100L);
                    planDetailEntity.setExecuteTime(timeRep);
                    planDetailEntity.setMerchantId(merchantId);
                    planDetailEntity.setName(mer.getMerName());
                    planDetailEntity.setPhone(mer.getMerMp());
                    planDetailEntity.setBankCode(bankCode);
                    planDetailEntity.setIdCardNo(mer.getCertNo());
                    planDetailEntity.setCardNo(cardNumber);
                    planDetailEntity.setInstitutionId(institutionId);
                    planDetailEntity.setAppId(appId);
                    planDetailEntity.setIsLd("1");
                    planDetailEntity.setAgentId(mer.getAgentId());
                    planDetailEntity.setAisleCode(aisleCode);
                    planDetailEntity.setCycleId(cycleIdG);
                    planDetailEntity.setRate(mer.getMerChantFee());
                    planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                    planDetailEntity.setBatch(batchG+"");
                    planDetailEntity.setMerProvince(mer.getProvince());
                    planDetailEntity.setMerCity(mer.getCity());
                    planDetailEntity.setMerCounty(mer.getCounty());

                    planDetailEntity.setProvince(province);
                    planDetailEntity.setCity(city);

                    pdList.add(planDetailEntity);
                }else {
                    if("2".equals(planDetailEntities.get(j + 1).getPayType())){
                        //计算支付的金额
                        Double rate=new Double(mer.getMerChantFee());

                        //Double f=BigDecilmalUtil.round(BigDecilmalUtil.div(new Double(planDetailEntities.get(j).getArrivalAmount()),1D-rate),0);
                        //手续费等于支付的手续费
                        //Long fee = f.longValue() - planDetailEntities.get(j).getArrivalAmount();
                        Long xiaofei = planDetailEntities.get(j).getArrivalAmount()/100 * 100;
                        //System.out.println("xiaofei=======================================" + xiaofei);
                        Double fee=BigDecilmalUtil.round(BigDecilmalUtil.mul(new Double(xiaofei),rate),0);
                        //System.out.println("fee=======================================" + fee);

                        //Long timePay = timeStamp+(9+1*j)*60*60*1000L+PlanUtil.random(1,29)*60*1000L;

                        String payOrderNo= OrderUtil.createOrderNo(idWorker,"SP");
                        PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                        planDetailEntity.setOrderNo(payOrderNo);
                        planDetailEntity.setState(PlanConstant.EXECUTE);
                        planDetailEntity.setPayState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentOrderExpect(rOrderNoG);
                        planDetailEntity.setRepaymentOrderReality(rOrderNoG);
                        planDetailEntity.setPayType("2");
                        planDetailEntity.setAmount(xiaofei);
                        planDetailEntity.setArrivalAmount(xiaofei - fee.longValue());
                        planDetailEntity.setFee(fee.longValue());
                        planDetailEntity.setPayFee(fee.longValue());
                        planDetailEntity.setRepaymentFee(0L);
                        planDetailEntity.setExecuteTime(timeRep);
                        planDetailEntity.setMerchantId(merchantId);
                        planDetailEntity.setName(mer.getMerName());
                        planDetailEntity.setPhone(mer.getMerMp());
                        planDetailEntity.setBankCode(card.getIssuingBank());
                        planDetailEntity.setIdCardNo(mer.getCertNo());
                        planDetailEntity.setCardNo(card.getCardNumber());
                        planDetailEntity.setYear(card.getEffectiveYear());
                        planDetailEntity.setMonth(card.getEffectiveMonth());
                        planDetailEntity.setCvv2(card.getCv2());
                        planDetailEntity.setInstitutionId(institutionId);
                        planDetailEntity.setAppId(appId);
                        planDetailEntity.setIsLd("1");
                        planDetailEntity.setAgentId(mer.getAgentId());
                        planDetailEntity.setAisleCode(aisleCode);
                        planDetailEntity.setCycleId(cycleIdG);
                        planDetailEntity.setRate(mer.getMerChantFee());
                        planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                        planDetailEntity.setBatch(batchG+"");
                        planDetailEntity.setMerProvince(mer.getProvince());
                        planDetailEntity.setMerCity(mer.getCity());
                        planDetailEntity.setMerCounty(mer.getCounty());

                        planDetailEntity.setProvince(province);
                        planDetailEntity.setCity(city);


                        pdList.add(planDetailEntity);
                        arrivalAmount += xiaofei - fee.longValue();
                    }else{
                        //计算支付的金额
                        Double rate=new Double(mer.getMerChantFee());
                        //System.out.println("planDetailEntities.get(j + 1).getAmount()============================"+planDetailEntities.get(j + 1).getAmount());
                        //System.out.println("arrivalAmount============================"+arrivalAmount);
                        //System.out.println("test============================"+(planDetailEntities.get(j + 1).getAmount() - arrivalAmount));

                        Double f=BigDecilmalUtil.round(BigDecilmalUtil.div(new Double((planDetailEntities.get(j + 1).getAmount() - arrivalAmount)),1D-rate),0);
                        //System.out.println("f===================================="+f);
                        //手续费等于支付的手续费
                        Long fee = f.longValue() - (planDetailEntities.get(j + 1).getAmount() - arrivalAmount);

                        //Long timePay = timeStamp+(9+1*j)*60*60*1000L+PlanUtil.random(1,29)*60*1000L;

                        String payOrderNo= OrderUtil.createOrderNo(idWorker,"SP");
                        PlanDetailEntity planDetailEntity = new PlanDetailEntity();
                        planDetailEntity.setOrderNo(payOrderNo);
                        planDetailEntity.setState(PlanConstant.EXECUTE);
                        planDetailEntity.setPayState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentState(PlanConstant.WAIT);
                        planDetailEntity.setRepaymentOrderExpect(rOrderNoG);
                        planDetailEntity.setRepaymentOrderReality(rOrderNoG);
                        planDetailEntity.setPayType("2");
                        planDetailEntity.setAmount(f.longValue());
                        planDetailEntity.setArrivalAmount(planDetailEntities.get(j + 1).getAmount() - arrivalAmount);
                        planDetailEntity.setFee(fee);
                        planDetailEntity.setPayFee(fee);
                        planDetailEntity.setRepaymentFee(0L);
                        planDetailEntity.setExecuteTime(timeRep);
                        planDetailEntity.setMerchantId(merchantId);
                        planDetailEntity.setName(mer.getMerName());
                        planDetailEntity.setPhone(mer.getMerMp());
                        planDetailEntity.setBankCode(card.getIssuingBank());
                        planDetailEntity.setIdCardNo(mer.getCertNo());
                        planDetailEntity.setCardNo(card.getCardNumber());
                        planDetailEntity.setYear(card.getEffectiveYear());
                        planDetailEntity.setMonth(card.getEffectiveMonth());
                        planDetailEntity.setCvv2(card.getCv2());
                        planDetailEntity.setInstitutionId(institutionId);
                        planDetailEntity.setAppId(appId);
                        planDetailEntity.setIsLd("1");
                        planDetailEntity.setAgentId(mer.getAgentId());
                        planDetailEntity.setAisleCode(aisleCode);
                        planDetailEntity.setCycleId(cycleIdG);
                        planDetailEntity.setRate(mer.getMerChantFee());
                        planDetailEntity.setD0Fee(mer.getGenerationFeeRepayment()+"");
                        planDetailEntity.setBatch(batchG+"");
                        planDetailEntity.setMerProvince(mer.getProvince());
                        planDetailEntity.setMerCity(mer.getCity());
                        planDetailEntity.setMerCounty(mer.getCounty());

                        planDetailEntity.setProvince(province);
                        planDetailEntity.setCity(city);


                        pdList.add(planDetailEntity);
                        arrivalAmount = 0L;
                    }
                }
            }
        }
        //生成整体的计划
        Long totalFees = 0L;
        Long totalPayFees = 0L;
        Long totalRepaymentFees = 0L;
        Long numbers = 0L;//还款笔数
        Long payNumbers = 0L;//支付笔数
        //1还款2消费
        for(int s = 0; s < pdList.size(); s++){
            totalFees += pdList.get(s).getFee();
            totalPayFees += pdList.get(s).getPayFee();
            totalRepaymentFees += pdList.get(s).getRepaymentFee();
            if("1".equals(pdList.get(s).getPayType())){
                numbers++;
            }
            if("2".equals(pdList.get(s).getPayType())){
                payNumbers++;
            }
        }
        String timeStampStr="";
        for(int w = 0; w < timeList.size(); w++){
            if (w!=0){
                timeStampStr+=",";
            }
            timeStampStr += timeList.get(w);
        }
        Collections.sort(longList);
        String cycleId= OrderUtil.createOrderNo(idWorker,"SC");

        Long maxRepAmount=0L;

        for (PlanDetailEntity pd:pdList){
            pd.setCycleId(cycleId);
            if (pd.getPayType().equals("1")){
                cycleId=OrderUtil.createOrderNo(idWorker,"SC");
                if (pd.getAmount()>maxRepAmount){
                    maxRepAmount=pd.getAmount();
                }
            }

        }

        PlanEntity plan=new PlanEntity();
        plan.setStartTime(pdList.get(0).getExecuteTime());
        plan.setState(PlanConstant.EXECUTE);
        plan.setTimeStampStr(timeStampStr);
        plan.setTotalPayFee(totalPayFees);
        plan.setTotalRepaymentFee(totalRepaymentFees);
        plan.setTotalFee(totalFees);
        plan.setTotalAmount(amount);
        plan.setMaxAmount(longList.get(longList.size() - 1));
        plan.setBasicAmount(maxRepAmount+totalFees);
        plan.setAlreadyAmount(0L);
        plan.setTotalDay(repList.size()+"");
        plan.setNumber(numbers+"");
        plan.setPayNumber(payNumbers+"");
        plan.setMerchantId(merchantId);
        plan.setName(mer.getMerName());
        plan.setPhone(mer.getMerMp());
        plan.setBankCode(card.getIssuingBank());
        plan.setIdCardNo(mer.getCertNo());
        plan.setCardNo(card.getCardNumber());
        plan.setYear(card.getEffectiveYear());
        plan.setMonth(card.getEffectiveMonth());
        plan.setCvv2(card.getCv2());
        plan.setAgentId(mer.getAgentId());
        plan.setInstitutionId(institutionId);
        plan.setAppId(appId);
        plan.setIsLd("1");
        plan.setAisleCode(aisleCode);

        plan.setProvince(province);
        plan.setCity(city);

        PlanParmeter pp=new PlanParmeter();
        String planJsonKey= UUID.randomUUID().toString();
        pp.setPlanJsonKey(planJsonKey);
        pp.setPlan(plan);
        pp.setList(pdList);
        String planJson = JSON.toJSONString(pp);
        //System.out.println(planJson);
        Jedis jedis= RedisUtils.getJedis();
        jedis.set(planJsonKey,planJson);
        jedis.expire(planJsonKey,3600);
        RedisUtils.returnResource(jedis);
        return YJResult.ok(pp);
    }

    //保存计划
    @RequestMapping("savePlan")
    public YJResult savePlan(String planJsonKey, String orderNos){
        return planService.savePlan(planJsonKey);
    }

    //停止计划
    @RequestMapping("stopPlan")
    public YJResult stopPlan(Long planId){
        return planService.stopPlan(planId);
    }

    //查计划
    @RequestMapping("findPlan")
    @ResponseBody
    public YJResult findPlan(PlanEntity plan){
        PageInfo<PlanEntity> planList=planService.queryPageForList(plan);
        return YJResult.build(YJConstant.SUCCESS_CODE, YJConstant.SUCCESS_MSG,planList);
    }

    //查询计划的详情
    @RequestMapping("findPlanDetail")
    @ResponseBody
    public YJResult findPlanDetail(PlanDetailEntity planDetail){
        PageInfo<PlanDetailEntity> planDetailList=planDetailService.queryPageForList(planDetail);
        return YJResult.build(YJConstant.SUCCESS_CODE, YJConstant.SUCCESS_MSG,planDetailList);
    }

    public Long getBatch(String batch,Long time){
        String aisleCode="ld16";
        BatchEntity batchDB=new BatchEntity();
        batchDB.setAisleCode(aisleCode);
        batchDB.setBatch(batch);
        batchDB.setExecuteDate(DateUtil.zero(time));
        batchDB=batchService.findByObject(batchDB);
        if (batchDB==null){
            return 0L;
        }
        return batchDB.getNumber()!=null&&!batchDB.getNumber().equals(0L)?batchDB.getNumber():0L;
    }

    @RequestMapping("updateRate")
    @ResponseBody
    public YJResult updateRate(String merchantId,
                               String aisleCode,
                               String cardNo,
                               String institutionId) throws Exception {
        InstitutionEntity in = institutionService.findByPrimaryKey(institutionId);
        MerChants mer = merChantsService.getMerRate(merchantId,aisleCode,in.getMerHost());
        if(mer == null){
            return YJResult.build(YJConstant.MER_NULL_CODE, YJConstant.MER_NULL_MSG,null);
        }
        SignEntity signEntity = new SignEntity();
        signEntity.setMerchantId(merchantId);
        signEntity.setCreditCardNumber(cardNo);
        signEntity.setSignState("0000");
        signEntity = signService.findByObject(signEntity);
        BindEntity bindEntity = new BindEntity();
        bindEntity.setState("0000");
        bindEntity.setMerchantId(merchantId);
        BindEntity bind = bindService.findByObject(bindEntity);
        if(bind == null){
            return YJResult.build("0001", "商户未开户");
        }
        String url = EasyConstant.host+"/bind/updateRate";
        HashMap<String,Object> map = Maps.newHashMap();
        map.put("institutionId",EasyConstant.spCode);
        map.put("aisleCode",EasyConstant.channelCode);
        map.put("outMerId",merchantId);
        map.put("aisleMerId",signEntity.getAisleMerId());
        map.put("debitCardNo",bind.getCardNo());
        map.put("rate",mer.getMerChantFee());
        map.put("d0Fee","100");
        map.put("sign",SignUtil.createYJSign(map));
        map.put("creditCardNo",cardNo);
        String result = HttpClientUtil.doPost(url,map);
        JSONObject job = JSONObject.parseObject(result);
        if("0000".equals(job.getString("code"))){
            return YJResult.ok();
        }
        return YJResult.build(job.getString("code"), job.getString("msg"));
//        // 获取令牌
//        BaseResMessage<TokenRes> tokenRes = new GetSpToken().token(key, EasyConstant.spCode);
//        String token = tokenRes.getData().getToken();
//        // 解密令牌
//        String tokenClearText = EncryptUtil.desDecrypt(token, key);
//
//
//        // 构建签名参数
//        TreeMap<String, Object> signParams = new TreeMap<String, Object>();
//        signParams.put("token", tokenClearText);
//        signParams.put("spCode", EasyConstant.spCode);
//        signParams.put("merchantCode", sign.getAisleMerId());
//        signParams.put("channelCode", EasyConstant.channelCode);
//        signParams.put("changeType", "1");
//		signParams.put("debitRate", mer.getMerChantFee());
//		signParams.put("debitCapAmount", "99999999");
//		signParams.put("creditRate", mer.getMerChantFee());
//		signParams.put("creditCapAmount", "99999999");
//        /*signParams.put("withdrawDepositRate", withdrawDepositRate);
//        signParams.put("withdrawDepositSingleFee", withdrawDepositSingleFee);*/
//
//
//        // 构建请求参数
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("token", tokenClearText);
//        jsonObj.put("spCode", EasyConstant.spCode);
//        jsonObj.put("merchantCode", sign.getAisleMerId());
//        jsonObj.put("channelCode", EasyConstant.channelCode);
//        jsonObj.put("changeType", "1");
//		jsonObj.put("debitRate", mer.getMerChantFee());
//		jsonObj.put("debitCapAmount", "99999999");
//		jsonObj.put("creditRate", mer.getMerChantFee());
//		jsonObj.put("creditCapAmount", "99999999");
//        /*jsonObj.put("withdrawDepositRate", withdrawDepositRate);
//        jsonObj.put("withdrawDepositSingleFee", withdrawDepositSingleFee);*/
//        jsonObj.put("sign", SignUtil.signByMap(EasyConstant.key, signParams));
//
//        // 接口访问
//        String jsonReq = jsonObj.toJSONString();
//        logger.info(sdf.format(new Date()) + "请求信息: " + jsonReq);
//        //System.out.println(sdf.format(new Date()) + "请求信息: " + jsonReq);
//
//        Response response = HttpUtil.sendPost(Constants.getServerUrl() + "/v2/merchant/merchantChange", jsonReq);
//        if (response.isSuccessful()) {
//            String jsonRsp = response.body().string();
//            logger.info(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            //System.out.println(sdf.format(new Date()) + "响应信息: " + jsonRsp);
//            JSONObject job = JSONObject.parseObject(jsonRsp);
//            if("000000".equals(job.getString("code"))){
//                return YJResult.ok();
//            }
//            //{"code":"000000","message":"SUCCESS","data":{"merchantCode":"100000000967045"}}
//            return YJResult.build("0001", "修改失败");
//        }
//        return YJResult.build(String.valueOf(response.code()),response.message());
    }
}
