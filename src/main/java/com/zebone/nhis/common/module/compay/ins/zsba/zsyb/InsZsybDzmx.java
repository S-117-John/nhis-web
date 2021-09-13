package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_zsyb_dzmx - 中山医保对账明细：（对帐明细查询[4020]）医保管理员不定期调用，调用前需删除掉与入参相同的记录。 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_DZMX")
public class InsZsybDzmx extends BaseModule  {

	@PK
	@Field(value="PK_INSZSYBDZMX",id=KeyId.UUID)
    private String pkInszsybdzmx;

	@Field(value="KSRQ")
    private Date ksrq;

	@Field(value="ZZRQ")
    private Date zzrq;

	@Field(value="TJLB")
    private String tjlb;

	@Field(value="JSRQ")
    private String jsrq;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="SDYWH")
    private String sdywh;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="JZLB")
    private String jzlb;

	@Field(value="RYLB")
    private String rylb;

	@Field(value="YLFYZE")
    private BigDecimal ylfyze;

	@Field(value="JBYLTCZF")
    private BigDecimal jbyltczf;

	@Field(value="GZZFUJE")
    private BigDecimal gzzfuje;

	@Field(value="GZZFEJE")
    private BigDecimal gzzfeje;

	@Field(value="XJZFUJE")
    private BigDecimal xjzfuje;

	@Field(value="XJZFEJE")
    private BigDecimal xjzfeje;

	@Field(value="GWYTCZF")
    private BigDecimal gwytczf;

	@Field(value="JBYILCBFS")
    private String jbyilcbfs;

	@Field(value="DBDXLB")
    private String dbdxlb;

	@Field(value="MZDBTCZF")
    private BigDecimal mzdbtczf;

	@Field(value="MZDBLJJE")
    private BigDecimal mzdbljje;

	@Field(value="GMSFHM")
    private String gmsfhm;

	@Field(value="YFDXLB")
    private String yfdxlb;

	@Field(value="MZYFTCZF")
    private BigDecimal mzyftczf;

	@Field(value="MZYFLJJE")
    private BigDecimal mzyfljje;

	@Field(value="SYLB")
    private String sylb;

	@Field(value="SFNC")
    private String sfnc;

	@Field(value="HBJSSS")
    private String hbjsss;

	@Field(value="DEBZ")
    private BigDecimal debz;

    /** QRBZ - 1为确认，0为未确认 */
	@Field(value="QRBZ")
    private String qrbz;

	@Field(value="JHSYSS1")
    private String jhsyss1;

	@Field(value="JHSYSS2")
    private String jhsyss2;

	@Field(value="JHSYSS3")
    private String jhsyss3;

	@Field(value="SFECJS")
    private String sfecjs;

	@Field(value="ZZLB")
    private String zzlb;

	@Field(value="LXRYLB")
    private String lxrylb;

	@Field(value="BCYLTCZF")
    private BigDecimal bcyltczf;

	@Field(value="DYYYBH")
    private String dyyybh;

	@Field(value="SQBH")
    private String sqbh;

	@Field(value="DBYLTCZF")
    private BigDecimal dbyltczf;

	@Field(value="FHZ")
    private Integer fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInszsybdzmx() {
		return pkInszsybdzmx;
	}
	public void setPkInszsybdzmx(String pkInszsybdzmx) {
		this.pkInszsybdzmx = pkInszsybdzmx;
	}
	
	public Date getKsrq(){
        return this.ksrq;
    }
    public void setKsrq(Date ksrq){
        this.ksrq = ksrq;
    }

    public Date getZzrq(){
        return this.zzrq;
    }
    public void setZzrq(Date zzrq){
        this.zzrq = zzrq;
    }

    public String getTjlb(){
        return this.tjlb;
    }
    public void setTjlb(String tjlb){
        this.tjlb = tjlb;
    }

    public String getJsrq(){
        return this.jsrq;
    }
    public void setJsrq(String jsrq){
        this.jsrq = jsrq;
    }

    public String getJzjlh(){
        return this.jzjlh;
    }
    public void setJzjlh(String jzjlh){
        this.jzjlh = jzjlh;
    }

    public String getSdywh(){
        return this.sdywh;
    }
    public void setSdywh(String sdywh){
        this.sdywh = sdywh;
    }

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public String getRylb(){
        return this.rylb;
    }
    public void setRylb(String rylb){
        this.rylb = rylb;
    }

    public BigDecimal getYlfyze(){
        return this.ylfyze;
    }
    public void setYlfyze(BigDecimal ylfyze){
        this.ylfyze = ylfyze;
    }

    public BigDecimal getJbyltczf(){
        return this.jbyltczf;
    }
    public void setJbyltczf(BigDecimal jbyltczf){
        this.jbyltczf = jbyltczf;
    }

    public BigDecimal getGzzfuje(){
        return this.gzzfuje;
    }
    public void setGzzfuje(BigDecimal gzzfuje){
        this.gzzfuje = gzzfuje;
    }

    public BigDecimal getGzzfeje(){
        return this.gzzfeje;
    }
    public void setGzzfeje(BigDecimal gzzfeje){
        this.gzzfeje = gzzfeje;
    }

    public BigDecimal getXjzfuje(){
        return this.xjzfuje;
    }
    public void setXjzfuje(BigDecimal xjzfuje){
        this.xjzfuje = xjzfuje;
    }

    public BigDecimal getXjzfeje(){
        return this.xjzfeje;
    }
    public void setXjzfeje(BigDecimal xjzfeje){
        this.xjzfeje = xjzfeje;
    }

    public BigDecimal getGwytczf(){
        return this.gwytczf;
    }
    public void setGwytczf(BigDecimal gwytczf){
        this.gwytczf = gwytczf;
    }

    public String getJbyilcbfs(){
        return this.jbyilcbfs;
    }
    public void setJbyilcbfs(String jbyilcbfs){
        this.jbyilcbfs = jbyilcbfs;
    }

    public String getDbdxlb(){
        return this.dbdxlb;
    }
    public void setDbdxlb(String dbdxlb){
        this.dbdxlb = dbdxlb;
    }

    public BigDecimal getMzdbtczf(){
        return this.mzdbtczf;
    }
    public void setMzdbtczf(BigDecimal mzdbtczf){
        this.mzdbtczf = mzdbtczf;
    }

    public BigDecimal getMzdbljje(){
        return this.mzdbljje;
    }
    public void setMzdbljje(BigDecimal mzdbljje){
        this.mzdbljje = mzdbljje;
    }

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
    }

    public String getYfdxlb(){
        return this.yfdxlb;
    }
    public void setYfdxlb(String yfdxlb){
        this.yfdxlb = yfdxlb;
    }

    public BigDecimal getMzyftczf(){
        return this.mzyftczf;
    }
    public void setMzyftczf(BigDecimal mzyftczf){
        this.mzyftczf = mzyftczf;
    }

    public BigDecimal getMzyfljje(){
        return this.mzyfljje;
    }
    public void setMzyfljje(BigDecimal mzyfljje){
        this.mzyfljje = mzyfljje;
    }

    public String getSylb(){
        return this.sylb;
    }
    public void setSylb(String sylb){
        this.sylb = sylb;
    }

    public String getSfnc(){
        return this.sfnc;
    }
    public void setSfnc(String sfnc){
        this.sfnc = sfnc;
    }

    public String getHbjsss(){
        return this.hbjsss;
    }
    public void setHbjsss(String hbjsss){
        this.hbjsss = hbjsss;
    }

    public BigDecimal getDebz(){
        return this.debz;
    }
    public void setDebz(BigDecimal debz){
        this.debz = debz;
    }

    public String getQrbz(){
        return this.qrbz;
    }
    public void setQrbz(String qrbz){
        this.qrbz = qrbz;
    }

    public String getJhsyss1(){
        return this.jhsyss1;
    }
    public void setJhsyss1(String jhsyss1){
        this.jhsyss1 = jhsyss1;
    }

    public String getJhsyss2(){
        return this.jhsyss2;
    }
    public void setJhsyss2(String jhsyss2){
        this.jhsyss2 = jhsyss2;
    }

    public String getJhsyss3(){
        return this.jhsyss3;
    }
    public void setJhsyss3(String jhsyss3){
        this.jhsyss3 = jhsyss3;
    }

    public String getSfecjs(){
        return this.sfecjs;
    }
    public void setSfecjs(String sfecjs){
        this.sfecjs = sfecjs;
    }

    public String getZzlb(){
        return this.zzlb;
    }
    public void setZzlb(String zzlb){
        this.zzlb = zzlb;
    }

    public String getLxrylb(){
        return this.lxrylb;
    }
    public void setLxrylb(String lxrylb){
        this.lxrylb = lxrylb;
    }

    public BigDecimal getBcyltczf(){
        return this.bcyltczf;
    }
    public void setBcyltczf(BigDecimal bcyltczf){
        this.bcyltczf = bcyltczf;
    }

    public String getDyyybh(){
        return this.dyyybh;
    }
    public void setDyyybh(String dyyybh){
        this.dyyybh = dyyybh;
    }

    public String getSqbh(){
        return this.sqbh;
    }
    public void setSqbh(String sqbh){
        this.sqbh = sqbh;
    }

    public BigDecimal getDbyltczf(){
        return this.dbyltczf;
    }
    public void setDbyltczf(BigDecimal dbyltczf){
        this.dbyltczf = dbyltczf;
    }

    public Integer getFhz(){
        return this.fhz;
    }
    public void setFhz(Integer fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}