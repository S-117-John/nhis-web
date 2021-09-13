package com.zebone.nhis.ma.tpi.rhip.vo;

import com.zebone.nhis.common.module.bl.BlIpDt;

/**
 * 费用明细VO
 * @author chengjia
 *
 */
public class RhipBlIpDtVo extends BlIpDt{
	
	private String cateCode;
	private String cateName;
	private String unitCode;
	private String unitName;
	private String pkSettle;
	private String flagCanc;
	private String ordsn;
	
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
	public String getFlagCanc() {
		return flagCanc;
	}
	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}

		
}
