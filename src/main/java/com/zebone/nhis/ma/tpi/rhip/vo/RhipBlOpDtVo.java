package com.zebone.nhis.ma.tpi.rhip.vo;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

/**
 * 费用明细VO
 * @author chengjia
 *
 */
public class RhipBlOpDtVo extends BlOpDt{
	
	private String cateCode;
	private String cateName;
	private String unitCode;
	private String unitName;
	private String pkSettle;
	private String codeEmpSt;
	
	public String getCateCode() {
		return cateCode;
	}
	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getCodeEmpSt() {
		return codeEmpSt;
	}
	public void setCodeEmpSt(String codeEmpSt) {
		this.codeEmpSt = codeEmpSt;
	}

		
}
