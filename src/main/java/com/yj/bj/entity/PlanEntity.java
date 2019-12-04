package com.yj.bj.entity;

import javax.persistence.*;

/**
 * Created by bin on 2017/11/6.
 * 还款计划
 */
@Table(name = "api_plan_easy")
public class PlanEntity implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pId", unique = true, nullable = false)
    private Long pId;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "startTime")
    private Long startTime;
    @Column(name = "finishTime")
    private Long finishTime;
    @Column(name = "state")
    private String state;//1草稿2执行中3已完成4中断5交易锁定
    @Column(name = "timeStampStr")
    private String timeStampStr;
    @Column(name = "lastDay")
    private Long lastDay;
    @Column(name = "ratio")
    private String ratio;
    @Column(name = "totalFee")
    private Long totalFee;
    @Column(name = "totalPayFee")
    private Long totalPayFee;
    @Column(name = "totalRepaymentFee")
    private Long totalRepaymentFee;
    @Column(name = "totalPayAmount")
    private Long totalPayAmount;
    @Column(name = "totalAmount")
    private Long totalAmount;
    @Column(name = "maxAmount")
    private Long maxAmount;
    @Column(name = "basicAmount")
    private Long basicAmount;
    @Column(name = "alreadyAmount")
    private Long alreadyAmount;

    @Column(name = "totalDay")
    private String totalDay;
    @Column(name = "number")
    private String number;
    @Column(name = "payNumber")
    private String payNumber;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "easyUserId")
    private String easyUserId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "bankCode")
    private String bankCode;
    @Column(name = "idCardNo")
    private String idCardNo;
    @Column(name = "cardNo")
    private String cardNo;
    @Column(name = "year")
    private String year;
    @Column(name = "month")
    private String month;
    @Column(name = "cvv2")
    private String cvv2;
    @Column(name = "bindId")
    private String bindId;

    @Column(name = "agentId")
    private String agentId;
    @Column(name = "institutionId")
    private String institutionId;
    @Column(name = "appId")
    private String appId;

    //落地
    @Column(name = "isLd")
    private String isLd;
    @Column(name = "aisleCode")
    private String aisleCode;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;

    @Column(name = "cardNumber")
    private String cardNumber;
    @Column(name = "remarks")
    private String remarks;

    public Long getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(Long totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public String getIsLd() {
        return isLd;
    }

    public void setIsLd(String isLd) {
        this.isLd = isLd;
    }

    public String getAisleCode() {
        return aisleCode;
    }

    public void setAisleCode(String aisleCode) {
        this.aisleCode = aisleCode;
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

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimeStampStr() {
        return timeStampStr;
    }

    public void setTimeStampStr(String timeStampStr) {
        this.timeStampStr = timeStampStr;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(String totalDay) {
        this.totalDay = totalDay;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getBasicAmount() {
        return basicAmount;
    }

    public void setBasicAmount(Long basicAmount) {
        this.basicAmount = basicAmount;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Long getTotalPayFee() {
        return totalPayFee;
    }

    public void setTotalPayFee(Long totalPayFee) {
        this.totalPayFee = totalPayFee;
    }

    public Long getTotalRepaymentFee() {
        return totalRepaymentFee;
    }

    public void setTotalRepaymentFee(Long totalRepaymentFee) {
        this.totalRepaymentFee = totalRepaymentFee;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public Long getLastDay() {
        return lastDay;
    }

    public void setLastDay(Long lastDay) {
        this.lastDay = lastDay;
    }

    public Long getAlreadyAmount() {
        return alreadyAmount;
    }

    public void setAlreadyAmount(Long alreadyAmount) {
        this.alreadyAmount = alreadyAmount;
    }
}
