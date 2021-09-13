package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_zsyb_zydj - 中山医保转院登记：（医院转院登记[2040]、转院登记情况查询[2041]） 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="ins_zsyb_zydj")
public class InsZsybZydj extends BaseModule  {

	@PK
	@Field(value="PK_INSZSYBZYDJ",id=KeyId.UUID)
    private String pkInszsybzydj;

	@Field(value="PK_PI")
    private String pkPi;

    /** GMSFHM - 个人身份号码和个人参保号作为确认参保患者身份的标志，两者不能同时为空 */
	@Field(value="GMSFHM")
    private String gmsfhm;

	@Field(value="GRSXH")
    private String grsxh;

    /** ZYLB - 1、转定点医院 2、转非定点医院 */
	@Field(value="ZYLB")
    private String zylb;

    /** XJZYY - 转定点用医院编号,转非定点用中文名称 */
	@Field(value="XJZYY")
    private String xjzyy;

	@Field(value="ZYZD")
    private String zyzd;

	@Field(value="JBR")
    private String jbr;

	@Field(value="SPR")
    private String spr;

    /** BQZK - 1 一般 2 紧急 */
	@Field(value="BQZK")
    private String bqzk;

	@Field(value="ZYSPH")
    private String zysph;

	@Field(value="FHZ")
    private Integer fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

    

    public String getPkInszsybzydj() {
		return pkInszsybzydj;
	}
	public void setPkInszsybzydj(String pkInszsybzydj) {
		this.pkInszsybzydj = pkInszsybzydj;
	}
	
	public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getGmsfhm(){
        return this.gmsfhm;
    }
    public void setGmsfhm(String gmsfhm){
        this.gmsfhm = gmsfhm;
    }

    public String getGrsxh(){
        return this.grsxh;
    }
    public void setGrsxh(String grsxh){
        this.grsxh = grsxh;
    }

    public String getZylb(){
        return this.zylb;
    }
    public void setZylb(String zylb){
        this.zylb = zylb;
    }

    public String getXjzyy(){
        return this.xjzyy;
    }
    public void setXjzyy(String xjzyy){
        this.xjzyy = xjzyy;
    }

    public String getZyzd(){
        return this.zyzd;
    }
    public void setZyzd(String zyzd){
        this.zyzd = zyzd;
    }

    public String getJbr(){
        return this.jbr;
    }
    public void setJbr(String jbr){
        this.jbr = jbr;
    }

    public String getSpr(){
        return this.spr;
    }
    public void setSpr(String spr){
        this.spr = spr;
    }

    public String getBqzk(){
        return this.bqzk;
    }
    public void setBqzk(String bqzk){
        this.bqzk = bqzk;
    }

    public String getZysph(){
        return this.zysph;
    }
    public void setZysph(String zysph){
        this.zysph = zysph;
    }

    public Integer getFhz(){
        return this.fhz;
    }
    public void setFhz(Integer fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}