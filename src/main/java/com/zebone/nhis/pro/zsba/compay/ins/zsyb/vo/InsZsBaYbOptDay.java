package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_bear - 外部医保-日间手术维护[2047] 
 *
 * @since 2017-09-06 10:42:09
 */
@Table(value="INS_OPT_DAY")
public class InsZsBaYbOptDay extends BaseModule  {

	@PK
	@Field(value="PK_OPTDAY",id=KeyId.UUID)
    private String pkOptday;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="CYZDGJDM")
    private String cyzdgjdm;

	@Field(value="CYZD")
    private String cyzd;
	
	@Field(value="ZLFF")
    private String zlff;

	@Field(value="ZLFFMC")
    private String zlffmc;

	@Field(value="SSRQ")
    private Date ssrq;

	@Field(value="MODITY_TIME")
    private String modity_time;

	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="EU_PVTYPE",date=FieldType.UPDATE)
    private String euPvtype;

	public String getPkOptday() {
		return pkOptday;
	}

	public void setPkOptday(String pkOptday) {
		this.pkOptday = pkOptday;
	}

	public String getPkInspv() {
		return pkInspv;
	}

	public void setPkInspv(String pkInspv) {
		this.pkInspv = pkInspv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getJzjlh() {
		return jzjlh;
	}

	public void setJzjlh(String jzjlh) {
		this.jzjlh = jzjlh;
	}

	public String getCyzdgjdm() {
		return cyzdgjdm;
	}

	public void setCyzdgjdm(String cyzdgjdm) {
		this.cyzdgjdm = cyzdgjdm;
	}

	public String getZlff() {
		return zlff;
	}

	public void setZlff(String zlff) {
		this.zlff = zlff;
	}

	public String getZlffmc() {
		return zlffmc;
	}

	public void setZlffmc(String zlffmc) {
		this.zlffmc = zlffmc;
	}

	public Date getSsrq() {
		return ssrq;
	}

	public void setSsrq(Date ssrq) {
		this.ssrq = ssrq;
	}

	public String getModity_time() {
		return modity_time;
	}

	public void setModity_time(String modity_time) {
		this.modity_time = modity_time;
	}

	public String getFhz() {
		return fhz;
	}

	public void setFhz(String fhz) {
		this.fhz = fhz;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getCyzd() {
		return cyzd;
	}

	public void setCyzd(String cyzd) {
		this.cyzd = cyzd;
	}


}