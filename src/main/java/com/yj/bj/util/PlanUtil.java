package com.yj.bj.util;




import com.yj.bj.entity.PlanDetailEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bin on 2018/1/17.
 */
public class PlanUtil {
    public static Long countAmount(Long pj,Long n,Long sub){
        if (sub<0){
            return pj+random((int)(0-n-sub+n),(int)(n+n))-n;
        }else {
            return pj+random((int)(0-n+n),(int)(n-sub+n))-n;
        }
    }
    //生成随机数
    public static int random(int min,int max){
        return new Random().nextInt(max)%(max-min+1) + min;
    }

    /**
     * 根据账单日 还款日 计算账单周期
     */
    public static List<Long> countPeriod(int startDay, int endDay,Long executeTime)throws ParseException {
        if (null==executeTime||executeTime.equals(0L)){
            executeTime=new Date().getTime();
        }
        Calendar cale = Calendar.getInstance();//生成日期
        cale.setTime(new Date(executeTime));
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        int maximum = cale.getActualMaximum(Calendar.DATE);//获取当月最大天数

        Long start=0L;
        List<Long> timestampList=new ArrayList<>();
        int periodNumber=0;
        if (startDay<endDay){
            if (day<startDay){
                //账单日到还款日
                periodNumber=endDay-startDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+startDay).getTime();
            }else if(day>endDay){
                //下月账单日到还款日
                periodNumber=endDay-startDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+(month+1)+"/"+startDay).getTime();
            }else {
                //开始日到还款日
                periodNumber=endDay-day;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }
        }else { //startDay>endDay
            if (day<endDay){
                //开始日到还款日
                periodNumber=endDay-day;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }else if(day>startDay){
                //开始日到下月还款日
                periodNumber=maximum-day+endDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }else {
                //账单日到下月还款日
                periodNumber=maximum-startDay+endDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+startDay).getTime();
            }
        }
        for(int i=1;i<periodNumber - 1;i++){
            timestampList.add(start+i*24*3600*1000L);
        }
        System.out.println(periodNumber+"================");
        return timestampList;
    }

    /**
     * 根据账单日 还款日 计算账单周期
     */
    public static List<Long> countPeriods(int startDay, int endDay,Long executeTime)throws ParseException {
        if (null==executeTime||executeTime.equals(0L)){
            executeTime=new Date().getTime();
        }
        Calendar cale = Calendar.getInstance();
        cale.setTime(new Date(executeTime));
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        int maximum = cale.getActualMaximum(Calendar.DATE);

        Long start=0L;
        List<Long> timestampList=new ArrayList<>();
        int periodNumber=0;
        if (startDay<endDay){
            if (day<startDay){
                //账单日到还款日
                periodNumber=endDay-startDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+startDay).getTime();
            }else if(day>endDay){
                //下月账单日到还款日
                periodNumber=endDay-startDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+(month+1)+"/"+startDay).getTime();
            }else {
                //开始日到还款日
                periodNumber=endDay-day;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }
        }else {
            if (day<endDay){
                //开始日到还款日
                periodNumber=endDay-day;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }else if(day>startDay){
                //开始日到下月还款日
                periodNumber=maximum-day+endDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+day).getTime();
            }else {
                //账单日到下月还款日
                periodNumber=maximum-startDay+endDay;
                start=new SimpleDateFormat("yyyy/MM/dd").parse(""+year+"/"+month+"/"+startDay).getTime();
            }
        }
        for(int i=1;i<periodNumber;i++){
            timestampList.add(start+i*24*3600*1000L);
        }
        System.out.println(periodNumber+"================");
        return timestampList;
    }

    /**
     * 生成还款金额
     * max 最大限额
     * min 最小限额
     * pj 平均还款金额
     * n  还款次数
     */
    public static List<Long> createAmountList(Long amount,Long max,Long min,Long x){
        Long pj=amount/x;
        List<Long> amountList=new ArrayList<>();
        Long m=pj-min<max-pj?pj-min:max-pj;//最大值或最小值与平均值最小差
        Long n=m/2; //TODO
        Long ta=0L;
        Long sub=0L;
        for (int i=0;i<x;i++){
            if(i+1==x){
                //最后一笔
                amountList.add(amount-ta + 100);
                ta+=amount-ta;
            }else {
                Long a=PlanUtil.countAmount(pj,n,sub);
                sub=a-pj+sub;
                a=a/100*100;
                amountList.add(a + 100);
                ta+=a;
            }
        }
        return amountList;
    }

    //生成一天金额
    public static List<PlanDetailEntity> createOneDay(List<Long> repAmountList,int payNumber){
        List<PlanDetailEntity> pdList=new ArrayList<>();
        int repNumber=repAmountList.size();//还款次数
        List<Integer> numberList=getNumber(repNumber,payNumber);
        for (int i=0;i<repAmountList.size();i++){
            Long repAmount=repAmountList.get(i);
            //还款对应的消费次数
            int pn=numberList.get(i);
            pdList=countOneDayAmount(pdList,repAmount,pn);
        }
        return pdList;
    }
    //计算还款周期交易
    public static List<PlanDetailEntity> countOneDayAmount(List<PlanDetailEntity> pdList, Long repAmount, int pn){
        List<Long> payAmountList=countPayAmount(repAmount,pn);
        //
        for (Long a:payAmountList){
            PlanDetailEntity pdp=new PlanDetailEntity();
            pdp.setPayType("2");
            pdp.setArrivalAmount(a);
            pdList.add(pdp);
        }
        PlanDetailEntity pdr=new PlanDetailEntity();
        pdr.setPayType("1");
        pdr.setAmount(repAmount);
        pdList.add(pdr);
        return pdList;
    }
    /*//计算消费金额快付通
    public static List<Long> countPayAmount(Long repAmount,int pn){
        List<Long> payAmountList=new ArrayList<>();
        if (pn>2){//=3 对应1笔消费
            int r1=random(1,49);
            Long onePayAmount=repAmount*r1/100;
            payAmountList.add(onePayAmount);
            int r2=random(1,49);
            Long towPayAmount=repAmount*r2/100;
            payAmountList.add(towPayAmount);
            payAmountList.add(repAmount-onePayAmount-towPayAmount);
            return payAmountList;

        }else if (pn>1){//=2
            int r=random(1,49);
            Long onePayAmount=repAmount*r/100;
            payAmountList.add(onePayAmount);
            payAmountList.add(repAmount-onePayAmount);
            Collections.shuffle(payAmountList);
            return payAmountList;
        }else{//=1
            payAmountList.add(repAmount);
            return payAmountList;
        }
    }*/
    //计算消费金额通联
    public static List<Long> countPayAmount(Long repAmount,int pn){
        List<Long> payAmountList=new ArrayList<>();
        if (pn>2){//=3 对应1笔消费
            if(repAmount > 190000L){
                int firsySj = random(33,35);
                Long first = (repAmount * firsySj)/100;
                int secondSj = random(45,47);
                Long second = ((repAmount - first) * secondSj)/100;
                payAmountList.add(first);
                payAmountList.add(second);
                payAmountList.add(repAmount - first - second);
                Collections.shuffle(payAmountList);
                return payAmountList;
            }
            double t = 10000D/Double.parseDouble(repAmount+"");
            System.out.println("t==================================="+t);
            if(repAmount - 100000L > 0){
                double t1 = (repAmount - 100000L)/Double.parseDouble(repAmount+"");
                if(t1 > t){
                    t=t1;
                }
                System.out.println("t1==================================="+t1);
            }
            /*System.out.println("t==================================="+t);*/
            int minRandom = (int)((t + 0.01)*100);
            int maxRandom = (int)((0.99 - t)*100);
            System.out.println("minRandom"+minRandom);
            System.out.println("maxRandom"+maxRandom);
            int firsySj = random(minRandom,maxRandom);
            System.out.println("firsySj"+firsySj);
            System.out.println("firsySj/100"+Double.parseDouble(firsySj+"")/100D);
            Long first = (repAmount * firsySj)/100;
            System.out.println("first"+first);
            Long second = repAmount - first;
            System.out.println("second"+second);
            if(first - second > 0){
                Long first1 = (first * firsySj)/100;
                payAmountList.add(first1);
                payAmountList.add(second);
                payAmountList.add(first - first1);
                Collections.shuffle(payAmountList);
                return payAmountList;
            }
            payAmountList.add(first);
            Long second1 = (second * firsySj)/100;
            payAmountList.add(second1);
            payAmountList.add(second-second1);
            Collections.shuffle(payAmountList);
            return payAmountList;

        }else if (pn>1){//=2
            double t = 10000D/Double.parseDouble(repAmount+"");
            System.out.println("t==================================="+t);
            if(repAmount - 100000L > 0){
                double t1 = (repAmount - 100000L)/Double.parseDouble(repAmount+"");
                if(t1 > t){
                    t=t1;
                }
                System.out.println("t1==================================="+t1);
            }
            /*System.out.println("t==================================="+t);*/
            int minRandom = (int)((t + 0.01)*100);
            int maxRandom = (int)((0.99 - t)*100);
            System.out.println("minRandom"+minRandom);
            System.out.println("maxRandom"+maxRandom);
            int firsySj = random(minRandom,maxRandom);
            System.out.println("firsySj"+firsySj);
            System.out.println("firsySj/100"+Double.parseDouble(firsySj+"")/100D);
            Long first = (repAmount * firsySj)/100;
            System.out.println("first"+first);
            Long second = repAmount - first;
            System.out.println("second"+second);
            /*int r=random(1,49);
            Long onePayAmount=repAmount*r/100;*/
            payAmountList.add(first);
            payAmountList.add(repAmount-first);
            Collections.shuffle(payAmountList);
            return payAmountList;
        }else{//=1
            payAmountList.add(repAmount);
            return payAmountList;
        }
    }

    //计算支付笔数
    public static List<Integer> getNumber(int repNumber,int payNumber){//TODO
        List<Integer> numberList=new ArrayList<>();
        if(repNumber==1){
            numberList.add(payNumber);
            return numberList;
        }else {
            if (payNumber<4){//=3  //2,1   1,2
                int r=random(1,2);
                if (r==1){
                    numberList.add(1);
                    numberList.add(2);
                    return numberList;
                }else {
                    numberList.add(2);
                    numberList.add(1);
                    return numberList;
                }
            }else if(payNumber<5){//4   2,2
                numberList.add(2);
                numberList.add(2);
                return numberList;
            }else if(payNumber<6){//5   2,3  3,2
                int r=random(1,2);
                if (r==1){
                    numberList.add(3);
                    numberList.add(2);
                    return numberList;
                }else {
                    numberList.add(2);
                    numberList.add(3);
                    return numberList;
                }
            }else if (payNumber == 6){
                numberList.add(3);
                numberList.add(3);
                return numberList;
            }
        }
        return numberList;
    }

    //随机消费还款的时间点
    public static List<Integer> get(int count){
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        if(count == 8){
            return list;
        }
        Collections.shuffle(list);
        List<Integer> lists = new ArrayList<>();
        for(int i = 0;i < count;i++){
            int number = list.get(0);
            list.remove(0);
            lists.add(number);
        }
        Collections.sort(lists);
        return lists;
    }

    public static List<Long> randomTimeList(List<Long> timeList,int dayNum){
        List<Long> newTimeList=new ArrayList<>();
        for (int i=0;i<dayNum;i++){
            int r=random(1,timeList.size())-1;
            newTimeList.add(timeList.get(r));
            timeList.remove(r);
        }
        Collections.sort(newTimeList);
        return newTimeList;
    }

    public static void main(String[] args) throws ParseException {
        /*for(int i=0;i<100;i++){
            System.out.println(random(0,3));
        }
*/
        /*HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("merMp","13522337211");
        hashMap.put("identifying",275883);
        hashMap.put("appId","0000");
        hashMap.put("merChantId","M40873871193368576012620");
        String result=HttpClientUtil.doPost("http://47.104.106.175/xb-mer/MerChants/login",hashMap);
        System.out.println(result);*/

        /*double d = 88.88;
        long l = Math.round(d);
        System.out.println(l);*/
        /*createAmountList(200000000L,2379266L, 2000000L, 10L);*/
    }
}
