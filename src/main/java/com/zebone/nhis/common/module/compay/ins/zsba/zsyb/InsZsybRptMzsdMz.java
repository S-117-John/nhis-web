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
 * Table: ins_zsyb_rpt_mzsd_mz - 外部医保-中山医保民政双低普通门诊月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_MZSD_MZ")
public class InsZsybRptMzsdMz extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 84 */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="JSRQ")
    private String jsrq;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="SDYWH")
    private String sdywh;

	@Field(value="XM")
    private String xm;

	@Field(value="GMSFHM")
    private String gmsfhm;

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

    /** XJZFUJE - 未扣减民政优抚统筹支付 */
	@Field(value="XJZFUJE")
    private BigDecimal xjzfuje;

    /** XJZFEJE - 未扣减民政优抚统筹支付 */
	@Field(value="XJZFEJE")
    private BigDecimal xjzfeje;

	@Field(value="GWYTCZF")
    private BigDecimal gwytczf;

	@Field(value="JBYILCBFS")
    private String jbyilcbfs;

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

	@Field(value="DBDXLB")
    private String dbdxlb;

	@Field(value="MZDBTCZF")
    private BigDecimal mzdbtczf;

	@Field(value="MZDBLJJE")
    private BigDecimal mzdbljje;

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

    public String getXm(){
        return this.xm;
    }
    public void setXm(String xm){
        this.xm = xm;
    }

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}