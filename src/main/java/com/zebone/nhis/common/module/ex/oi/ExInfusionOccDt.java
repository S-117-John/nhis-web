package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_INFUSION_OCC_DT 
 *
 * @since 2017-11-10 05:08:27
 */
@Table(value="EX_INFUSION_OCC_DT")
public class ExInfusionOccDt extends BaseModule  {

	@PK
	@Field(value="PK_INFUOCCDT",id=KeyId.UUID)
    private String pkInfuoccdt;

	@Field(value="PK_INFUOCC")
    private String pkInfuocc;

	@Field(value="PK_INFUREG")
    private String pkInfureg;

	@Field(value="REG_DT_NO")
    private String regDtNo;

	@Field(value="OCC_NO")
    private String occNo;

	@Field(value="DATE_OPERA")
    private Date dateOpera;

	@Field(value="EMP_OPERA")
    private String empOpera;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="COMMENT_STR")
    private String commentStr;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInfuoccdt(){
        return this.pkInfuoccdt;
    }
    public void setPkInfuoccdt(String pkInfuoccdt){
        this.pkInfuoccdt = pkInfuoccdt;
    }

    public String getPkInfuocc(){
        return this.pkInfuocc;
    }
    public void setPkInfuocc(String pkInfuocc){
        this.pkInfuocc = pkInfuocc;
    }

    public String getPkInfureg(){
        return this.pkInfureg;
    }
    public void setPkInfureg(String pkInfureg){
        this.pkInfureg = pkInfureg;
    }

    public String getRegDtNo(){
        return this.regDtNo;
    }
    public void setRegDtNo(String regDtNo){
        this.regDtNo = regDtNo;
    }

    public String getOccNo(){
        return this.occNo;
    }
    public void setOccNo(String occNo){
        this.occNo = occNo;
    }

    public Date getDateOpera(){
        return this.dateOpera;
    }
    public void setDateOpera(Date dateOpera){
        this.dateOpera = dateOpera;
    }

    public String getEmpOpera(){
        return this.empOpera;
    }
    public void setEmpOpera(String empOpera){
        this.empOpera = empOpera;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getCommentStr(){
        return this.commentStr;
    }
    public void setCommentStr(String commentStr){
        this.commentStr = commentStr;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}