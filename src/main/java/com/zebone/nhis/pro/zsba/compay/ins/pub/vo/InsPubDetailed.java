package com.zebone.nhis.pro.zsba.compay.ins.pub.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_cert_out - ins_cert_out 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_DETAILED")
public class InsPubDetailed extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4453394141463354442L;

	@PK
	@Field(value="PK_INSDETA",id=KeyId.UUID)
    private String pkInsdeta;

	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/**1省内医保，2跨省医保*/
	@Field(value="INS_TYPE")
    private String insType;
	
	/***/
	@Field(value="pk_settle")
    private String pkSettle;
	
	/***/
	@Field(value="jzjlh")
    private String jzjlh;
	
	/***/
	@Field(value="xmxh")
    private String xmxh;
	
	/***/
	@Field(value="kzrq")
    private String kzrq;
	
	/***/
	@Field(value="tzrq")
    private String tzrq;

	/***/
	@Field(value="xmbh")
    private String xmbh;
	
	/***/
	@Field(value="yyxmbh")
    private String yyxmbh;
	
	/***/
	@Field(value="xmmc")
    private String xmmc;
	
	/***/
	@Field(value="spmc")
    private String spmc;
	
	/***/
	@Field(value="fldm")
    private String fldm;
	
	/***/
	@Field(value="dlmc")
    private String dlmc;
	
	/***/
	@Field(value="ypgg")
    private String ypgg;
	
	/***/
	@Field(value="ypjx")
    private String ypjx;
	
	/***/
	@Field(value="ypyf")
    private String ypyf;
	
	/***/
	@Field(value="ypjl")
    private String ypjl;
	
	/***/
	@Field(value="yzlb")
    private String yzlb;
	
	/***/
	@Field(value="jg")
    private String jg;
	
	/***/
	@Field(value="mcyl")
    private String mcyl;
	
	/***/
	@Field(value="zfbl")
    private String zfbl;
	
	/***/
	@Field(value="xjje")
    private String xjje;
	
	/***/
	@Field(value="je")
    private String je;
	
	/***/
	@Field(value="yply")
    private String yply;
	
	/***/
	@Field(value="tsypbz")
    private String tsypbz;
	
	/***/
	@Field(value="xzyybz")
    private String xzyybz;
	
	/***/
	@Field(value="sjgyyy")
    private String sjgyyy;
	
	/***/
	@Field(value="dlgg")
    private String dlgg;
	
	/***/
	@Field(value="dwgg")
    private String dwgg;
	
	/***/
	@Field(value="syts")
    private String syts;
	
	/***/
	@Field(value="yjjypbm")
    private String yjjypbm;
	
	/***/
	@Field(value="yyclzczmc")
    private String yyclzczmc;
	
	/***/
	@Field(value="yyclsyjzch")
    private String yyclsyjzch;
	
	/***/
	@Field(value="txbz")
    private String txbz;
	
	/***/
	@Field(value="bz1")
    private String bz1;
	
	/***/
	@Field(value="bz2")
    private String bz2;
	
	/***/
	@Field(value="bz3")
    private String bz3;
	
	/***/
	@Field(value="jksybz")
    private String jksybz;
	
	/***/
	@Field(value="yyxmmc")
    private String yyxmmc;
	
	/***/
	@Field(value="zfje")
    private String zfje;
	
	/***/
	@Field(value="zcwgrzf")
    private String zcwgrzf;
	
	/***/
	@Field(value="zcngrzf")
    private String zcngrzf;
	
	/***/
	@Field(value="yxbxbf")
    private String yxbxbf;
	
	/***/
	@Field(value="sfxmdj")
    private String sfxmdj;

	public String getPkInsdeta() {
		return pkInsdeta;
	}

	public void setPkInsdeta(String pkInsdeta) {
		this.pkInsdeta = pkInsdeta;
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

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getJzjlh() {
		return jzjlh;
	}

	public void setJzjlh(String jzjlh) {
		this.jzjlh = jzjlh;
	}

	public String getXmxh() {
		return xmxh;
	}

	public void setXmxh(String xmxh) {
		this.xmxh = xmxh;
	}

	public String getKzrq() {
		return kzrq;
	}

	public void setKzrq(String kzrq) {
		this.kzrq = kzrq;
	}

	public String getTzrq() {
		return tzrq;
	}

	public void setTzrq(String tzrq) {
		this.tzrq = tzrq;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getYyxmbh() {
		return yyxmbh;
	}

	public void setYyxmbh(String yyxmbh) {
		this.yyxmbh = yyxmbh;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getSpmc() {
		return spmc;
	}

	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}

	public String getFldm() {
		return fldm;
	}

	public void setFldm(String fldm) {
		this.fldm = fldm;
	}

	public String getDlmc() {
		return dlmc;
	}

	public void setDlmc(String dlmc) {
		this.dlmc = dlmc;
	}

	public String getYpgg() {
		return ypgg;
	}

	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}

	public String getYpjx() {
		return ypjx;
	}

	public void setYpjx(String ypjx) {
		this.ypjx = ypjx;
	}

	public String getYpyf() {
		return ypyf;
	}

	public void setYpyf(String ypyf) {
		this.ypyf = ypyf;
	}

	public String getYpjl() {
		return ypjl;
	}

	public void setYpjl(String ypjl) {
		this.ypjl = ypjl;
	}

	public String getYzlb() {
		return yzlb;
	}

	public void setYzlb(String yzlb) {
		this.yzlb = yzlb;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getMcyl() {
		return mcyl;
	}

	public void setMcyl(String mcyl) {
		this.mcyl = mcyl;
	}

	public String getZfbl() {
		return zfbl;
	}

	public void setZfbl(String zfbl) {
		this.zfbl = zfbl;
	}

	public String getXjje() {
		return xjje;
	}

	public void setXjje(String xjje) {
		this.xjje = xjje;
	}

	public String getJe() {
		return je;
	}

	public void setJe(String je) {
		this.je = je;
	}

	public String getYply() {
		return yply;
	}

	public void setYply(String yply) {
		this.yply = yply;
	}

	public String getTsypbz() {
		return tsypbz;
	}

	public void setTsypbz(String tsypbz) {
		this.tsypbz = tsypbz;
	}

	public String getXzyybz() {
		return xzyybz;
	}

	public void setXzyybz(String xzyybz) {
		this.xzyybz = xzyybz;
	}

	public String getSjgyyy() {
		return sjgyyy;
	}

	public void setSjgyyy(String sjgyyy) {
		this.sjgyyy = sjgyyy;
	}

	public String getDlgg() {
		return dlgg;
	}

	public void setDlgg(String dlgg) {
		this.dlgg = dlgg;
	}

	public String getDwgg() {
		return dwgg;
	}

	public void setDwgg(String dwgg) {
		this.dwgg = dwgg;
	}

	public String getSyts() {
		return syts;
	}

	public void setSyts(String syts) {
		this.syts = syts;
	}

	public String getYjjypbm() {
		return yjjypbm;
	}

	public void setYjjypbm(String yjjypbm) {
		this.yjjypbm = yjjypbm;
	}

	public String getYyclzczmc() {
		return yyclzczmc;
	}

	public void setYyclzczmc(String yyclzczmc) {
		this.yyclzczmc = yyclzczmc;
	}

	public String getYyclsyjzch() {
		return yyclsyjzch;
	}

	public void setYyclsyjzch(String yyclsyjzch) {
		this.yyclsyjzch = yyclsyjzch;
	}

	public String getTxbz() {
		return txbz;
	}

	public void setTxbz(String txbz) {
		this.txbz = txbz;
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

	public String getJksybz() {
		return jksybz;
	}

	public void setJksybz(String jksybz) {
		this.jksybz = jksybz;
	}

	public String getYyxmmc() {
		return yyxmmc;
	}

	public void setYyxmmc(String yyxmmc) {
		this.yyxmmc = yyxmmc;
	}

	public String getZfje() {
		return zfje;
	}

	public void setZfje(String zfje) {
		this.zfje = zfje;
	}

	public String getZcwgrzf() {
		return zcwgrzf;
	}

	public void setZcwgrzf(String zcwgrzf) {
		this.zcwgrzf = zcwgrzf;
	}

	public String getZcngrzf() {
		return zcngrzf;
	}

	public void setZcngrzf(String zcngrzf) {
		this.zcngrzf = zcngrzf;
	}

	public String getYxbxbf() {
		return yxbxbf;
	}

	public void setYxbxbf(String yxbxbf) {
		this.yxbxbf = yxbxbf;
	}

	public String getSfxmdj() {
		return sfxmdj;
	}

	public void setSfxmdj(String sfxmdj) {
		this.sfxmdj = sfxmdj;
	}
	
	
}