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
 * Table: REG_SZYJ_PDPRICE 
 *
 * @since 2019-12-28 02:00:11
 */
@Table(value="REG_SZYJ_PDPRICE")
public class RegSzyjPdprice   {

	@PK
	@Field(value="PK_PDPRICE",id=KeyId.UUID)
    private String pkPdprice;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="YPBM")
    private String ypbm;

	@Field(value="GYSBM")
    private String gysbm;

	@Field(value="GYSMC")
    private String gysmc;

	@Field(value="ZBJ")
    private Double zbj;

	@Field(value="CJJ")
    private Double cjj;

	@JSONField(format="yyyy-MM-dd")
	@Field(value="SXRQ")
    private Date sxrq;
	
	@JSONField(format="yyyy-MM-dd")
	@Field(value="YXQQSRQ")
    private Date yxqqsrq;

	@JSONField(format="yyyy-MM-dd")
	@Field(value="YXQJZRQ")
    private Date yxqjzrq;

	@Field(value="SFJY")
    private String sfjy;

	@Field(value="BCGXSJ")
    private Date bcgxsj;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPdprice(){
        return this.pkPdprice;
    }
    public void setPkPdprice(String pkPdprice){
        this.pkPdprice = pkPdprice;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getGysbm(){
        return this.gysbm;
    }
    public void setGysbm(String gysbm){
        this.gysbm = gysbm;
    }

    public String getGysmc(){
        return this.gysmc;
    }
    public void setGysmc(String gysmc){
        this.gysmc = gysmc;
    }

    public Double getZbj(){
        return this.zbj;
    }
    public void setZbj(Double zbj){
        this.zbj = zbj;
    }

    public Double getCjj(){
        return this.cjj;
    }
    public void setCjj(Double cjj){
        this.cjj = cjj;
    }

    @JsonDeserialize(using=CustJsonDateFormat.class)
    public Date getSxrq(){
        return this.sxrq;
    }
    public void setSxrq(Date sxrq){
        this.sxrq = sxrq;
    }

    public Date getYxqqsrq(){
        return this.yxqqsrq;
    }
    public void setYxqqsrq(Date yxqqsrq){
        this.yxqqsrq = yxqqsrq;
    }

    public Date getYxqjzrq(){
        return this.yxqjzrq;
    }
    public void setYxqjzrq(Date yxqjzrq){
        this.yxqjzrq = yxqjzrq;
    }

    public String getSfjy(){
        return this.sfjy;
    }
    public void setSfjy(String sfjy){
        this.sfjy = sfjy;
    }

    public Date getBcgxsj(){
        return this.bcgxsj;
    }
    public void setBcgxsj(Date bcgxsj){
        this.bcgxsj = bcgxsj;
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