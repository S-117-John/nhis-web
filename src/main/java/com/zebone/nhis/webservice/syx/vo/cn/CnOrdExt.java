package com.zebone.nhis.webservice.syx.vo.cn;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnOrdExt extends CnOrder {
	private String func_id;
	private String id;
	private String codePi;
	private String codePv;
    private String codeDept;
    private String codeDr;
    private String codeDeptNs;
    private String codeDeptEx;

    private String unit;
	private String dtBtAbo;
	private String dtBttype;
	private String dtBtRh;
	private String btContent;
    
	
	
	
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getFunc_id() {
		return func_id;
	}
	public void setFunc_id(String func_id) {
		this.func_id = func_id;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getCodeDr() {
		return codeDr;
	}
	public void setCodeDr(String codeDr) {
		this.codeDr = codeDr;
	}
	public String getCodeDeptNs() {
		return codeDeptNs;
	}
	public void setCodeDeptNs(String codeDeptNs) {
		this.codeDeptNs = codeDeptNs;
	}
	public String getCodeDeptEx() {
		return codeDeptEx;
	}
	public void setCodeDeptEx(String codeDeptEx) {
		this.codeDeptEx = codeDeptEx;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDtBtAbo() {
		return dtBtAbo;
	}
	public void setDtBtAbo(String dtBtAbo) {
		this.dtBtAbo = dtBtAbo;
	}
	public String getDtBttype() {
		return dtBttype;
	}
	public void setDtBttype(String dtBttype) {
		this.dtBttype = dtBttype;
	}
	public String getDtBtRh() {
		return dtBtRh;
	}
	public void setDtBtRh(String dtBtRh) {
		this.dtBtRh = dtBtRh;
	}
	public String getBtContent() {
		return btContent;
	}
	public void setBtContent(String btContent) {
		this.btContent = btContent;
	}
    
    
}
