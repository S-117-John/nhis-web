package com.zebone.nhis.common.module.compay.ins.zsba.ksyb;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_st_ksyb - ins_st_ksyb_jjfx
 *
 * @since 2017-12-13 10:42:10
 */
@Table(value="INS_ST_KSYB_JJFX")
public class InsKsybStKsybJjfx extends BaseModule  {

	/** 主键 */
	@PK
	@Field(value="PK_INSST",id=KeyId.UUID)
    private String pkInsst;

	/** 医院编号 */
	@Field(value="YYBH")
    private String yybh;
	
	/** 医保类别 */
	@Field(value="PK_HP")
    private String pkHp;
	
	/** 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;
	
	/** 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;
	
	/** 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;
	
	/** 就诊类型 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/** 基金款项代码 */
	@Field(value="AAA157")
    private String aaa157;

	/** 省基金款项代码 */
	@Field(value="AAA160")
    private String aaa160;
	
	/** 基金款项名称 */
	@Field(value="AAD006")
    private String aad006;
	
	/** 基金支付金额 */
	@Field(value="AAE187")
    private String aae187;
	
	 /** 状态标志 */
	@Field(value="STATUS")
    private String status;

	/**
	 * 修改时间
	 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkInsst() {
		return pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getYybh() {
		return yybh;
	}

	public void setYybh(String yybh) {
		this.yybh = yybh;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
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

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getAaa157() {
		return aaa157;
	}

	public void setAaa157(String aaa157) {
		this.aaa157 = aaa157;
	}

	public String getAaa160() {
		return aaa160;
	}

	public void setAaa160(String aaa160) {
		this.aaa160 = aaa160;
	}

	public String getAad006() {
		return aad006;
	}

	public void setAad006(String aad006) {
		this.aad006 = aad006;
	}

	public String getAae187() {
		return aae187;
	}

	public void setAae187(String aae187) {
		this.aae187 = aae187;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}