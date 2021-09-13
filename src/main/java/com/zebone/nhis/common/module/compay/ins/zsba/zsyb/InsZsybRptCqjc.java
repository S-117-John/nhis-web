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
 * Table: ins_zsyb_rpt_cqjc - 外部医保-中山医保产前检查月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_CQJC")
public class InsZsybRptCqjc extends BaseModule  {

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

	@Field(value="CJDDYY")
    private String cjddyy;

	@Field(value="ZYH")
    private String zyh;

	@Field(value="JZRQ")
    private String jzrq;

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

	@Field(value="DEBZ")
    private BigDecimal debz;

    /** ZZLB - 1流产,2引产,3分娩,4变更终止,5到期自动终止（10个月） */
	@Field(value="ZZLB")
    private String zzlb;

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

    public String getCjddyy(){
        return this.cjddyy;
    }
    public void setCjddyy(String cjddyy){
        this.cjddyy = cjddyy;
    }

    public String getZyh(){
        return this.zyh;
    }
    public void setZyh(String zyh){
        this.zyh = zyh;
    }

    public String getJzrq(){
        return this.jzrq;
    }
    public void setJzrq(String jzrq){
        this.jzrq = jzrq;
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

    public BigDecimal getDebz(){
        return this.debz;
    }
    public void setDebz(BigDecimal debz){
        this.debz = debz;
    }

    public String getZzlb(){
        return this.zzlb;
    }
    public void setZzlb(String zzlb){
        this.zzlb = zzlb;
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