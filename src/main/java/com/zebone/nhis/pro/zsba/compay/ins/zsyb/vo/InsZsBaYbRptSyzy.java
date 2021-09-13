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
 * Table: ins_zsyb_rpt_syzy - 外部医保-中山医保生育住院月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_SYZY")
public class InsZsBaYbRptSyzy extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 13 */
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

	@Field(value="ZFEIZE")
    private BigDecimal zfeize;

	@Field(value="TCZFJE")
    private BigDecimal tczfje;

	@Field(value="GRZHZF")
    private BigDecimal grzhzf;

	@Field(value="SYLB")
    private String sylb;

	@Field(value="BZ")
    private String bz;

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

    public BigDecimal getZfeize(){
        return this.zfeize;
    }
    public void setZfeize(BigDecimal zfeize){
        this.zfeize = zfeize;
    }

    public BigDecimal getTczfje(){
        return this.tczfje;
    }
    public void setTczfje(BigDecimal tczfje){
        this.tczfje = tczfje;
    }

    public BigDecimal getGrzhzf(){
        return this.grzhzf;
    }
    public void setGrzhzf(BigDecimal grzhzf){
        this.grzhzf = grzhzf;
    }

    public String getSylb(){
        return this.sylb;
    }
    public void setSylb(String sylb){
        this.sylb = sylb;
    }

    public String getBz(){
        return this.bz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}