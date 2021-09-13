package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_OPT_PB 
 *
 * @since 2017-06-27 09:26:00
 */
@Table(value="INS_OPT_PB")
public class InsOptPb extends BaseModule  {

	@PK
	@Field(value="PK_OPTPB",id=KeyId.UUID)
    private String pkOptpb;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="CODE_HPPV")
    private String codeHppv;

	@Field(value="DATE_OPT")
    private String dateOpt;

	@Field(value="PB_OPT1")
    private String pbOpt1;

	@Field(value="PB_OPT2")
    private String pbOpt2;

	@Field(value="PB_OPT3")
    private String pbOpt3;

	@Field(value="CODE_ACC")
    private String codeAcc;

	@Field(value="DICT_BANK")
    private String dictBank;

	@Field(value="NAME_ACC")
    private String nameAcc;

	@Field(value="CONTACT")
    private String contact;

	@Field(value="OPERATOR")
    private String operator;

	@Field(value="AUDITOR")
    private String auditor;

	@Field(value="WEEK_PREG")
    private Integer weekPreg;

	@Field(value="FLAG_SEC")
    private String flagSec;

	@Field(value="AMT_LMT")
    private double amtLmt;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOptpb(){
        return this.pkOptpb;
    }
    public void setPkOptpb(String pkOptpb){
        this.pkOptpb = pkOptpb;
    }

    public String getPkInspv(){
        return this.pkInspv;
    }
    public void setPkInspv(String pkInspv){
        this.pkInspv = pkInspv;
    }

    public String getCodeHppv(){
        return this.codeHppv;
    }
    public void setCodeHppv(String codeHppv){
        this.codeHppv = codeHppv;
    }

    public String getDateOpt(){
        return this.dateOpt;
    }
    public void setDateOpt(String dateOpt){
        this.dateOpt = dateOpt;
    }

    public String getPbOpt1(){
        return this.pbOpt1;
    }
    public void setPbOpt1(String pbOpt1){
        this.pbOpt1 = pbOpt1;
    }

    public String getPbOpt2(){
        return this.pbOpt2;
    }
    public void setPbOpt2(String pbOpt2){
        this.pbOpt2 = pbOpt2;
    }

    public String getPbOpt3(){
        return this.pbOpt3;
    }
    public void setPbOpt3(String pbOpt3){
        this.pbOpt3 = pbOpt3;
    }

    public String getCodeAcc(){
        return this.codeAcc;
    }
    public void setCodeAcc(String codeAcc){
        this.codeAcc = codeAcc;
    }
    public String getDictBank() {
		return dictBank;
	}
	public void setDictBank(String dictBank) {
		this.dictBank = dictBank;
	}
	public String getNameAcc(){
        return this.nameAcc;
    }
    public void setNameAcc(String nameAcc){
        this.nameAcc = nameAcc;
    }

    public String getContact(){
        return this.contact;
    }
    public void setContact(String contact){
        this.contact = contact;
    }

    public String getOperator(){
        return this.operator;
    }
    public void setOperator(String operator){
        this.operator = operator;
    }

    public String getAuditor(){
        return this.auditor;
    }
    public void setAuditor(String auditor){
        this.auditor = auditor;
    }

    public Integer getWeekPreg(){
        return this.weekPreg;
    }
    public void setWeekPreg(Integer weekPreg){
        this.weekPreg = weekPreg;
    }

    public String getFlagSec(){
        return this.flagSec;
    }
    public void setFlagSec(String flagSec){
        this.flagSec = flagSec;
    }

    public double getAmtLmt(){
        return this.amtLmt;
    }
    public void setAmtLmt(double amtLmt){
        this.amtLmt = amtLmt;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	/**
	 * 医保就在类别
	 */
    private String dictPvtype;


	public String getDictPvtype() {
		return dictPvtype;
	}
	public void setDictPvtype(String dictPvtype) {
		this.dictPvtype = dictPvtype;
	}
}