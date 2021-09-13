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
 * Table: ins_zsyb_rpt_mzyf - 外部医保-中山医保民政优抚月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_MZYF")
public class InsZsybRptMzyf extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 82民政优抚报表(重点优抚人员) ，83民政优抚报表(一到六级残疾军人特殊医疗) */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="ZGXM")
    private String zgxm;

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

	@Field(value="GZZFUJE")
    private BigDecimal gzzfuje;

	@Field(value="GZZFEJE")
    private BigDecimal gzzfeje;

    /** XJZFUJE - 此处个人自付费用金额为未扣除民政补助金额后的最终 */
	@Field(value="XJZFUJE")
    private BigDecimal xjzfuje;

	@Field(value="XJZFEJE")
    private BigDecimal xjzfeje;

	@Field(value="TCZFJE")
    private BigDecimal tczfje;

	@Field(value="GWYTCJE")
    private BigDecimal gwytcje;

	@Field(value="BZ")
    private String bz;

	@Field(value="BCYLTCZF")
    private BigDecimal bcyltczf;

	@Field(value="DEJSFZ")
    private BigDecimal dejsfz;

	@Field(value="TSYPZE")
    private BigDecimal tsypze;

	@Field(value="ICD_10")
    private String icd10;

	@Field(value="DBTCZF")
    private BigDecimal dbtczf;

	@Field(value="JSRQ")
    private String jsrq;

	@Field(value="JZLB")
    private String jzlb;

	@Field(value="SDYWH")
    private String sdywh;

    /** YFDXLB - 1、三属人员 2、在乡复员军人 3、带病回乡退伍军人 4、一至四级残疾军人 5、五至六级残疾军人 6、七至十级残疾军人 7、在乡“五老”人员  8、参战涉核退役人员 9、铀矿开采军队退役人员 10、（ */
	@Field(value="YFDXLB")
    private String yfdxlb;

	@Field(value="MZYFTCZF")
    private BigDecimal mzyftczf;

	@Field(value="MZYFLJJE")
    private BigDecimal mzyfljje;

	@Field(value="CYZD")
    private String cyzd;

	@Field(value="GMSFHM")
    private String gmsfhm;

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

    public BigDecimal getTczfje(){
        return this.tczfje;
    }
    public void setTczfje(BigDecimal tczfje){
        this.tczfje = tczfje;
    }

    public BigDecimal getGwytcje(){
        return this.gwytcje;
    }
    public void setGwytcje(BigDecimal gwytcje){
        this.gwytcje = gwytcje;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public BigDecimal getBcyltczf(){
        return this.bcyltczf;
    }
    public void setBcyltczf(BigDecimal bcyltczf){
        this.bcyltczf = bcyltczf;
    }

    public BigDecimal getDejsfz(){
        return this.dejsfz;
    }
    public void setDejsfz(BigDecimal dejsfz){
        this.dejsfz = dejsfz;
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

    public BigDecimal getDbtczf(){
        return this.dbtczf;
    }
    public void setDbtczf(BigDecimal dbtczf){
        this.dbtczf = dbtczf;
    }

    public String getJsrq(){
        return this.jsrq;
    }
    public void setJsrq(String jsrq){
        this.jsrq = jsrq;
    }

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public String getSdywh(){
        return this.sdywh;
    }
    public void setSdywh(String sdywh){
        this.sdywh = sdywh;
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

    public String getCyzd(){
        return this.cyzd;
    }
    public void setCyzd(String cyzd){
        this.cyzd = cyzd;
    }

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}