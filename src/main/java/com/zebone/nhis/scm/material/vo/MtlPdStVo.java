package com.zebone.nhis.scm.material.vo;

import java.util.Date;

import com.zebone.nhis.common.module.scm.st.PdSt;

public class MtlPdStVo extends PdSt{
	//仓库名称
    private String storeStName;
    //供应商名称
    private String supplyerName;
    
    private String pkDeptLk;
    
    private String deptLkName;
    
    private String nameEmpOp;
    
    private Double amount;
    
    private Date dateChkRpt;
    
    private String nameEmpChkRpt;
    
	public String getStoreStName() {
		return storeStName;
	}
	public void setStoreStName(String storeStName) {
		this.storeStName = storeStName;
	}
	public String getSupplyerName() {
		return supplyerName;
	}
	public void setSupplyerName(String supplyerName) {
		this.supplyerName = supplyerName;
	}
	public String getPkDeptLk() {
		return pkDeptLk;
	}
	public void setPkDeptLk(String pkDeptLk) {
		this.pkDeptLk = pkDeptLk;
	}
	public String getDeptLkName() {
		return deptLkName;
	}
	public void setDeptLkName(String deptLkName) {
		this.deptLkName = deptLkName;
	}
	public String getNameEmpOp() {
		return nameEmpOp;
	}
	public void setNameEmpOp(String nameEmpOp) {
		this.nameEmpOp = nameEmpOp;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getDateChkRpt() {
		return dateChkRpt;
	}
	public void setDateChkRpt(Date dateChkRpt) {
		this.dateChkRpt = dateChkRpt;
	}
	public String getNameEmpChkRpt() {
		return nameEmpChkRpt;
	}
	public void setNameEmpChkRpt(String nameEmpChkRpt) {
		this.nameEmpChkRpt = nameEmpChkRpt;
	}

}
