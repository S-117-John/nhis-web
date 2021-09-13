package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdReceiveVo {
	
    // 领用科室主键
    private String pkDeptLk;

    // 科室名称
    private String deptName;

    // 物品名称
    private String name;

    // 规格
    private String spec;

    // 生产厂家主键
    private String pkFactory;

    // 生产厂家名称
    private String factoryName;

    // 单位主键
    private String pkUnitPack;

    // 单位名称
    private String unitName;

    // 数量
    private Double quanPack;

    // 成本金额
    private Double amountCost;

    // 零售金额
    private Double amount;

    // 领用日期
    private Date dateChk;

    // 库单主键
    private String pkPdst;
    
    //业务类型
    private String dtSttype;
    
    //库单号
    private String codeSt;

    //库单日期
    private Date dateSt;
    
    //申请机构
    private String pkOrgLk;
    
    //库单状态
    private String euStatus;
    
    //经办人
    private String pkEmpOp;
    
    //经办人姓名
    private String nameEmpOp;
    
    //审核人
    private String pkEmpChk;
    
    //审核人姓名
    private String nameEmpChk;
    
    //备注
    private String note;
    
    //申领仓库
    private String pkStoreLk;
    
    
    
    
    
	public String getPkStoreLk() {
		return pkStoreLk;
	}

	public void setPkStoreLk(String pkStoreLk) {
		this.pkStoreLk = pkStoreLk;
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

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getPkOrgLk() {
		return pkOrgLk;
	}

	public void setPkOrgLk(String pkOrgLk) {
		this.pkOrgLk = pkOrgLk;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkDeptLk() {
		return pkDeptLk;
	}

	public void setPkDeptLk(String pkDeptLk) {
		this.pkDeptLk = pkDeptLk;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkFactory() {
		return pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}

	public Double getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(Double amountCost) {
		this.amountCost = amountCost;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getPkPdst() {
		return pkPdst;
	}

	public void setPkPdst(String pkPdst) {
		this.pkPdst = pkPdst;
	}
	
}
