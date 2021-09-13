package com.zebone.nhis.bl.pub.syx.vo;

import java.math.BigDecimal;
import java.util.Date;

public class BlSettleRefundVo {
    private String pkSettle; 
    private String pkPi;//
	private String codeSt;  
	private Date dateSt;  
	private BigDecimal amountSt; 
	private BigDecimal amountPi;
	private BigDecimal amountInsu;
	private String dtPaymode;
	private String namePaymode;//支付方式
	private String nameEmpSt;  
	private String codeInv;  
	private BigDecimal amountInv;
	private String pkPv;
	
	
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public BigDecimal getAmountInsu() {
		return amountInsu;
	}
	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public BigDecimal getAmountSt() {
		return amountSt;
	}
	public BigDecimal getAmountInv() {
		return amountInv;
	}
	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}
	public void setAmountInv(BigDecimal amountInv) {
		this.amountInv = amountInv;
	}
	public String getNamePaymode() {
		return namePaymode;
	}
	public void setNamePaymode(String namePaymode) {
		this.namePaymode = namePaymode;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getCodeSt() {
		return codeSt;
	}
	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}
	public Date getDateSt() {
		return dateSt;
	}
	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}
	public String getDtPaymode() {
		return dtPaymode;
	}
	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getNameEmpSt() {
		return nameEmpSt;
	}
	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}
	public String getCodeInv() {
		return codeInv;
	}
	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
	
	
}
