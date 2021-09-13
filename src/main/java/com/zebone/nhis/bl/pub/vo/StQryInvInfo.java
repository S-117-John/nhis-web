package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

public class StQryInvInfo {

	 private String pkInvoice ; // --发票主键
     private String codeInv ; //   --发票号码
     private Date dateInv ; //   --发票日期
     private BigDecimal amountInv ; // --发票金额
     private BigDecimal amountPi ; //  --自付金额
     private String nameEmpInv ; // --开票人
     private String flagCancel ; //  --作废标志
     private Date dateCancel ; //  --作废日期
     private String nameEmpCancel ;  // --作废人
     
     private String ebillbatchcode; // --发票主键
     private String ebillno; //   --发票号码
     private String ebillbatchcodeCancel; // --发票主键
     private String ebillnoCancel; //   --发票号码
     private String checkcode;//电子票据校验码
     private byte[] qrcodeEbill;//电子票据二维码
     private byte[] qrcodeEbillCancel;
     private String urlNetebill;//电子票据H5外网地址
     private String urlEbill;//电子票据H5内网地址

	 private String billbatchcode;//纸质票据代码
	 private String codeSn;//发票序列
     
	public byte[] getQrcodeEbillCancel() {
		return qrcodeEbillCancel;
	}
	public void setQrcodeEbillCancel(byte[] qrcodeEbillCancel) {
		this.qrcodeEbillCancel = qrcodeEbillCancel;
	}
	public String getCheckcode() {
		return checkcode;
	}
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	public byte[] getQrcodeEbill() {
		return qrcodeEbill;
	}
	public void setQrcodeEbill(byte[] qrcodeEbill) {
		this.qrcodeEbill = qrcodeEbill;
	}
	public String getEbillbatchcode() {
		return ebillbatchcode;
	}
	public void setEbillbatchcode(String ebillbatchcode) {
		this.ebillbatchcode = ebillbatchcode;
	}
	public String getEbillno() {
		return ebillno;
	}
	public void setEbillno(String ebillno) {
		this.ebillno = ebillno;
	}
	public String getEbillbatchcodeCancel() {
		return ebillbatchcodeCancel;
	}
	public void setEbillbatchcodeCancel(String ebillbatchcodeCancel) {
		this.ebillbatchcodeCancel = ebillbatchcodeCancel;
	}
	public String getEbillnoCancel() {
		return ebillnoCancel;
	}
	public void setEbillnoCancel(String ebillnoCancel) {
		this.ebillnoCancel = ebillnoCancel;
	}
	public String getPkInvoice() {
		return pkInvoice;
	}
	public void setPkInvoice(String pkInvoice) {
		this.pkInvoice = pkInvoice;
	}
	public String getCodeInv() {
		return codeInv;
	}
	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
	public Date getDateInv() {
		return dateInv;
	}
	public void setDateInv(Date dateInv) {
		this.dateInv = dateInv;
	}
	public BigDecimal getAmountInv() {
		return amountInv;
	}
	public void setAmountInv(BigDecimal amountInv) {
		this.amountInv = amountInv;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public String getNameEmpInv() {
		return nameEmpInv;
	}
	public void setNameEmpInv(String nameEmpInv) {
		this.nameEmpInv = nameEmpInv;
	}
	public String getFlagCancel() {
		return flagCancel;
	}
	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}
	public Date getDateCancel() {
		return dateCancel;
	}
	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}
	public String getNameEmpCancel() {
		return nameEmpCancel;
	}
	public void setNameEmpCancel(String nameEmpCancel) {
		this.nameEmpCancel = nameEmpCancel;
	}
	public String getUrlNetebill() {
		return urlNetebill;
	}
	public void setUrlNetebill(String urlNetebill) {
		this.urlNetebill = urlNetebill;
	}
	public String getUrlEbill() {
		return urlEbill;
	}
	public void setUrlEbill(String urlEbill) {
		this.urlEbill = urlEbill;
	}

	public String getBillbatchcode() {
		return billbatchcode;
	}

	public void setBillbatchcode(String billbatchcode) {
		this.billbatchcode = billbatchcode;
	}

	public String getCodeSn() {
		return codeSn;
	}

	public void setCodeSn(String codeSn) {
		this.codeSn = codeSn;
	}
}
