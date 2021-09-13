package com.zebone.nhis.common.module.ma.tpi.ems;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ys_zy_jbzd 
 *
 * @since 2018-09-13 05:28:04
 */
@Table(value="his.ys_zy_jbzd")
public class YsZyJbzd   {

	@Field(value="JLBH")
    private String jlbh;

	@Field(value="JZHM")
    private String jzhm;

	@Field(value="BRBH")
    private String brbh;

	@Field(value="ZDLB")
    private String zdlb;

	@Field(value="JBZH")
    private String jbzh;

	@Field(value="JBDM")
    private String jbdm;

	@Field(value="JBMC")
    private String jbmc;

	@Field(value="MSZD")
    private String mszd;

	@Field(value="ZGQK")
    private String zgqk;

	@Field(value="ZDYS")
    private String zdys;

	@Field(value="ZDSJ")
    private Date zdsj;

	@Field(value="ZFBZ")
    private Integer zfbz;

	@Field(value="TJBZ")
    private Integer tjbz;


    public String getJlbh(){
        return this.jlbh;
    }
    public void setJlbh(String jlbh){
        this.jlbh = jlbh;
    }

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

    public String getZdlb(){
        return this.zdlb;
    }
    public void setZdlb(String zdlb){
        this.zdlb = zdlb;
    }

    public String getJbzh(){
        return this.jbzh;
    }
    public void setJbzh(String jbzh){
        this.jbzh = jbzh;
    }

    public String getJbdm(){
        return this.jbdm;
    }
    public void setJbdm(String jbdm){
        this.jbdm = jbdm;
    }

    public String getJbmc(){
        return this.jbmc;
    }
    public void setJbmc(String jbmc){
        this.jbmc = jbmc;
    }

    public String getMszd(){
        return this.mszd;
    }
    public void setMszd(String mszd){
        this.mszd = mszd;
    }

    public String getZgqk(){
        return this.zgqk;
    }
    public void setZgqk(String zgqk){
        this.zgqk = zgqk;
    }

    public String getZdys(){
        return this.zdys;
    }
    public void setZdys(String zdys){
        this.zdys = zdys;
    }

    public Date getZdsj(){
        return this.zdsj;
    }
    public void setZdsj(Date zdsj){
        this.zdsj = zdsj;
    }

    public Integer getZfbz(){
        return this.zfbz;
    }
    public void setZfbz(Integer zfbz){
        this.zfbz = zfbz;
    }

    public Integer getTjbz(){
        return this.tjbz;
    }
    public void setTjbz(Integer tjbz){
        this.tjbz = tjbz;
    }
}