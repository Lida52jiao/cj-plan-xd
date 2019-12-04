package com.yj.bj.util;


/**
 * Created by bin on 2017/11/14.
 */
public class OrderUtil {
    public static String createOrderNo(SnowflakeIdWorker idWorker){
        return createOrderNo(idWorker,"P");
    }
    public static String createOrderNo(SnowflakeIdWorker idWorker , String type){
        return type+idWorker.nextId();
    }
}
