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
 * Table: ins_zsyb_rpt_gsmz - 外部医保-中山医保工伤门诊月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_GSMZ")
public class InsZsybRptGsmz extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 9 */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="KSSJ")
    private String kssj;

	@Field(value="ZZSJ")
    private String zzsj;

	@Field(value="RC")
    private Integer rc;

	@Field(value="YLFYZE")
    private BigDecimal ylfyze;

	@Field(value="GRZFJE")
    private BigDecimal grzfje;

	@Field(value="TCZFJE")
    private BigDecimal tczfje;

	@Field(value="GWYTCZF")
    private BigDecimal gwytczf;

	@Field(value="ZFEIJE")
    private BigDecimal zfeije;

	@Field(value="SQBH")
    private String sqbh;

	@Field(value="SQMC")
    private String sqmc;

	@Field(value="CBRS")
    private Integer cbrs;

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

    public String getKssj(){
        return this.kssj;
    }
    public void setKssj(String kssj){
        this.kssj = kssj;
    }

    public String getZzsj(){
        return this.zzsj;
    }
    public void setZzsj(String zzsj){
        this.zzsj = zzsj;
    }

    public Integer getRc(){
        return this.rc;
    }
    public void setRc(Integer rc){
        this.rc = rc;
    }

    public BigDecimal getYlfyze(){
        return this.ylfyze;
    }
    public void setYlfyze(BigDecimal ylfyze){
        this.ylfyze = ylfyze;
    }

    public BigDecimal getGrzfje(){
        return this.grzfje;
    }
    public void setGrzfje(BigDecimal grzfje){
        this.grzfje = grzfje;
    }

    public BigDecimal getTczfje(){
        return this.tczfje;
    }
    public void setTczfje(BigDecimal tczfje){
        this.tczfje = tczfje;
    }

    public BigDecimal getGwytczf(){
        return this.gwytczf;
    }
    public void setGwytczf(BigDecimal gwytczf){
        this.gwytczf = gwytczf;
    }

    public BigDecimal getZfeije(){
        return this.zfeije;
    }
    public void setZfeije(BigDecimal zfeije){
        this.zfeije = zfeije;
    }

    public String getSqbh(){
        return this.sqbh;
    }
    public void setSqbh(String sqbh){
        this.sqbh = sqbh;
    }

    public String getSqmc(){
        return this.sqmc;
    }
    public void setSqmc(String sqmc){
        this.sqmc = sqmc;
    }

    public Integer getCbrs(){
        return this.cbrs;
    }
    public void setCbrs(Integer cbrs){
        this.cbrs = cbrs;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}