package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_FYMX 
 *
 * @since 2018-05-04 04:54:15
 */
@Table(value="INS_SZYB_FYMX")
public class InsSzybFymx extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** YWLSH - ZYLSH(MZLSH)-住院流水号(门诊流水号) */
	@Field(value="YWLSH")
    private String ywlsh;

	@Field(value="JYLSH")
    private String jylsh;

    /** XMLB - XMLB-项目类别 */
	@Field(value="XMLB")
    private String xmlb;

    /** FYLB - FYLB-费用类别 */
	@Field(value="FYLB")
    private String fylb;

    /** CFH - CFH-处方号 */
	@Field(value="CFH")
    private String cfh;

    /** CFRQ - CFRQ-处方日期 */
	@Field(value="CFRQ")
    private String cfrq;

    /** YYSFXMNM - YYSFXMNM-医院收费项目内码 */
	@Field(value="YYSFXMNM")
    private String yysfxmnm;

    /** SFXMZXBM - SFXMZXBM-收费项目中心编码 */
	@Field(value="SFXMZXBM")
    private String sfxmzxbm;

    /** YYSFXMMC - YYSFXMMC-医院收费项目名称 */
	@Field(value="YYSFXMMC")
    private String yysfxmmc;

    /** DJ - DJ-单价 */
	@Field(value="DJ")
    private String dj;

    /** SL - SL-数量 */
	@Field(value="SL")
    private String sl;

    /** JX - JX-剂型 */
	@Field(value="JX")
    private String jx;

    /** GG - GG-规格 */
	@Field(value="GG")
    private String gg;

    /** MCYL - MCYL-每次用量 */
	@Field(value="MCYL")
    private String mcyl;

    /** SYPC - SYPC-使用频次 */
	@Field(value="SYPC")
    private String sypc;

    /** YSXM - YSXM-医生姓名 */
	@Field(value="YSXM")
    private String ysxm;

    /** CFYS - CFYS-处方医师 */
	@Field(value="CFYS")
    private String cfys;

    /** YF - YF-用法 */
	@Field(value="YF")
    private String yf;

    /** DW - DW-单位 */
	@Field(value="DW")
    private String dw;

    /** KBMC - KBMC-科别名称 */
	@Field(value="KBMC")
    private String kbmc;

    /** ZXTS - ZXTS-执行天数 */
	@Field(value="ZXTS")
    private String zxts;

    /** CYDFFBZ - CYDFFBZ-草药单复方标志 */
	@Field(value="CYDFFBZ")
    private String cydffbz;

    /** JBR - JBR-经办人 */
	@Field(value="JBR")
    private String jbr;

    /** K - K-空 */
	@Field(value="K")
    private String k;

    /** MXKKJE - MXKKJE-明细扣款金额 */
	@Field(value="MXKKJE")
    private String mxkkje;

    /** JE - JE-金额 */
	@Field(value="JE")
    private String je;

    /** ZFBL - ZFBL-自付比例 */
	@Field(value="ZFBL")
    private String zfbl;

    /** JZLB - JZLB-记账类别 */
	@Field(value="JZLB")
    private String jzlb;

    /** XMDJ - XMDJ-项目等级 */
	@Field(value="XMDJ")
    private String xmdj;

    /** CCZLFAZFJE - CCZLFAZFJE-超出治疗方案自付金额 */
	@Field(value="CCZLFAZFJE")
    private String cczlfazfje;

    /** JE1 - JE-金额 */
	@Field(value="JE1")
    private String je1;

    /** ZLJE - ZLJE-自理金额 */
	@Field(value="ZLJE")
    private String zlje;

    /** ZFJE - ZFJE-自费金额 */
	@Field(value="ZFJE")
    private String zfje;

    /** SFXMDJ - SFXMDJ-收费项目等级 */
	@Field(value="SFXMDJ")
    private String sfxmdj;

    /** QEZFBZ - QEZFBZ-全额自费标志 */
	@Field(value="QEZFBZ")
    private String qezfbz;

    /** LRYBJE - LRYBJE-列入医保金额 */
	@Field(value="LRYBJE")
    private String lrybje;

    /** QETC - QETC-全额统筹 */
	@Field(value="QETC")
    private String qetc;

    /** BFTC - BFTC-部分统筹 */
	@Field(value="BFTC")
    private String bftc;

    /** BFZF - BFZF-部分自付 */
	@Field(value="BFZF")
    private String bfzf;

    /** QEZF - QEZF-全额自费 */
	@Field(value="QEZF")
    private String qezf;

    /** ZFBL1 - ZFBL-自付比例 */
	@Field(value="ZFBL1")
    private String zfbl1;

    /** MODIFIER - 最后操作人 */
	@Field(value="MODIFIER")
    private String modifier;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	@Field(value="PK_PV")
	private String pkPv;
	
	@Field(value="CODE_BILL")
	private String codeBill;
	
    /** 计费主键 */
	@Field(value="PK_CGIP")
	private String pkCgip;

	/** 挂号费标记 */
	@Field(value = "FLAGPV")
	private String flagpv;
	
	/** 主键来源标记 */
	@Field(value = "FLAGPRE")
	private String flagpre;
	
	
	

    public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeBill() {
		return codeBill;
	}
	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}
	public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getYwlsh(){
        return this.ywlsh;
    }
    public void setYwlsh(String ywlsh){
        this.ywlsh = ywlsh;
    }

    public String getJylsh(){
        return this.jylsh;
    }
    public void setJylsh(String jylsh){
        this.jylsh = jylsh;
    }

    public String getXmlb(){
        return this.xmlb;
    }
    public void setXmlb(String xmlb){
        this.xmlb = xmlb;
    }

    public String getFylb(){
        return this.fylb;
    }
    public void setFylb(String fylb){
        this.fylb = fylb;
    }

    public String getCfh(){
        return this.cfh;
    }
    public void setCfh(String cfh){
        this.cfh = cfh;
    }

    public String getCfrq(){
        return this.cfrq;
    }
    public void setCfrq(String cfrq){
        this.cfrq = cfrq;
    }

    public String getYysfxmnm(){
        return this.yysfxmnm;
    }
    public void setYysfxmnm(String yysfxmnm){
        this.yysfxmnm = yysfxmnm;
    }

    public String getSfxmzxbm(){
        return this.sfxmzxbm;
    }
    public void setSfxmzxbm(String sfxmzxbm){
        this.sfxmzxbm = sfxmzxbm;
    }

    public String getYysfxmmc(){
        return this.yysfxmmc;
    }
    public void setYysfxmmc(String yysfxmmc){
        this.yysfxmmc = yysfxmmc;
    }

    public String getDj(){
        return this.dj;
    }
    public void setDj(String dj){
        this.dj = dj;
    }

    public String getSl(){
        return this.sl;
    }
    public void setSl(String sl){
        this.sl = sl;
    }

    public String getJx(){
        return this.jx;
    }
    public void setJx(String jx){
        this.jx = jx;
    }

    public String getGg(){
        return this.gg;
    }
    public void setGg(String gg){
        this.gg = gg;
    }

    public String getMcyl(){
        return this.mcyl;
    }
    public void setMcyl(String mcyl){
        this.mcyl = mcyl;
    }

    public String getSypc(){
        return this.sypc;
    }
    public void setSypc(String sypc){
        this.sypc = sypc;
    }

    public String getYsxm(){
        return this.ysxm;
    }
    public void setYsxm(String ysxm){
        this.ysxm = ysxm;
    }

    public String getCfys(){
        return this.cfys;
    }
    public void setCfys(String cfys){
        this.cfys = cfys;
    }

    public String getYf(){
        return this.yf;
    }
    public void setYf(String yf){
        this.yf = yf;
    }

    public String getDw(){
        return this.dw;
    }
    public void setDw(String dw){
        this.dw = dw;
    }

    public String getKbmc(){
        return this.kbmc;
    }
    public void setKbmc(String kbmc){
        this.kbmc = kbmc;
    }

    public String getZxts(){
        return this.zxts;
    }
    public void setZxts(String zxts){
        this.zxts = zxts;
    }

    public String getCydffbz(){
        return this.cydffbz;
    }
    public void setCydffbz(String cydffbz){
        this.cydffbz = cydffbz;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public String getK(){
        return this.k;
    }
    public void setK(String k){
        this.k = k;
    }

    public String getMxkkje(){
        return this.mxkkje;
    }
    public void setMxkkje(String mxkkje){
        this.mxkkje = mxkkje;
    }

    public String getJe(){
        return this.je;
    }
    public void setJe(String je){
        this.je = je;
    }

    public String getZfbl(){
        return this.zfbl;
    }
    public void setZfbl(String zfbl){
        this.zfbl = zfbl;
    }

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public String getXmdj(){
        return this.xmdj;
    }
    public void setXmdj(String xmdj){
        this.xmdj = xmdj;
    }

    public String getCczlfazfje(){
        return this.cczlfazfje;
    }
    public void setCczlfazfje(String cczlfazfje){
        this.cczlfazfje = cczlfazfje;
    }

    public String getJe1(){
        return this.je1;
    }
    public void setJe1(String je1){
        this.je1 = je1;
    }

    public String getZlje(){
        return this.zlje;
    }
    public void setZlje(String zlje){
        this.zlje = zlje;
    }

    public String getZfje(){
        return this.zfje;
    }
    public void setZfje(String zfje){
        this.zfje = zfje;
    }

    public String getSfxmdj(){
        return this.sfxmdj;
    }
    public void setSfxmdj(String sfxmdj){
        this.sfxmdj = sfxmdj;
    }

    public String getQezfbz(){
        return this.qezfbz;
    }
    public void setQezfbz(String qezfbz){
        this.qezfbz = qezfbz;
    }

    public String getLrybje(){
        return this.lrybje;
    }
    public void setLrybje(String lrybje){
        this.lrybje = lrybje;
    }

    public String getQetc(){
        return this.qetc;
    }
    public void setQetc(String qetc){
        this.qetc = qetc;
    }

    public String getBftc(){
        return this.bftc;
    }
    public void setBftc(String bftc){
        this.bftc = bftc;
    }

    public String getBfzf(){
        return this.bfzf;
    }
    public void setBfzf(String bfzf){
        this.bfzf = bfzf;
    }

    public String getQezf(){
        return this.qezf;
    }
    public void setQezf(String qezf){
        this.qezf = qezf;
    }

    public String getZfbl1(){
        return this.zfbl1;
    }
    public void setZfbl1(String zfbl1){
        this.zfbl1 = zfbl1;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getFlagpv() {
		return flagpv;
	}
	public void setFlagpv(String flagpv) {
		this.flagpv = flagpv;
	}
	public String getFlagpre() {
		return flagpre;
	}
	public void setFlagpre(String flagpre) {
		this.flagpre = flagpre;
	}

}