package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_BEAR 
 *
 * @since 2017-06-27 09:26:33
 */
@Table(value="INS_BEAR")
public class InsBear extends BaseModule  {

	@PK
	@Field(value="PK_INSBEAR",id=KeyId.UUID)
    private String pkInsbear;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="CODE_HPPV")
    private String codeHppv;

	@Field(value="DATE_BEGIN")
    private String dateBegin;

	@Field(value="DATE_END")
    private String dateEnd;

	@Field(value="DATE_LABOR")
    private Date dateLabor;

	@Field(value="BABYNUM")
    private Integer babynum;

	@Field(value="WEEK_PREG")
    private Integer weekPreg;

	@Field(value="FLAG_DYS")
    private String flagDys;

	@Field(value="DICT_BEARTYPE")
    private String dictBeartype;
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

	@Field(value="AMT_LMT")
    private double amtLmt;
	
	@Field(value="DICT_PBOPT")
    private String dictPbopt;
	
	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInsbear(){
        return this.pkInsbear;
    }
    public void setPkInsbear(String pkInsbear){
        this.pkInsbear = pkInsbear;
    }

    public String getPkInspv(){
        return this.pkInspv;
    }
    public void setPkInspv(String pkInspv){
        this.pkInspv = pkInspv;
    }

    public String getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(String dateBegin){
        this.dateBegin = dateBegin;
    }

    public String getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(String dateEnd){
        this.dateEnd = dateEnd;
    }

    public Date getDateLabor() {
		return dateLabor;
	}
	public void setDateLabor(Date dateLabor) {
		this.dateLabor = dateLabor;
	}
	public Integer getBabynum(){
        return this.babynum;
    }
    public void setBabynum(Integer babynum){
        this.babynum = babynum;
    }

    public Integer getWeekPreg(){
        return this.weekPreg;
    }
    public void setWeekPreg(Integer weekPreg){
        this.weekPreg = weekPreg;
    }

    public String getFlagDys(){
        return this.flagDys;
    }
    public void setFlagDys(String flagDys){
        this.flagDys = flagDys;
    }
    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getCodeHppv() {
		return codeHppv;
	}
	public void setCodeHppv(String codeHppv) {
		this.codeHppv = codeHppv;
	}
	public String getDictBeartype() {
		return dictBeartype;
	}
	public void setDictBeartype(String dictBeartype) {
		this.dictBeartype = dictBeartype;
	}
	public String getCodeAcc() {
		return codeAcc;
	}
	public void setCodeAcc(String codeAcc) {
		this.codeAcc = codeAcc;
	}

	public String getDictBank() {
		return dictBank;
	}
	public void setDictBank(String dictBank) {
		this.dictBank = dictBank;
	}
	public String getNameAcc() {
		return nameAcc;
	}
	public void setNameAcc(String nameAcc) {
		this.nameAcc = nameAcc;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public double getAmtLmt() {
		return amtLmt;
	}
	public void setAmtLmt(double amtLmt) {
		this.amtLmt = amtLmt;
	}    
	public String getDictPbopt() {
		return dictPbopt;
	}
	public void setDictPbopt(String dictPbopt) {
		this.dictPbopt = dictPbopt;
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