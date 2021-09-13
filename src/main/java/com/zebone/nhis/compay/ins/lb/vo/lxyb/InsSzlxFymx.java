package com.zebone.nhis.compay.ins.lb.vo.lxyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZLX_FYMX 
 *
 * @since 2018-06-21 11:57:44
 */
@Table(value="INS_SZLX_FYMX")
public class InsSzlxFymx extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** FYLB - FYLB-费用类别 */
	@Field(value="FYLB")
    private String fylb;

    /** YLFWJGBM - YLFWJGBM-医疗服务机构编码 */
	@Field(value="YLFWJGBM")
    private String ylfwjgbm;

    /** DJDM - DJDM-登记代码 */
	@Field(value="DJDM")
    private String djdm;

    /** FYXH - FYXH-费用序号 */
	@Field(value="FYXH")
    private String fyxh;

    /** ZXYPXMBM - ZXYPXMBM-中心药品项目编码 */
	@Field(value="ZXYPXMBM")
    private String zxypxmbm;

    /** JKFYPXMBM - JKFYPXMBM-接口方药品项目编码 */
	@Field(value="JKFYPXMBM")
    private String jkfypxmbm;

    /** JKFYPXMMC - JKFYPXMMC-接口方药品项目名称 */
	@Field(value="JKFYPXMMC")
    private String jkfypxmmc;

    /** JKFYPXMDW - JKFYPXMDW-接口方药品项目单位 */
	@Field(value="JKFYPXMDW")
    private String jkfypxmdw;

    /** KSBM - KSBM-科室编码 */
	@Field(value="KSBM")
    private String ksbm;

    /** CFYSBM - CFYSBM-处方医生编码 */
	@Field(value="CFYSBM")
    private String cfysbm;

    /** CFYSMC - CFYSMC-处方医生名称 */
	@Field(value="CFYSMC")
    private String cfysmc;

    /** SL - SL-数量 */
	@Field(value="SL")
    private String sl;

    /** DJ - DJ-单价 */
	@Field(value="DJ")
    private String dj;

    /** MCYL - MCYL-每次用量 */
	@Field(value="MCYL")
    private String mcyl;

    /** SYPC - SYPC-使用频次 */
	@Field(value="SYPC")
    private String sypc;

    /** YF - YF-用法 */
	@Field(value="YF")
    private String yf;

    /** ZXTS - ZXTS-执行天数 */
	@Field(value="ZXTS")
    private String zxts;

    /** JZCZY - JZCZY-记帐操作员 */
	@Field(value="JZCZY")
    private String jzczy;

    /** DBFYHJ - DBFYHJ-单笔费用合计 */
	@Field(value="DBFYHJ")
    private String dbfyhj;

    /** JZRQ - JZRQ-记帐日期 */
	@Field(value="JZRQ")
    private String jzrq;

    /** SFYP - SFYP-是否药品 */
	@Field(value="SFYP")
    private String sfyp;

    /** FYSZND - FYSZND-费用所在年度 */
	@Field(value="FYSZND")
    private String fysznd;

    /** FADM - FADM-方案代码 */
	@Field(value="FADM")
    private String fadm;

    /** BBH - BBH-版本号 */
	@Field(value="BBH")
    private String bbh;

    /** FYYM - FYYM-费用掩码 */
	@Field(value="FYYM")
    private String fyym;

    /** FYDJ - FYDJ-费用等级 */
	@Field(value="FYDJ")
    private String fydj;

    /** BBLX - BBLX-报表类型 */
	@Field(value="BBLX")
    private String bblx;

    /** ZFBL - ZFBL-自付比例 */
	@Field(value="ZFBL")
    private String zfbl;

    /** BZJG - BZJG-标准价格 */
	@Field(value="BZJG")
    private String bzjg;

    /** CBZFY - CBZFY-超标准费用 */
	@Field(value="CBZFY")
    private String cbzfy;

    /** ZFUFY - ZFFY-自付费用 */
	@Field(value="ZFUFY")
    private String zfufy;

    /** ZFEIFY - ZFFY-自费费用 */
	@Field(value="ZFEIFY")
    private String zfeify;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="CODE_BILL")
	private String codeBill;
	
	@Field(value="PK_CGIP")
	private String pkCgip;
	
	@Field(value="FLAGPRE")
	private String flagpre;

	public String getCodeBill() {
		return codeBill;
	}
	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}
	public String getFyxhReturn() {
		return fyxhReturn;
	}
	public void setFyxhReturn(String fyxhReturn) {
		this.fyxhReturn = fyxhReturn;
	}
	@Field(value="FYXH_RETURN")
	private String fyxhReturn;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getFylb(){
        return this.fylb;
    }
    public void setFylb(String fylb){
        this.fylb = fylb;
    }

    public String getYlfwjgbm(){
        return this.ylfwjgbm;
    }
    public void setYlfwjgbm(String ylfwjgbm){
        this.ylfwjgbm = ylfwjgbm;
    }

    public String getDjdm(){
        return this.djdm;
    }
    public void setDjdm(String djdm){
        this.djdm = djdm;
    }

    public String getFyxh(){
        return this.fyxh;
    }
    public void setFyxh(String fyxh){
        this.fyxh = fyxh;
    }

    public String getZxypxmbm(){
        return this.zxypxmbm;
    }
    public void setZxypxmbm(String zxypxmbm){
        this.zxypxmbm = zxypxmbm;
    }

    public String getJkfypxmbm(){
        return this.jkfypxmbm;
    }
    public void setJkfypxmbm(String jkfypxmbm){
        this.jkfypxmbm = jkfypxmbm;
    }

    public String getJkfypxmmc(){
        return this.jkfypxmmc;
    }
    public void setJkfypxmmc(String jkfypxmmc){
        this.jkfypxmmc = jkfypxmmc;
    }

    public String getJkfypxmdw(){
        return this.jkfypxmdw;
    }
    public void setJkfypxmdw(String jkfypxmdw){
        this.jkfypxmdw = jkfypxmdw;
    }

    public String getKsbm(){
        return this.ksbm;
    }
    public void setKsbm(String ksbm){
        this.ksbm = ksbm;
    }

    public String getCfysbm(){
        return this.cfysbm;
    }
    public void setCfysbm(String cfysbm){
        this.cfysbm = cfysbm;
    }

    public String getCfysmc(){
        return this.cfysmc;
    }
    public void setCfysmc(String cfysmc){
        this.cfysmc = cfysmc;
    }

    public String getSl(){
        return this.sl;
    }
    public void setSl(String sl){
        this.sl = sl;
    }

    public String getDj(){
        return this.dj;
    }
    public void setDj(String dj){
        this.dj = dj;
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

    public String getYf(){
        return this.yf;
    }
    public void setYf(String yf){
        this.yf = yf;
    }

    public String getZxts(){
        return this.zxts;
    }
    public void setZxts(String zxts){
        this.zxts = zxts;
    }

    public String getJzczy(){
        return this.jzczy;
    }
    public void setJzczy(String jzczy){
        this.jzczy = jzczy;
    }

    public String getDbfyhj(){
        return this.dbfyhj;
    }
    public void setDbfyhj(String dbfyhj){
        this.dbfyhj = dbfyhj;
    }

    public String getJzrq(){
        return this.jzrq;
    }
    public void setJzrq(String jzrq){
        this.jzrq = jzrq;
    }

    public String getSfyp(){
        return this.sfyp;
    }
    public void setSfyp(String sfyp){
        this.sfyp = sfyp;
    }

    public String getFysznd(){
        return this.fysznd;
    }
    public void setFysznd(String fysznd){
        this.fysznd = fysznd;
    }

    public String getFadm(){
        return this.fadm;
    }
    public void setFadm(String fadm){
        this.fadm = fadm;
    }

    public String getBbh(){
        return this.bbh;
    }
    public void setBbh(String bbh){
        this.bbh = bbh;
    }

    public String getFyym(){
        return this.fyym;
    }
    public void setFyym(String fyym){
        this.fyym = fyym;
    }

    public String getFydj(){
        return this.fydj;
    }
    public void setFydj(String fydj){
        this.fydj = fydj;
    }

    public String getBblx(){
        return this.bblx;
    }
    public void setBblx(String bblx){
        this.bblx = bblx;
    }

    public String getZfbl(){
        return this.zfbl;
    }
    public void setZfbl(String zfbl){
        this.zfbl = zfbl;
    }

    public String getBzjg(){
        return this.bzjg;
    }
    public void setBzjg(String bzjg){
        this.bzjg = bzjg;
    }

    public String getCbzfy(){
        return this.cbzfy;
    }
    public void setCbzfy(String cbzfy){
        this.cbzfy = cbzfy;
    }

    public String getZfufy(){
        return this.zfufy;
    }
    public void setZfufy(String zfufy){
        this.zfufy = zfufy;
    }

    public String getZfeify(){
        return this.zfeify;
    }
    public void setZfeify(String zfeify){
        this.zfeify = zfeify;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getFlagpre() {
		return flagpre;
	}
	public void setFlagpre(String flagpre) {
		this.flagpre = flagpre;
	}
}