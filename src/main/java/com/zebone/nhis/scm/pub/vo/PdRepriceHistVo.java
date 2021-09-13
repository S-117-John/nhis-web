package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.scm.st.PdRepriceHist;

public class PdRepriceHistVo extends PdRepriceHist {
	private String spec;
	private String pdname;
	private String factory;
	private String unit;
	private String nameStore;
	private String nameDept;
	private String euRepmode;//调价方式
	private String pkPdrep;//调价单主键
	private String reptype;//调价方式
	private String pkPdstock;
	private String spcode;//拼音码
	private Integer packSizeCvt;//仓库包装量
	private String pkUnitPackCvt;//仓库单位
	
	private Integer packSizePd;//零售包装量
	
	
	public Integer getPackSizePd() {
		return packSizePd;
	}

	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}

	public String getPkUnitPackCvt() {
		return pkUnitPackCvt;
	}

	public void setPkUnitPackCvt(String pkUnitPackCvt) {
		this.pkUnitPackCvt = pkUnitPackCvt;
	}

	public Integer getPackSizeCvt() {
		return packSizeCvt;
	}

	public void setPackSizeCvt(Integer packSizeCvt) {
		this.packSizeCvt = packSizeCvt;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getPkPdstock() {
		return pkPdstock;
	}

	public void setPkPdstock(String pkPdstock) {
		this.pkPdstock = pkPdstock;
	}

	public String getReptype() {
		return reptype;
	}

	public void setReptype(String reptype) {
		this.reptype = reptype;
	}

	public String getPkPdrep() {
		return pkPdrep;
	}

	public void setPkPdrep(String pkPdrep) {
		this.pkPdrep = pkPdrep;
	}

	public String getEuRepmode() {
		return euRepmode;
	}

	public void setEuRepmode(String euRepmode) {
		this.euRepmode = euRepmode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPdname() {
		return pdname;
	}

	public void setPdname(String pdname) {
		this.pdname = pdname;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

}
