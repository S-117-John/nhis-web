package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.scm.st.PdRepriceDetail;

public class PdRepriceDtVo extends PdRepriceDetail {
	private String pdcode;
	private String pdname;
	private String spec;
	private String factory;
	private String unit;

	public String getPdcode() {
		return pdcode;
	}

	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}

	public String getPdname() {
		return pdname;
	}

	public void setPdname(String pdname) {
		this.pdname = pdname;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

}
