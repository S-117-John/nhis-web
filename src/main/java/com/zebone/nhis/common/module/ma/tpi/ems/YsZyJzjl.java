package com.zebone.nhis.common.module.ma.tpi.ems;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ys_zy_jzjl 
 *
 * @since 2018-09-13 05:26:10
 */
@Table(value="his.ys_zy_jzjl")
public class YsZyJzjl   {

	@Field(value="JZHM")
    private String jzhm;

	@Field(value="BRBH")
    private String brbh;

	@Field(value="BRXM")
    private String brxm;

	@Field(value="BRXB")
    private String brxb;

	@Field(value="CSRQ")
    private Date csrq;

	@Field(value="BRXZ")
    private String brxz;

	@Field(value="KSDM")
    private String ksdm;

	@Field(value="BQDM")
    private String bqdm;

	@Field(value="CWHM")
    private String cwhm;

	@Field(value="YSDM")
    private String ysdm;

	@Field(value="HLJB")
    private BigDecimal hljb;

	@Field(value="RYZD")
    private String ryzd;

	@Field(value="RYQK")
    private BigDecimal ryqk;

	@Field(value="RYRQ")
    private Date ryrq;

	@Field(value="CYRQ")
    private Date cyrq;

	@Field(value="CYBZ")
    private BigDecimal cybz;

	@Field(value="ZKZT")
    private BigDecimal zkzt;

	@Field(value="ZGQK")
    private BigDecimal zgqk;

	@Field(value="YYQK")
    private String yyqk;


    public String getJzhm(){
        return this.jzhm;
    }
    public void setJzhm(String jzhm){
        this.jzhm = jzhm;
    }

    public String getBrbh(){
        return this.brbh;
    }
    public void setBrbh(String brbh){
        this.brbh = brbh;
    }

    public String getBrxm(){
        return this.brxm;
    }
    public void setBrxm(String brxm){
        this.brxm = brxm;
    }

    public String getBrxb(){
        return this.brxb;
    }
    public void setBrxb(String brxb){
        this.brxb = brxb;
    }

    public Date getCsrq(){
        return this.csrq;
    }
    public void setCsrq(Date csrq){
        this.csrq = csrq;
    }

    public String getBrxz(){
        return this.brxz;
    }
    public void setBrxz(String brxz){
        this.brxz = brxz;
    }

    public String getKsdm(){
        return this.ksdm;
    }
    public void setKsdm(String ksdm){
        this.ksdm = ksdm;
    }

    public String getBqdm(){
        return this.bqdm;
    }
    public void setBqdm(String bqdm){
        this.bqdm = bqdm;
    }

    public String getCwhm(){
        return this.cwhm;
    }
    public void setCwhm(String cwhm){
        this.cwhm = cwhm;
    }

    public String getYsdm(){
        return this.ysdm;
    }
    public void setYsdm(String ysdm){
        this.ysdm = ysdm;
    }

    public BigDecimal getHljb(){
        return this.hljb;
    }
    public void setHljb(BigDecimal hljb){
        this.hljb = hljb;
    }

    public String getRyzd(){
        return this.ryzd;
    }
    public void setRyzd(String ryzd){
        this.ryzd = ryzd;
    }

    public BigDecimal getRyqk(){
        return this.ryqk;
    }
    public void setRyqk(BigDecimal ryqk){
        this.ryqk = ryqk;
    }

    public Date getRyrq(){
        return this.ryrq;
    }
    public void setRyrq(Date ryrq){
        this.ryrq = ryrq;
    }

    public Date getCyrq(){
        return this.cyrq;
    }
    public void setCyrq(Date cyrq){
        this.cyrq = cyrq;
    }

    public BigDecimal getCybz(){
        return this.cybz;
    }
    public void setCybz(BigDecimal cybz){
        this.cybz = cybz;
    }

    public BigDecimal getZkzt(){
        return this.zkzt;
    }
    public void setZkzt(BigDecimal zkzt){
        this.zkzt = zkzt;
    }

    public BigDecimal getZgqk(){
        return this.zgqk;
    }
    public void setZgqk(BigDecimal zgqk){
        this.zgqk = zgqk;
    }

    public String getYyqk(){
        return this.yyqk;
    }
    public void setYyqk(String yyqk){
        this.yyqk = yyqk;
    }
}