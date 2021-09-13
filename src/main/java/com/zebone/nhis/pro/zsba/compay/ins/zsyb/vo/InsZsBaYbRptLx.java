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
 * Table: ins_zsyb_rpt_lx - 外部医保-中山医保离休月结明细：（费用汇总查询[2202]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT_LX")
public class InsZsBaYbRptLx extends BaseModule  {

	@PK
	@Field(value="PK_INSACCT",id=KeyId.UUID)
    private String pkInsacct;

	@Field(value="JJYWH")
    private String jjywh;

    /** BBLB - 4 */
	@Field(value="BBLB")
    private String bblb;

	@Field(value="KSSJ")
    private String kssj;

	@Field(value="ZZSJ")
    private String zzsj;

	@Field(value="JZLB")
    private String jzlb;

	@Field(value="LXLB")
    private String lxlb;

	@Field(value="RC")
    private Integer rc;

	@Field(value="CR")
    private Integer cr;

	@Field(value="XYFY")
    private BigDecimal xyfy;

	@Field(value="ZYFY")
    private BigDecimal zyfy;

	@Field(value="YBJCFY")
    private BigDecimal ybjcfy;

	@Field(value="TSJCFY")
    private BigDecimal tsjcfy;

	@Field(value="QTFY")
    private BigDecimal qtfy;

	@Field(value="ZLJCFY")
    private BigDecimal zljcfy;

	@Field(value="ZJ")
    private BigDecimal zj;

	@Field(value="XJJE")
    private BigDecimal xjje;

	@Field(value="ZYCF")
    private BigDecimal zycf;

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

    public String getJzlb(){
        return this.jzlb;
    }
    public void setJzlb(String jzlb){
        this.jzlb = jzlb;
    }

    public String getLxlb(){
        return this.lxlb;
    }
    public void setLxlb(String lxlb){
        this.lxlb = lxlb;
    }

    public Integer getRc(){
        return this.rc;
    }
    public void setRc(Integer rc){
        this.rc = rc;
    }

    public Integer getCr(){
        return this.cr;
    }
    public void setCr(Integer cr){
        this.cr = cr;
    }

    public BigDecimal getXyfy(){
        return this.xyfy;
    }
    public void setXyfy(BigDecimal xyfy){
        this.xyfy = xyfy;
    }

    public BigDecimal getZyfy(){
        return this.zyfy;
    }
    public void setZyfy(BigDecimal zyfy){
        this.zyfy = zyfy;
    }

    public BigDecimal getYbjcfy(){
        return this.ybjcfy;
    }
    public void setYbjcfy(BigDecimal ybjcfy){
        this.ybjcfy = ybjcfy;
    }

    public BigDecimal getTsjcfy(){
        return this.tsjcfy;
    }
    public void setTsjcfy(BigDecimal tsjcfy){
        this.tsjcfy = tsjcfy;
    }

    public BigDecimal getQtfy(){
        return this.qtfy;
    }
    public void setQtfy(BigDecimal qtfy){
        this.qtfy = qtfy;
    }

    public BigDecimal getZljcfy(){
        return this.zljcfy;
    }
    public void setZljcfy(BigDecimal zljcfy){
        this.zljcfy = zljcfy;
    }

    public BigDecimal getZj(){
        return this.zj;
    }
    public void setZj(BigDecimal zj){
        this.zj = zj;
    }

    public BigDecimal getXjje(){
        return this.xjje;
    }
    public void setXjje(BigDecimal xjje){
        this.xjje = xjje;
    }

    public BigDecimal getZycf(){
        return this.zycf;
    }
    public void setZycf(BigDecimal zycf){
        this.zycf = zycf;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}