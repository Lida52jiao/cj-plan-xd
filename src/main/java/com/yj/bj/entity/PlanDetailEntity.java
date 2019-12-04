package com.yj.bj.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 还款计划详情
 */
@Table(name = "api_plan_detail_easy")
public class PlanDetailEntity implements java.io.Serializable{
    @Id
    @Column(name = "orderNo", unique = true, nullable = false)
    private String orderNo;//单号
    @Column(name = "planId")
    private Long planId;//计划ID
    @Column(name = "state")
    private String state;//发起状态
    @Column(name = "payState")
    private String payState;//支付状态
    @Column(name = "repaymentState")
    private String repaymentState;//还款状态
    @Column(name = "repaymentOrderExpect")
    private String repaymentOrderExpect;//计划还款订单
    @Column(name = "repaymentOrderReality")
    private String repaymentOrderReality;//实际还款订单
    @Column(name = "isAnew")
    private String isAnew;//是否补单
    @Column(name = "anewOrderNo")
    private String anewOrderNo;//补单单号

    @Column(name = "isShare")
    private String isShare;
    @Column(name = "day")
    private Integer day;
    @Column(name = "time")
    private Integer time;
    @Column(name = "number")
    private Integer number;
    @Column(name = "payType")
    private String payType;//1还款2消费
    @Column(name = "amount")
    private Long amount;//订单金额
    @Column(name = "arrivalAmount")
    private Long arrivalAmount;//到账金额
    @Column(name = "fee")
    private Long fee;//手续费
    @Column(name = "payFee")
    private Long payFee;//支付手续费
    @Column(name = "repaymentFee")
    private Long repaymentFee;//还款手续费
    //时间
    @Column(name = "createTime")
    private String createTime;//创建时间
    @Column(name = "executeTime")
    private Long executeTime;//执行时间 时间戳
    @Column(name = "finishTime")
    private Long finishTime;//完成时间 时间戳
    //用户信息
    @Column(name = "merchantId")
    private String merchantId;//商户号
    @Column(name = "easyUserId")
    private String easyUserId;//易生商户号
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    //支付需要的信息
    @Column(name = "bankCode")
    private String bankCode;//银行编号
    @Column(name = "idCardNo")
    private String idCardNo;//身份证
    @Column(name = "cardNo")
    private String cardNo;//卡号
    @Column(name = "year")
    private String year;//有效期年
    @Column(name = "month")
    private String month;//有效期月
    @Column(name = "cvv2")
    private String cvv2;
    @Column(name = "bindId")
    private String bindId;//绑卡ID

    @Column(name = "agentId")
    private String agentId;//代理商号
    @Column(name = "institutionId")
    private String institutionId;//机构号
    @Column(name = "appId")
    private String appId;//APPID
    //落地
    @Column(name = "isLd")
    private String isLd;
    @Column(name = "aisleCode")
    private String aisleCode;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "mcc")
    private String mcc;
    //还款周期ID
    @Column(name = "cycleId")
    private String cycleId;
    @Column(name = "aisleCycleId")
    private String aisleCycleId;
    //费率信息
    @Column(name = "rate")
    private String rate;
    @Column(name = "d0Fee")
    private String d0Fee;

    @Column(name = "rateType")
    private String rateType;

    @Column(name = "batch")
    private String batch;
    @Column(name = "merProvince")
    private String merProvince;
    @Column(name = "merCity")
    private String merCity;
    @Column(name = "merCounty")
    private String merCounty;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "cause")
    private String cause;
    @Column(name ="executeDateStr")
    private String executeDateStr;//执行日期字符串

    @Column(name = "requestMsg")
    private String requestMsg;

    @Column(name ="responseMsg")
    private String responseMsg;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getRepaymentState() {
        return repaymentState;
    }

    public void setRepaymentState(String repaymentState) {
        this.repaymentState = repaymentState;
    }

    public String getRepaymentOrderExpect() {
        return repaymentOrderExpect;
    }

    public void setRepaymentOrderExpect(String repaymentOrderExpect) {
        this.repaymentOrderExpect = repaymentOrderExpect;
    }

    public String getRepaymentOrderReality() {
        return repaymentOrderReality;
    }

    public void setRepaymentOrderReality(String repaymentOrderReality) {
        this.repaymentOrderReality = repaymentOrderReality;
    }

    public String getIsAnew() {
        return isAnew;
    }

    public void setIsAnew(String isAnew) {
        this.isAnew = isAnew;
    }

    public String getAnewOrderNo() {
        return anewOrderNo;
    }

    public void setAnewOrderNo(String anewOrderNo) {
        this.anewOrderNo = anewOrderNo;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(Long arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getPayFee() {
        return payFee;
    }

    public void setPayFee(Long payFee) {
        this.payFee = payFee;
    }

    public Long getRepaymentFee() {
        return repaymentFee;
    }

    public void setRepaymentFee(Long repaymentFee) {
        this.repaymentFee = repaymentFee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getEasyUserId() {
        return easyUserId;
    }

    public void setEasyUserId(String easyUserId) {
        this.easyUserId = easyUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getIsLd() {
        return isLd;
    }

    public void setIsLd(String isLd) {
        this.isLd = isLd;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getD0Fee() {
        return d0Fee;
    }

    public void setD0Fee(String d0Fee) {
        this.d0Fee = d0Fee;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getAisleCode() {
        return aisleCode;
    }

    public void setAisleCode(String aisleCode) {
        this.aisleCode = aisleCode;
    }

    public String getAisleCycleId() {
        return aisleCycleId;
    }

    public void setAisleCycleId(String aisleCycleId) {
        this.aisleCycleId = aisleCycleId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getMerProvince() {
        return merProvince;
    }

    public void setMerProvince(String merProvince) {
        this.merProvince = merProvince;
    }

    public String getMerCity() {
        return merCity;
    }

    public void setMerCity(String merCity) {
        this.merCity = merCity;
    }

    public String getMerCounty() {
        return merCounty;
    }

    public void setMerCounty(String merCounty) {
        this.merCounty = merCounty;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getExecuteDateStr() {
        return executeDateStr;
    }

    public void setExecuteDateStr(String executeDateStr) {
        this.executeDateStr = executeDateStr;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
