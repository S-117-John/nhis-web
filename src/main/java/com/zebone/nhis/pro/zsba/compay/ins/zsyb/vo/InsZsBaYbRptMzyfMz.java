package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_zsyb_rpt_mzyf_mz - 外部医保-中山医保民政优抚普通门诊月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_MZYF_MZ")
public class InsZsBaYbRptMzyfMz extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 85民政特殊医疗优抚对象普通门诊和86民政重点优抚对象普通门诊 */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="JSRQ")
    private String jsrq;

	@Field(value="JZJLH")
    private String jzjlh;

	@Field(value="SDYWH")
    private String sdywh;

	@Field(value="GRSXH")
    private String grsxh;

	@Field(value="XM")
    private String xm;

	@Field(value="GMSFHM")
    private String gmsfhm;

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

	@Field(value="YFDXLB")
    private String yfdxlb;

	@Field(value="MZYFTCZF")
    private BigDecimal mzyftczf;

	@Field(value="MZYFLJJE")
    private BigDecimal mzyfljje;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	@Field(value="COLUMN_11")
    private String column11;


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

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getColumn11(){
        return this.column11;
    }
    public void setColumn11(String column11){
        this.column11 = column11;
    }
}