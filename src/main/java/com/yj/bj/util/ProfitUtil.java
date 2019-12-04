package com.yj.bj.util;

/**
 * Created by bin on 2018/1/26.
 */
public class ProfitUtil {
    public static Double fmtPayRate(String userPayRate){
        return new Double(userPayRate)/100;
    }
    public static Long fmtRepaymentFee(String userRepaymentFee){
        return (long)(Double.parseDouble(userRepaymentFee)*100D);
    }
    public static long countPayFee(Long amount,Double rate){
        return (long)Math.ceil(rate*amount);
    }
    public static long countPayFee(Long amount,String userPayRate){
        return (long)(countPayFee(amount,fmtPayRate(userPayRate)));
    }
    public static long countPayAmount(Long amount,Double rate){
        return (long)Math.ceil(amount/(1D-rate));
    }
    public static long countPayAmount(Long amount,String userPayRate){
        return countPayAmount(amount,fmtPayRate(userPayRate));
    }
    public static long countRepaymentAmount(Long amount,String userRepaymentFee){
        return amount+(long)(Double.parseDouble(userRepaymentFee)*100D);
    }

    public static void main(String[] args) {
        Long a=1111111L;
        System.out.println(countPayFee(a,"0.85"));
        System.out.println(Math.ceil(2.2));
    }
}
