package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_WORK 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_WORK")
public class CpTempWork extends BaseModule  {

	@PK
	@Field(value="PK_TEMPWORK",id=KeyId.UUID)
    private String pkTempwork;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="PK_CPACTION")
    private String pkCpaction;

	@Field(value="FLAG_NEC")
    private String flagNec;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="SEQ")
    private Integer seq;

    public String getPkTempwork(){
        return this.pkTempwork;
    }
    public void setPkTempwork(String pkTempwork){
        this.pkTempwork = pkTempwork;
    }

    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public String getPkCpaction(){
        return this.pkCpaction;
    }
    public void setPkCpaction(String pkCpaction){
        this.pkCpaction = pkCpaction;
    }

    public String getFlagNec(){
        return this.flagNec;
    }
    public void setFlagNec(String flagNec){
        this.flagNec = flagNec;
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
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	private String nameAct;
	private String euType;
	private String euRole;
	private String func;
	private String euCalltype;
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getNameAct() {
		return nameAct;
	}
	public void setNameAct(String nameAct) {
		this.nameAct = nameAct;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getEuRole() {
		return euRole;
	}
	public void setEuRole(String euRole) {
		this.euRole = euRole;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getEuCalltype() {
		return euCalltype;
	}
	public void setEuCalltype(String euCalltype) {
		this.euCalltype = euCalltype;
	}
}