package com.yj.bj.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by bin on 2018/9/5.
 */
@Table(name = "cj_bind_xd")
public class BindEntity implements java.io.Serializable{
    @Id
    @Column(name = "orderNo", unique = true, nullable = false)
    private String orderNo;
    @Column(name = "createTime")
    private String createTime;

    @Column(name ="state")
    private String state;

    @Column(name = "merchantId")
    private String merchantId;//
    @Column(name = "agentId")
    private String agentId;//代理商号
    @Column(name = "institutionId")
    private String institutionId;//机构号
    @Column(name = "appId")
    private String appId;//APPID

    @Column(name ="cardNo")
    private String cardNo;
    @Column(name ="name")
    private String name;
    @Column(name ="phone")
    private String phone;
    @Column(name ="idCardNo")
    private String idCardNo;
    @Column(name ="aisleMerId")
    private String aisleMerId;
    @Column(name ="remark")
    private String remark;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getAisleMerId() {
        return aisleMerId;
    }

    public void setAisleMerId(String aisleMerId) {
        this.aisleMerId = aisleMerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
