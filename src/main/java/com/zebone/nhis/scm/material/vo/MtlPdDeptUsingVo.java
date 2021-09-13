package com.zebone.nhis.scm.material.vo;

import java.util.Date;

import com.zebone.nhis.common.module.scm.st.PdDeptusing;

@SuppressWarnings("serial")
public class MtlPdDeptUsingVo extends PdDeptusing {
	private String namePd;
	private String spec;
	private String pkFactory;
	private String pkUnitPack;
	private String nameFactory;
	private String nameUnit;
	private Date dateFac;
	private int packSize;
	private Double quanBack;//当前操作的数量
	private String pkDeptNew;//当前转移的科室

	public String getNamePd() {
		return namePd;
	}
	public void setNamePd(String namePd) {
		this.namePd = namePd;
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
	public String getPkUnitPack() {
		return pkUnitPack;
	}
	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}
	public String getNameFactory() {
		return nameFactory;
	}
	public void setNameFactory(String nameFactory) {
		this.nameFactory = nameFactory;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public Date getDateFac() {
		return dateFac;
	}
	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}
	public int getPackSize() {
		return packSize;
	}
	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}
	public Double getQuanBack() {
		return quanBack;
	}
	public void setQuanBack(Double quanBack) {
		this.quanBack = quanBack;
	}
	public String getPkDeptNew() {
		return pkDeptNew;
	}
	public void setPkDeptNew(String pkDeptNew) {
		this.pkDeptNew = pkDeptNew;
	}

}
