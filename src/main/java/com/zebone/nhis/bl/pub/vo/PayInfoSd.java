package com.zebone.nhis.bl.pub.vo;

/**
 * 收款信息(深大用)
 * @author Administrator
 *
 */
public class PayInfoSd {
	
	public PayInfoSd(){
		
	}
	
	public PayInfoSd(String payType,String payName,Double debitAmt,Double creditAmt){
		this.payType = payType;
		this.payName = payName;
		this.debitAmt = debitAmt;
		this.creditAmt = creditAmt;
	}
	
	private String payType;
	
	//会计科目
	private String payName;
	
	//借方金额
	private Double debitAmt;
	
	//贷方金额
	private Double creditAmt;

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public Double getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(Double debitAmt) {
		this.debitAmt = debitAmt;
	}

	public Double getCreditAmt() {
		return creditAmt;
	}

	public void setCreditAmt(Double creditAmt) {
		this.creditAmt = creditAmt;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}
