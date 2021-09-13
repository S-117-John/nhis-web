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
 * Table: ins_zsyb_rpt_jhsy - 外部医保-中山医保计划生育月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_JHSY")
public class InsZsybRptJhsy extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 72 */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="ZGXM")
    private String zgxm;

	@Field(value="JZLB")
    private String jzlb;

	@Field(value="PTRQ")
    private String ptrq;

	@Field(value="ZYH")
    private String zyh;

	@Field(value="ZYTS")
    private Integer zyts;

	@Field(value="RYRQ")
    private String ryrq;

	@Field(value="CYRQ")
    private String cyrq;

	@Field(value="YLFYZE")
    private BigDecimal ylfyze;

	@Field(value="ZFEIJE")
    private BigDecimal zfeije;

	@Field(value="GRZFJE")
    private BigDecimal grzfje;

	@Field(value="GRZHZFJE")
    private BigDecimal grzhzfje;

	@Field(value="TCZFJE")
    private BigDecimal tczfje;

	@Field(value="BZ")
    private String bz;

	@Field(value="TSYPZE")
    private BigDecimal tsypze;

	@Field(value="ICD10")
    private String icd10;

	@Field(value="JHSYSS1")
    private String jhsyss1;

	@Field(value="JHSYSS2")
    private String jhsyss2;

	@Field(value="JHSYSS3")
    private String jhsyss3;

	@Field(value="HYZS")
    private Integer hyzs;

    /** SFECJS - 0为否，1为是 */
	@Field(value="SFECJS")
    private String sfecjs;

	@Field(value="DEBZ")
    private BigDecimal debz;

	@Field(value="YYBH")
    private String yybh;

	@Field(value="SDYWH")
    private String sdywh;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsacct(){
        return this.pkInsacct;
    }
    public void setPkInsacct(String pkInsacct){
        this.pkInsacct = pkInsacct;
    }

    public String getJjywh(){
        return this.jjywh;
    }
    public void setJjywh(String jjywh){
        this.jjywh = jjywh;
    }

    public String getBblb(){
        return this.bblb;
    }
    public void setBblb(String bblb){
        this.bblb = bblb;
    }

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public String getZgxm(){
        return this.zgxm;
    }
    public void setZgxm(String zgxm){
        this.zgxm = zgxm;
    }

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public String getPtrq(){
        return this.ptrq;
    }
    public void setPtrq(String ptrq){
        this.ptrq = ptrq;
    }

    public String getZyh(){
        return this.zyh;
    }
    public void setZyh(String zyh){
        this.zyh = zyh;
    }

    public Integer getZyts(){
        return this.zyts;
    }
    public void setZyts(Integer zyts){
        this.zyts = zyts;
    }

    public String getRyrq(){
        return this.ryrq;
    }
    public void setRyrq(String ryrq){
        this.ryrq = ryrq;
    }

    public String getCyrq(){
        return this.cyrq;
    }
    public void setCyrq(String cyrq){
        this.cyrq = cyrq;
    }

    public BigDecimal getYlfyze(){
        return this.ylfyze;
    }
    public void setYlfyze(BigDecimal ylfyze){
        this.ylfyze = ylfyze;
    }

    public BigDecimal getZfeije(){
        return this.zfeije;
    }
    public void setZfeije(BigDecimal zfeije){
        this.zfeije = zfeije;
    }

    public BigDecimal getGrzfje(){
        return this.grzfje;
    }
    public void setGrzfje(BigDecimal grzfje){
        this.grzfje = grzfje;
    }

    public BigDecimal getGrzhzfje(){
        return this.grzhzfje;
    }
    public void setGrzhzfje(BigDecimal grzhzfje){
        this.grzhzfje = grzhzfje;
    }

    public BigDecimal getTczfje(){
        return this.tczfje;
    }
    public void setTczfje(BigDecimal tczfje){
        this.tczfje = tczfje;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public BigDecimal getTsypze(){
        return this.tsypze;
    }
    public void setTsypze(BigDecimal tsypze){
        this.tsypze = tsypze;
    }

    public String getIcd10(){
        return this.icd10;
    }
    public void setIcd10(String icd10){
        this.icd10 = icd10;
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

    public Integer getHyzs(){
        return this.hyzs;
    }
    public void setHyzs(Integer hyzs){
        this.hyzs = hyzs;
    }

    public String getSfecjs(){
        return this.sfecjs;
    }
    public void setSfecjs(String sfecjs){
        this.sfecjs = sfecjs;
    }

    public BigDecimal getDebz(){
        return this.debz;
    }
    public void setDebz(BigDecimal debz){
        this.debz = debz;
    }

    public String getYybh(){
        return this.yybh;
    }
    public void setYybh(String yybh){
        this.yybh = yybh;
    }

    public String getSdywh(){
        return this.sdywh;
    }
    public void setSdywh(String sdywh){
        this.sdywh = sdywh;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}