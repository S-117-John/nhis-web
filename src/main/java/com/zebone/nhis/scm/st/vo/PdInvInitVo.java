package com.zebone.nhis.scm.st.vo;

import com.zebone.nhis.common.module.scm.st.PdInvInit;

public class PdInvInitVo extends PdInvInit {
	private String spec;// 规格
	private String pkFactory;// 生产厂家
	private String pkUnit;//当前使用仓库单位
	private Integer packSize;//当前仓库包装量
	private Integer packSizePd;//零售包装量

	public Integer getPackSizePd() {
		return packSizePd;
	}

	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
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

}
