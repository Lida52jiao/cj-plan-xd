package com.yj.bj.mapper;

import com.yj.bj.entity.PlanDetailEntity;
import com.yj.bj.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bin on 2017/11/6.
 */
public interface PlanDetailMapper extends MyMapper<PlanDetailEntity> {
    List<PlanDetailEntity> getExpireList(@Param("newDate") Long newDate, @Param("startTime") Long startTime, @Param("state") String state);
    List<PlanDetailEntity> getTodayExpireList(@Param("finishTime") Long finishTime, @Param("startTime") Long startTime, @Param("state") String state);
    List<PlanDetailEntity> getToDayList(@Param("newTime") Long newTime, @Param("zeroTime") Long zeroTime);
    List<PlanDetailEntity> freeFindOrderList(@Param("planId") Long planId,
                                             @Param("isLd") String isLd,
                                             @Param("aisleCode") String aisleCode,
                                             @Param("merchantId") String merchantId,
                                             @Param("institutionId") String institutionId,
                                             @Param("agentId") String agentId,
                                             @Param("appId") String appId,
                                             @Param("orderNo") String orderNo,
                                             @Param("phone") String phone,
                                             @Param("cardNo") String cardNo,
                                             @Param("payType") String payType,
                                             @Param("state") String state,
                                             @Param("payState") String payState,
                                             @Param("repaymentState") String repaymentState,
                                             @Param("startTime") Long startTime,
                                             @Param("endTime") Long endTime);
}
