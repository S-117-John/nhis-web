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
 * Table: REG_SZYJ_PDDELIVERY_DT 
 *
 * @since 2020-01-04 10:37:37
 */
@Table(value="REG_SZYJ_PDDELIVERY_DT")
public class RegSzyjPddeliveryDt   {

	@PK
	@Field(value="PK_PDDIVDT",id=KeyId.UUID)
    private String pkPddivdt;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PDDIV")
    private String pkPddiv;

	@Field(value="SXH")
    private String sxh;

	@Field(value="PSDMXBH")
    private String psdmxbh;

	@Field(value="YPBM")
    private String ypbm;

	@Field(value="SCPH")
    private String scph;

	@JSONField(format="yyyy-MM-dd")
	@Field(value="SCRQ")
    private Date scrq;

	@JSONField(format="yyyy-MM-dd")
	@Field(value="YXRQ")
    private Date yxrq;

	@Field(value="TXM")
    private String txm;

	@Field(value="DJ")
    private Double dj;

	@Field(value="PSSL")
    private Double pssl;

	@Field(value="PSSBM")
    private String pssbm;

	@Field(value="ZLJL")
    private String zljl;

	@Field(value="JYBGLJ")
    private String jybglj;

	@Field(value="JLS")
    private String jls;

	@Field(value="CGJHMXBH")
    private String cgjhmxbh;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPddivdt(){
        return this.pkPddivdt;
    }
    public void setPkPddivdt(String pkPddivdt){
        this.pkPddivdt = pkPddivdt;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPddiv(){
        return this.pkPddiv;
    }
    public void setPkPddiv(String pkPddiv){
        this.pkPddiv = pkPddiv;
    }

    public String getSxh(){
        return this.sxh;
    }
    public void setSxh(String sxh){
        this.sxh = sxh;
    }

    public String getPsdmxbh(){
        return this.psdmxbh;
    }
    public void setPsdmxbh(String psdmxbh){
        this.psdmxbh = psdmxbh;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getScph(){
        return this.scph;
    }
    public void setScph(String scph){
        this.scph = scph;
    }

    @JsonDeserialize(using=CustJsonDateFormat.class)
    public Date getScrq(){
        return this.scrq;
    }
    public void setScrq(Date scrq){
        this.scrq = scrq;
    }

    @JsonDeserialize(using=CustJsonDateFormat.class)
    public Date getYxrq(){
        return this.yxrq;
    }
    public void setYxrq(Date yxrq){
        this.yxrq = yxrq;
    }

    public String getTxm(){
        return this.txm;
    }
    public void setTxm(String txm){
        this.txm = txm;
    }

    public Double getDj(){
        return this.dj;
    }
    public void setDj(Double dj){
        this.dj = dj;
    }

    public Double getPssl(){
        return this.pssl;
    }
    public void setPssl(Double pssl){
        this.pssl = pssl;
    }

    public String getPssbm(){
        return this.pssbm;
    }
    public void setPssbm(String pssbm){
        this.pssbm = pssbm;
    }

    public String getZljl(){
        return this.zljl;
    }
    public void setZljl(String zljl){
        this.zljl = zljl;
    }

    public String getJybglj(){
        return this.jybglj;
    }
    public void setJybglj(String jybglj){
        this.jybglj = jybglj;
    }

    public String getJls(){
        return this.jls;
    }
    public void setJls(String jls){
        this.jls = jls;
    }

    public String getCgjhmxbh(){
        return this.cgjhmxbh;
    }
    public void setCgjhmxbh(String cgjhmxbh){
        this.cgjhmxbh = cgjhmxbh;
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