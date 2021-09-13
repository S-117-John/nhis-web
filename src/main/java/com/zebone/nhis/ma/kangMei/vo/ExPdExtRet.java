package com.zebone.nhis.ma.kangMei.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PD_EXT_RET 
 *
 * @since 2018-08-29 10:52:48
 */
@Table(value="EX_PD_EXT_RET")
public class ExPdExtRet extends BaseModule  {

	@PK
	@Field(value="PK_EXPDEXTRET",id=KeyId.UUID)
    private String pkExpdextret;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="REG_NUM")
    private String regNum;

	@Field(value="ORDERID")
    private String orderid;

	@Field(value="PRES_NO")
    private String presNo;

	@Field(value="ORDSN")
    private String ordsn;

	@Field(value="DESC_INFO")
    private String descInfo;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkExpdextret(){
        return this.pkExpdextret;
    }
    public void setPkExpdextret(String pkExpdextret){
        this.pkExpdextret = pkExpdextret;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getRegNum(){
        return this.regNum;
    }
    public void setRegNum(String regNum){
        this.regNum = regNum;
    }

    public String getOrderid(){
        return this.orderid;
    }
    public void setOrderid(String orderid){
        this.orderid = orderid;
    }

    public String getPresNo(){
        return this.presNo;
    }
    public void setPresNo(String presNo){
        this.presNo = presNo;
    }

    public String getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(String ordsn){
        this.ordsn = ordsn;
    }

    public String getDescInfo(){
        return this.descInfo;
    }
    public void setDescInfo(String descInfo){
        this.descInfo = descInfo;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}