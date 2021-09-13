package com.zebone.nhis.pro.zsba.mz.datsett.vo;

public class ZsbaOpSettleBackInvDtl {
	
	//姓名
	private String namePi;
	
	//发票号
	private String codeInv;
	
	//流水票
	private String no;

	//金额
	private String amountInv;
	
	//实收金额
	private String amount;
	
	//支付方式
	private String payName;
	
	private String reStAmount;
	
	public String getReStAmount() {
		return reStAmount;
	}

	public void setReStAmount(String reStAmount) {
		this.reStAmount = reStAmount;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getNameEmpInv() {
		return nameEmpInv;
	}

	public void setNameEmpInv(String nameEmpInv) {
		this.nameEmpInv = nameEmpInv;
	}

	private String nameEmpInv;

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getCodeInv() {
		return codeInv;
	}

	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getAmountInv() {
		return amountInv;
	}

	public void setAmountInv(String amountInv) {
		this.amountInv = amountInv;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
