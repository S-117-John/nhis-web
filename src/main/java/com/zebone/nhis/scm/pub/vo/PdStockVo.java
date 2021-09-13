package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.scm.st.PdStock;

public class PdStockVo extends PdStock{
	
	private String posiNo;//货位
	private String pdname;
	private String pdcode;
	private String pkUnit;
	private String unitname;
	private String factory;
	private String spec;
	private int packSize;
	private Double quanPack;//对于盘点 指账面数量 --包装单位
	
	public String getPosiNo() {
		return posiNo;
	}
	public void setPosiNo(String posiNo) {
		this.posiNo = posiNo;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public int getPackSize() {
		return packSize;
	}
	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}
	public Double getQuanPack() {
		return quanPack;
	}
	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}
}
