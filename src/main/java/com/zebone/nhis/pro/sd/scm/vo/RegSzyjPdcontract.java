package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDCONTRACT - reg_szyj_pdcontract
深圳药监管理系统 
 *
 * @since 2020-01-01 11:45:42
 */
@Table(value="REG_SZYJ_PDCONTRACT")
public class RegSzyjPdcontract   {

	@PK
	@Field(value="PK_PDCOMP",id=KeyId.UUID)
    private String pkPdcomp;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** HTBH - 1 为生产厂家，2 为供应商，3 为配送商，4 为 GPO */
	@Field(value="HTBH")
    private String htbh;

	@Field(value="HTMXBH")
    private String htmxbh;

	@Field(value="GYSBM")
    private String gysbm;

    /** YXQQ - YYYY-MM-DD */
	@Field(value="YXQQ")
	@JSONField(format="yyyy-MM-dd")
    private Date yxqq;

    /** YXQZ - YYYY-MM-DD */
	@Field(value="YXQZ")
	@JSONField(format="yyyy-MM-dd")
    private Date yxqz;

	@Field(value="YPBM")
    private String ypbm;

	@Field(value="DJ")
    private Double dj;

	@Field(value="SL")
    private Double sl;

	@Field(value="JE")
    private Double je;

	@Field(value="SYSL")
    private Double sysl;

	@Field(value="SYJE")
    private Double syje;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPdcomp(){
        return this.pkPdcomp;
    }
    public void setPkPdcomp(String pkPdcomp){
        this.pkPdcomp = pkPdcomp;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getHtbh(){
        return this.htbh;
    }
    public void setHtbh(String htbh){
        this.htbh = htbh;
    }

    public String getHtmxbh(){
        return this.htmxbh;
    }
    public void setHtmxbh(String htmxbh){
        this.htmxbh = htmxbh;
    }

    public String getGysbm(){
        return this.gysbm;
    }
    public void setGysbm(String gysbm){
        this.gysbm = gysbm;
    }
    
    @JsonDeserialize(using=CustJsonDateFormat.class)
    public Date getYxqq(){
        return this.yxqq;
    }
    public void setYxqq(Date yxqq){
        this.yxqq = yxqq;
    }
    
    @JsonDeserialize(using=CustJsonDateFormat.class)
    public Date getYxqz(){
        return this.yxqz;
    }
    public void setYxqz(Date yxqz){
        this.yxqz = yxqz;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public Double getDj(){
        return this.dj;
    }
    public void setDj(Double dj){
        this.dj = dj;
    }

    public Double getSl(){
        return this.sl;
    }
    public void setSl(Double sl){
        this.sl = sl;
    }

    public Double getJe(){
        return this.je;
    }
    public void setJe(Double je){
        this.je = je;
    }

    public Double getSysl(){
        return this.sysl;
    }
    public void setSysl(Double sysl){
        this.sysl = sysl;
    }

    public Double getSyje(){
        return this.syje;
    }
    public void setSyje(Double syje){
        this.syje = syje;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}