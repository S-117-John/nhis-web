package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

public class SettleRecord {
	
	 private  String pkSettle; 
     private  String euPvtype ;
     private   String sttype ;
     private  String dtSttype; 
     private  Date dateSt;   
     private  String codePi ;  
     private  String namePi  ; 
     private  String hp;
     private  BigDecimal amountSt; 
     private  BigDecimal amountPi;
     private  String nameDept;   
     private  String nameEmpSt; 
     private  String euStresult;   
     private  String reasonCanc;
     private  String pkPv;
     private Date dateBegin;	//就诊时间
     private String codeIp;	//住院号
     private String codeOp;
	private String codePv;
	private String codeSt;
	private String oldCodeSt;//原结算号，退费记录上需要显示
	private String oldAmountSt;//原结算金额，退费记录上需要显示
	private String pkSettleRecharge;//部分退，重新收的pkSettle（记录在退费数据中）
	private String pkInsu;
	private String flagUpload;//是否已经上传结算单，灵璧使用
	private String printState;//是否打印

	public String getPrintState() {
		return printState;
	}

	public void setPrintState(String printState) {
		this.printState = printState;
	}

	public String getFlagUpload() {
		return flagUpload;
	}

	public void setFlagUpload(String flagUpload) {
		this.flagUpload = flagUpload;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getOldCodeSt() {
		return oldCodeSt;
	}

	public void setOldCodeSt(String oldCodeSt) {
		this.oldCodeSt = oldCodeSt;
	}

	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getSttype() {
		return sttype;
	}
	public void setSttype(String sttype) {
		this.sttype = sttype;
	}
	public String getDtSttype() {
		return dtSttype;
	}
	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}
	public Date getDateSt() {
		return dateSt;
	}
	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public BigDecimal getAmountSt() {
		return amountSt;
	}
	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameEmpSt() {
		return nameEmpSt;
	}
	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}
	public String getEuStresult() {
		return euStresult;
	}
	public void setEuStresult(String euStresult) {
		this.euStresult = euStresult;
	}
	public String getReasonCanc() {
		return reasonCanc;
	}
	public void setReasonCanc(String reasonCanc) {
		this.reasonCanc = reasonCanc;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getOldAmountSt() {
		return oldAmountSt;
	}

	public void setOldAmountSt(String oldAmountSt) {
		this.oldAmountSt = oldAmountSt;
	}

	public String getPkSettleRecharge() {
		return pkSettleRecharge;
	}

	public void setPkSettleRecharge(String pkSettleRecharge) {
		this.pkSettleRecharge = pkSettleRecharge;
	}
	
}
