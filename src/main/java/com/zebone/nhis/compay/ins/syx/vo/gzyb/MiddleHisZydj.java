package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;
/**
 * Table: HIS_ZYDJ 医保接口中间表-住院登记信息表
 * 
 * @since 2018-07-26 14:35:53
 */
@Table(value = "HIS_ZYDJ")
public class MiddleHisZydj {

	@Field(value = "JYDJH")
	private String jydjh;

	@Field(value = "YYBH")
	private String yybh;

	@Field(value = "GMSFHM")
	private String gmsfhm;

	@Field(value = "XM")
	private String xm;

	@Field(value = "DWMC")
	private String dwmc;

	@Field(value = "XB")
	private String xb;

	@Field(value = "CSRQ")
	private Date csrq;

	@Field(value = "RYLB")
	private String rylb;

	@Field(value = "GWYJB")
	private String gwyjb;

	@Field(value = "ZYH")
	private String zyh;

	@Field(value = "JZLB")
	private String jzlb;

	@Field(value = "RYRQ")
	private Date ryrq;

	@Field(value = "RYZD")
	private String ryzd;

	@Field(value = "RYZDGJDM")
	private String ryzdgjdm;

	@Field(value = "BQDM")
	private String bqdm;

	@Field(value = "CWDH")
	private String cwdh;

	@Field(value = "TZDXSPH")
	private Double tzdxsph;

	@Field(value = "BZ1")
	private String bz1;

	@Field(value = "BZ2")
	private String bz2;

	@Field(value = "BZ3")
	private String bz3;

	@Field(value = "DRBZ")
	private int drbz;

	public String getJydjh() {
		return jydjh;
	}

	public void setJydjh(String jydjh) {
		this.jydjh = jydjh;
	}

	public String getYybh() {
		return yybh;
	}

	public void setYybh(String yybh) {
		this.yybh = yybh;
	}

	public String getGmsfhm() {
		return gmsfhm;
	}

	public void setGmsfhm(String gmsfhm) {
		this.gmsfhm = gmsfhm;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public Date getCsrq() {
		return csrq;
	}

	public void setCsrq(Date csrq) {
		this.csrq = csrq;
	}

	public String getRylb() {
		return rylb;
	}

	public void setRylb(String rylb) {
		this.rylb = rylb;
	}

	public String getGwyjb() {
		return gwyjb;
	}

	public void setGwyjb(String gwyjb) {
		this.gwyjb = gwyjb;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getJzlb() {
		return jzlb;
	}

	public void setJzlb(String jzlb) {
		this.jzlb = jzlb;
	}

	public Date getRyrq() {
		return ryrq;
	}

	public void setRyrq(Date ryrq) {
		this.ryrq = ryrq;
	}

	public String getRyzd() {
		return ryzd;
	}

	public void setRyzd(String ryzd) {
		this.ryzd = ryzd;
	}

	public String getRyzdgjdm() {
		return ryzdgjdm;
	}

	public void setRyzdgjdm(String ryzdgjdm) {
		this.ryzdgjdm = ryzdgjdm;
	}

	public String getBqdm() {
		return bqdm;
	}

	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}

	public String getCwdh() {
		return cwdh;
	}

	public void setCwdh(String cwdh) {
		this.cwdh = cwdh;
	}

	public Double getTzdxsph() {
		return tzdxsph;
	}

	public void setTzdxsph(Double tzdxsph) {
		this.tzdxsph = tzdxsph;
	}

	public String getBz1() {
		return bz1;
	}

	public void setBz1(String bz1) {
		this.bz1 = bz1;
	}

	public String getBz2() {
		return bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getBz3() {
		return bz3;
	}

	public void setBz3(String bz3) {
		this.bz3 = bz3;
	}

	public int getDrbz() {
		return drbz;
	}

	public void setDrbz(int drbz) {
		this.drbz = drbz;
	}
	
}
