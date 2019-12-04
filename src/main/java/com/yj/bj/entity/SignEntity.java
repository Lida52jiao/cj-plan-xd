package com.yj.bj.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by bin on 2018/4/8.
 */
@Table(name = "cj_sign_xd")
public class SignEntity implements java.io.Serializable{
   
	@Id
    @Column(name = "orderNo", unique = true, nullable = false)
    private String orderNo;//单号

	@Column(name = "aisleMerId")
	private String aisleMerId;
	@Column(name = "signId")
	private String signId;


    @Column(name = "createTime")
    private String createTime;

	@Column(name = "signState")
	private String signState;

    @Column(name = "institutionId")
    private String institutionId;//机构号
    @Column(name = "agentId")
    private String agentId;
    @Column(name = "appId")
    private String appId;//APPID

    @Column(name = "merchantId")
    private String merchantId;//商户号

	@Column(name = "name")
	private String name;
	@Column(name = "idCard")
	private String idCard;
	@Column(name = "creditCardNumber")
	private String creditCardNumber;
	@Column(name = "creditCardCode")
	private String creditCardCode;
	@Column(name = "creditPhone")
	private String creditPhone;
	@Column(name = "CardValidDate")
	private String CardValidDate;
	@Column(name = "cv2")
	private String cv2;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "bankType")
	private String bankType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAisleMerId() {
		return aisleMerId;
	}

	public void setAisleMerId(String aisleMerId) {
		this.aisleMerId = aisleMerId;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardCode() {
		return creditCardCode;
	}

	public void setCreditCardCode(String creditCardCode) {
		this.creditCardCode = creditCardCode;
	}

	public String getCreditPhone() {
		return creditPhone;
	}

	public void setCreditPhone(String creditPhone) {
		this.creditPhone = creditPhone;
	}

	public String getCardValidDate() {
		return CardValidDate;
	}

	public void setCardValidDate(String cardValidDate) {
		CardValidDate = cardValidDate;
	}

	public String getCv2() {
		return cv2;
	}

	public void setCv2(String cv2) {
		this.cv2 = cv2;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
}
