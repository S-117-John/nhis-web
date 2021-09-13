package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: xt_gzrz 此为深大10.0.3.81的SQLserver数据库上的表
 *
 * @since 2020-03-26 10:03:26
 */
@Table(value="xt_gzrz")
public class XtGzrz   {

	@Field(value="id")
    private Integer id;

	@Field(value="rz_date")
    private Date rzDate;

	@Field(value="type")
    private String type;

	@Field(value="opera")
    private String opera;

	@Field(value="demo")
    private String demo;

	@Field(value="times")
    private Integer times;

	@Field(value="isok")
    private String isok;

	@Field(value="oktimes")
    private String oktimes;

	@Field(value="myd")
    private String myd;

	@Field(value="type1")
    private String type1;

	@Field(value="engineer")
    private String engineer;

	@Field(value="ks")
    private String ks;

	@Field(value="ksms")
    private String ksms;

	@Field(value="diff")
    private String diff;

	@Field(value="kay")
    private String kay;

	@Field(value="unit_dept")
    private String unitDept;

	@Field(value="user_group")
    private String userGroup;

	@Field(value="intending_date")
    private Date intendingDate;

	@Field(value="import_flag")
    private Byte importFlag;

	@Field(value="rz_ps")
    private String rzPs;

	@Field(value="demo_bx")
    private String demoBx;

	@Field(value="apply_opera")
    private String applyOpera;

	@Field(value="imagerecord")
    private byte[] imagerecord;

	@Field(value="demo_bak1")
    private String demoBak1;


    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Date getRzDate(){
        return this.rzDate;
    }
    public void setRzDate(Date rzDate){
        this.rzDate = rzDate;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getOpera(){
        return this.opera;
    }
    public void setOpera(String opera){
        this.opera = opera;
    }

    public String getDemo(){
        return this.demo;
    }
    public void setDemo(String demo){
        this.demo = demo;
    }

    public Integer getTimes(){
        return this.times;
    }
    public void setTimes(Integer times){
        this.times = times;
    }

    public String getIsok(){
        return this.isok;
    }
    public void setIsok(String isok){
        this.isok = isok;
    }

    public String getOktimes(){
        return this.oktimes;
    }
    public void setOktimes(String oktimes){
        this.oktimes = oktimes;
    }

    public String getMyd(){
        return this.myd;
    }
    public void setMyd(String myd){
        this.myd = myd;
    }

    public String getType1(){
        return this.type1;
    }
    public void setType1(String type1){
        this.type1 = type1;
    }

    public String getEngineer(){
        return this.engineer;
    }
    public void setEngineer(String engineer){
        this.engineer = engineer;
    }

    public String getKs(){
        return this.ks;
    }
    public void setKs(String ks){
        this.ks = ks;
    }

    public String getKsms(){
        return this.ksms;
    }
    public void setKsms(String ksms){
        this.ksms = ksms;
    }

    public String getDiff(){
        return this.diff;
    }
    public void setDiff(String diff){
        this.diff = diff;
    }

    public String getKay(){
        return this.kay;
    }
    public void setKay(String kay){
        this.kay = kay;
    }

    public String getUnitDept(){
        return this.unitDept;
    }
    public void setUnitDept(String unitDept){
        this.unitDept = unitDept;
    }

    public String getUserGroup(){
        return this.userGroup;
    }
    public void setUserGroup(String userGroup){
        this.userGroup = userGroup;
    }

    public Date getIntendingDate(){
        return this.intendingDate;
    }
    public void setIntendingDate(Date intendingDate){
        this.intendingDate = intendingDate;
    }

    public Byte getImportFlag(){
        return this.importFlag;
    }
    public void setImportFlag(Byte importFlag){
        this.importFlag = importFlag;
    }

    public String getRzPs(){
        return this.rzPs;
    }
    public void setRzPs(String rzPs){
        this.rzPs = rzPs;
    }

    public String getDemoBx(){
        return this.demoBx;
    }
    public void setDemoBx(String demoBx){
        this.demoBx = demoBx;
    }

    public String getApplyOpera(){
        return this.applyOpera;
    }
    public void setApplyOpera(String applyOpera){
        this.applyOpera = applyOpera;
    }

    public byte[] getImagerecord(){
        return this.imagerecord;
    }
    public void setImagerecord(byte[] imagerecord){
        this.imagerecord = imagerecord;
    }

    public String getDemoBak1(){
        return this.demoBak1;
    }
    public void setDemoBak1(String demoBak1){
        this.demoBak1 = demoBak1;
    }
}