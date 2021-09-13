package com.zebone.nhis.bl.pub.syx.vo;

import java.util.Date;

public class InvVo {
	 
    // PK
	private String pkInvoice;
     
    // 发票日期
    private Date dateInv;
     
    // 金额
    private Double amountInv;
     
    // 发票号码
    private String codeInv;
     
    // 作废标志
    private String flagCancel;
     
    // 结账标志
    private String flagCc;

     
    // 新发票号
    private String newCodeInv;

     
    // 作废名称
    private String nameCancel;

     
    // 结账名称
    private String nameCc;
    
    // 新票据开始号
    private String newBegin;
    
    // 新票据结束号
    private String newEnd;

	public String getNewBegin() {
		return newBegin;
	}


	public void setNewBegin(String newBegin) {
		this.newBegin = newBegin;
	}


	public String getNewEnd() {
		return newEnd;
	}


	public void setNewEnd(String newEnd) {
		this.newEnd = newEnd;
	}


	public String getPkInvoice() {
		return pkInvoice;
	}


	public void setPkInvoice(String pkInvoice) {
		this.pkInvoice = pkInvoice;
	}


	public Date getDateInv() {
		return dateInv;
	}


	public void setDateInv(Date dateInv) {
		this.dateInv = dateInv;
	}


	public Double getAmountInv() {
		return amountInv;
	}


	public void setAmountInv(Double amountInv) {
		this.amountInv = amountInv;
	}


	public String getCodeInv() {
		return codeInv;
	}


	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}


	public String getFlagCancel() {
		return flagCancel;
	}


	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}


	public String getFlagCc() {
		return flagCc;
	}


	public void setFlagCc(String flagCc) {
		this.flagCc = flagCc;
	}


	public String getNewCodeInv() {
		return newCodeInv;
	}


	public void setNewCodeInv(String newCodeInv) {
		this.newCodeInv = newCodeInv;
	}


	public String getNameCancel() {
		return nameCancel;
	}


	public void setNameCancel(String nameCancel) {
		this.nameCancel = nameCancel;
	}


	public String getNameCc() {
		return nameCc;
	}


	public void setNameCc(String nameCc) {
		this.nameCc = nameCc;
	}
    
}
