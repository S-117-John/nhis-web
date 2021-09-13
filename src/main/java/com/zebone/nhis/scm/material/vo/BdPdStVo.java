package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdStVo {
	
	//库单主键
	private String pkPdst;
	
	//所属机构
	private String pkOrg;
	
	//库单部门
	private String pkDeptSt;
	
	//仓库
	private String pkStoreSt;
	
	//业务类型
	private String dtSttype;
	
	//库单号
	private String codeSt;
	
	//库单名称
	private String nameSt;
	
	//库单名称
	private String euDirect;
	
	//库单日期
	private Date dateSt;
	
	//库单状态
	private String euStatus;
	
	//对应库存机构
	private String pkOrgLk;
	
	//目的库存部门
	private String pkDeptLk;
	
	//目的库存仓库
	private String pkStoreLk;
	
	//供应商
	private String pkSupplyer;
	
	//供应商名称
	private String supplyerName;
	
	//采购订单
	private String pkPdpu;
	
	//请领单
	private String pkPdPlan;
	
	//发票号
	private String receiptNo;
	
	//付款完成标志
	private String flagPay;
	
	//经办人
	private String pkEmpOp;
	
	//经办人姓名
	private String nameEmpOp;
	
	//审核标志
	private String flagChk;
	
	//审核人
	private String pkEmpChk;
	
	//审核人姓名
	private String nameEmpChk;
	
	//审核时间
	private Date DateChk;
	
	//备注
	private String note;
	
	//源单据主键
	private String pkPdstSr;
	
	//采购标志
	private String flagPu;
	
	//删除标志
	private String delFlag;
	

	public String getSupplyerName() {
		return supplyerName;
	}

	public void setSupplyerName(String supplyerName) {
		this.supplyerName = supplyerName;
	}

	public String getPkPdst() {
		return pkPdst;
	}

	public void setPkPdst(String pkPdst) {
		this.pkPdst = pkPdst;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getPkStoreSt() {
		return pkStoreSt;
	}

	public void setPkStoreSt(String pkStoreSt) {
		this.pkStoreSt = pkStoreSt;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getNameSt() {
		return nameSt;
	}

	public void setNameSt(String nameSt) {
		this.nameSt = nameSt;
	}

	public String getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkOrgLk() {
		return pkOrgLk;
	}

	public void setPkOrgLk(String pkOrgLk) {
		this.pkOrgLk = pkOrgLk;
	}

	public String getPkDeptLk() {
		return pkDeptLk;
	}

	public void setPkDeptLk(String pkDeptLk) {
		this.pkDeptLk = pkDeptLk;
	}

	public String getPkStoreLk() {
		return pkStoreLk;
	}

	public void setPkStoreLk(String pkStoreLk) {
		this.pkStoreLk = pkStoreLk;
	}

	public String getPkSupplyer() {
		return pkSupplyer;
	}

	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}

	public String getPkPdpu() {
		return pkPdpu;
	}

	public void setPkPdpu(String pkPdpu) {
		this.pkPdpu = pkPdpu;
	}

	public String getPkPdPlan() {
		return pkPdPlan;
	}

	public void setPkPdPlan(String pkPdPlan) {
		this.pkPdPlan = pkPdPlan;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getFlagPay() {
		return flagPay;
	}

	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}

	public String getPkEmpOp() {
		return pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {
		this.pkEmpOp = pkEmpOp;
	}

	public String getNameEmpOp() {
		return nameEmpOp;
	}

	public void setNameEmpOp(String nameEmpOp) {
		this.nameEmpOp = nameEmpOp;
	}

	public String getFlagChk() {
		return flagChk;
	}

	public void setFlagChk(String flagChk) {
		this.flagChk = flagChk;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public Date getDateChk() {
		return DateChk;
	}

	public void setDateChk(Date dateChk) {
		DateChk = dateChk;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkPdstSr() {
		return pkPdstSr;
	}

	public void setPkPdstSr(String pkPdstSr) {
		this.pkPdstSr = pkPdstSr;
	}

	public String getFlagPu() {
		return flagPu;
	}

	public void setFlagPu(String flagPu) {
		this.flagPu = flagPu;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
