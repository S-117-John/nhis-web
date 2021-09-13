package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Pos机交易记录（目前只用于个账）
 * @author zrj
 * @date 2020-08-12
 */
@Table(value = "pay_pos_tr")
public class PayPosTr extends BaseModule implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	@PK
	@Field(value = "pk_pos_tr", id = KeyId.UUID)
	private String pkPosTr;//主键
	
	@Field(value = "pk_insst")
	private String pkInsst;//医保结算主键

	@Field(value = "pk_pi")
	private String pkPi;//患者主键

	@Field(value = "pk_pv")
	private String pkPv;//就诊主键

	@Field(value = "eu_pvtype")
	private String euPvtype;//就诊类型

	@Field(value = "pk_settle")
	private String pkSettle;//结算主键

	@Field(value = "sappname")
	private String sappname;//应用名称

	@Field(value = "stransid")
	private String stransid;//交易类型

	@Field(value = "stime")
	private Date stime;//请求时间

	@Field(value = "scard")
	private String scard;//社保卡

	@Field(value = "sidentity")
	private String sidentity;//身份号码

	@Field(value = "sname")
	private String sname;//姓名

	@Field(value = "samt1")
	private BigDecimal samt1;//交易金额 1
	
	@Field(value = "samt2")
	private BigDecimal samt2;//交易金额 2

	@Field(value = "samt3")
	private BigDecimal samt3;//交易金额 3

	@Field(value = "samt4")
	private BigDecimal samt4;//交易金额 4

	@Field(value = "fhm")
	private String fhm;//返回码

	@Field(value = "shh")
	private String shh;//商户号

	@Field(value = "zdh")
	private String zdh;//终端号

	@Field(value = "jyje")
	private BigDecimal jyje;//交易金额

	@Field(value = "pzh")
	private String pzh;//凭证号

	@Field(value = "xtckh")
	private String xtckh;//系统参考号

	@Field(value = "yhkh")
	private String yhkh;//银行卡号

	@Field(value = "shdh")
	private String shdh;//商户单号

	@Field(value = "jysj")
	private Date jysj;//交易时间

	@Field(value = "jyfl")
	private String jyfl;//交易分类
	
	@Field(value = "fkm")
	private String fkm;//付款码
	
	@Field(value = "psam")
	private String psam;//psam 卡号
	
	@Field(value = "fjy")
	private String fjy;//附加域
	
	@Field(value = "bill_status")
	private String billStatus;//对账状态：0未对账、1对账成功、2金额不一致、3单边账
	
	@Field(value = "bill_time")
	private Date billTime;//对账时间
	
	@Field(value = "bill_desc")
	private String billDesc;//对账描述

	@Field(value = "sbkh")
	private String sbkh;//扣个账后pos机返回的参保号
	
	@Field(value = "SJLY")
	private String sjly;//数据来源 01 第三代社保卡（实体卡） 02 医保电子凭证
	
	@Field(value = "TOKEN")
	private String token;//电子凭证授权(验证后的)
	
	@Field(value = "GZYE")
	private String gzye;//个账余额
	
	@Field(value = "ID_NO")
	private String idNo;//身份证(明文)
	
	public PayPosTr(){
		
	}

	public String getPkPosTr() {
		return pkPosTr;
	}

	public void setPkPosTr(String pkPosTr) {
		this.pkPosTr = pkPosTr;
	}

	public String getPkInsst() {
		return pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
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

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getSappname() {
		return sappname;
	}

	public void setSappname(String sappname) {
		this.sappname = sappname;
	}

	public String getStransid() {
		return stransid;
	}

	public void setStransid(String stransid) {
		this.stransid = stransid;
	}

	public String getScard() {
		return scard;
	}

	public void setScard(String scard) {
		this.scard = scard;
	}

	public String getSidentity() {
		return sidentity;
	}

	public void setSidentity(String sidentity) {
		this.sidentity = sidentity;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getFhm() {
		return fhm;
	}

	public void setFhm(String fhm) {
		this.fhm = fhm;
	}

	public String getShh() {
		return shh;
	}

	public void setShh(String shh) {
		this.shh = shh;
	}

	public String getZdh() {
		return zdh;
	}

	public void setZdh(String zdh) {
		this.zdh = zdh;
	}

	public BigDecimal getJyje() {
		return jyje;
	}

	public void setJyje(BigDecimal jyje) {
		this.jyje = jyje;
	}

	public String getPzh() {
		return pzh;
	}

	public void setPzh(String pzh) {
		this.pzh = pzh;
	}

	public String getXtckh() {
		return xtckh;
	}

	public void setXtckh(String xtckh) {
		this.xtckh = xtckh;
	}

	public String getYhkh() {
		return yhkh;
	}

	public void setYhkh(String yhkh) {
		this.yhkh = yhkh;
	}

	public String getShdh() {
		return shdh;
	}

	public void setShdh(String shdh) {
		this.shdh = shdh;
	}

	public String getJyfl() {
		return jyfl;
	}

	public void setJyfl(String jyfl) {
		this.jyfl = jyfl;
	}

	public String getFkm() {
		return fkm;
	}

	public void setFkm(String fkm) {
		this.fkm = fkm;
	}

	public String getPsam() {
		return psam;
	}

	public void setPsam(String psam) {
		this.psam = psam;
	}

	public String getFjy() {
		return fjy;
	}

	public void setFjy(String fjy) {
		this.fjy = fjy;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public String getBillDesc() {
		return billDesc;
	}

	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}

	public String getSbkh() {
		return sbkh;
	}

	public void setSbkh(String sbkh) {
		this.sbkh = sbkh;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getJysj() {
		return jysj;
	}

	public void setJysj(Date jysj) {
		this.jysj = jysj;
	}

	public BigDecimal getSamt1() {
		return samt1;
	}

	public void setSamt1(BigDecimal samt1) {
		this.samt1 = samt1;
	}

	public BigDecimal getSamt2() {
		return samt2;
	}

	public void setSamt2(BigDecimal samt2) {
		this.samt2 = samt2;
	}

	public BigDecimal getSamt3() {
		return samt3;
	}

	public void setSamt3(BigDecimal samt3) {
		this.samt3 = samt3;
	}

	public BigDecimal getSamt4() {
		return samt4;
	}

	public void setSamt4(BigDecimal samt4) {
		this.samt4 = samt4;
	}

	public String getSjly() {
		return sjly;
	}

	public void setSjly(String sjly) {
		this.sjly = sjly;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGzye() {
		return gzye;
	}

	public void setGzye(String gzye) {
		this.gzye = gzye;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	
}