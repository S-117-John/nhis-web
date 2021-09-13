package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: HIS_CFXM_JKSYBZ 中间库-限制用药使用标志表
 * 
 * @since 2018-09-26 10:20:15
 */
@Table(value = "HIS_CFXM_JKSYBZ")
public class MiddleHisCfxmJksybz {
	@Field(value = "JYDJH")
	private String jydjh;

	@Field(value = "YYBH")
	private String yybh;

	@Field(value = "GMSFHM")
	private String gmsfhm;

	@Field(value = "ZYH")
	private String zyh;

	@Field(value = "XMXH")
	private BigDecimal xmxh;

	@Field(value = "XMBH")
	private String xmbh;

	@Field(value = "XMMC")
	private String xmmc;

	@Field(value = "MCYL")
	private BigDecimal mcyl;

	@Field(value = "JE")
	private BigDecimal je;

	@Field(value = "AKA185")
	private String aka185;

	@Field(value = "BZ1")
	private String bz1;

	@Field(value = "BZ2")
	private String bz2;

	@Field(value = "BZ3")
	private String bz3;

	@Field(value = "RANGE")
	private String range;

	public String getJydjh() {
		return this.jydjh;
	}

	public void setJydjh(String jydjh) {
		this.jydjh = jydjh;
	}

	public String getYybh() {
		return this.yybh;
	}

	public void setYybh(String yybh) {
		this.yybh = yybh;
	}

	public String getGmsfhm() {
		return this.gmsfhm;
	}

	public void setGmsfhm(String gmsfhm) {
		this.gmsfhm = gmsfhm;
	}

	public String getZyh() {
		return this.zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public BigDecimal getXmxh() {
		return this.xmxh;
	}

	public void setXmxh(BigDecimal xmxh) {
		this.xmxh = xmxh;
	}

	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public BigDecimal getMcyl() {
		return this.mcyl;
	}

	public void setMcyl(BigDecimal mcyl) {
		this.mcyl = mcyl;
	}

	public BigDecimal getJe() {
		return this.je;
	}

	public void setJe(BigDecimal je) {
		this.je = je;
	}

	public String getAka185() {
		return this.aka185;
	}

	public void setAka185(String aka185) {
		this.aka185 = aka185;
	}

	public String getBz1() {
		return this.bz1;
	}

	public void setBz1(String bz1) {
		this.bz1 = bz1;
	}

	public String getBz2() {
		return this.bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getBz3() {
		return this.bz3;
	}

	public void setBz3(String bz3) {
		this.bz3 = bz3;
	}

	public String getRange() {
		return this.range;
	}

	public void setRange(String range) {
		this.range = range;
	}
}
