package com.yj.bj.entity.user;






import com.yj.bj.entity.PlanEntity;

import java.io.Serializable;

public class CardInformation implements Serializable {

	private Long cardId;
	private String merChantId;
	private String cardType;
	private String cardNumber;
	private String merMp;
	private String issuingBank;
	private String statementDate;//账单日
	private String repaymentDate;//还款日
	private String cv2;
	private String effectiveYear;
	private String effectiveMonth;
	private String cardDefault;
	private String creatDate;
	private String bindId;
	private String remarks;

	private PlanEntity plan;

	public PlanEntity getPlan() {
		return plan;
	}

	public void setPlan(PlanEntity plan) {
		this.plan = plan;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getMerChantId() {
		return merChantId;
	}

	public void setMerChantId(String merChantId) {
		this.merChantId = merChantId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getMerMp() {
		return merMp;
	}

	public void setMerMp(String merMp) {
		this.merMp = merMp;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	public String getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public String getCv2() {
		return cv2;
	}

	public void setCv2(String cv2) {
		this.cv2 = cv2;
	}

	public String getEffectiveYear() {
		return effectiveYear;
	}

	public void setEffectiveYear(String effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	public String getEffectiveMonth() {
		return effectiveMonth;
	}

	public void setEffectiveMonth(String effectiveMonth) {
		this.effectiveMonth = effectiveMonth;
	}

	public String getCardDefault() {
		return cardDefault;
	}

	public void setCardDefault(String cardDefault) {
		this.cardDefault = cardDefault;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
