package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_ACTION  - bd_cp_action
包括临床工作和医疗文档 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="BD_CP_ACTION")
public class BdCpAction extends BaseModule  {

	@PK
	@Field(value="PK_CPACTION",id=KeyId.UUID)
    private String pkCpaction;

	@Field(value="CODE_ACT")
    private String codeAct;

	@Field(value="NAME_ACT")
    private String nameAct;

    /** EU_TYPE - 0 活动，1 文档 */
	@Field(value="EU_TYPE")
    private String euType;

    /** EU_ROLE - 0 医生，1 护士 */
	@Field(value="EU_ROLE")
    private String euRole;

	@Field(value="FUNC")
    private String func;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="SPCODE")
    private String spcode;
	
	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value="CODE_DOCTYPE")
	private String codeDoctype;
	
	@Field(value="EU_CALLTYPE")
	private String euCalltype;
	
    public String getPkCpaction(){
        return this.pkCpaction;
    }
    public void setPkCpaction(String pkCpaction){
        this.pkCpaction = pkCpaction;
    }

    public String getCodeAct(){
        return this.codeAct;
    }
    public void setCodeAct(String codeAct){
        this.codeAct = codeAct;
    }

    public String getNameAct(){
        return this.nameAct;
    }
    public void setNameAct(String nameAct){
        this.nameAct = nameAct;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getEuRole(){
        return this.euRole;
    }
    public void setEuRole(String euRole){
        this.euRole = euRole;
    }

    public String getFunc(){
        return this.func;
    }
    public void setFunc(String func){
        this.func = func;
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
    
    public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getdCode() {
		return dCode;
	}
	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	/**
	 * 已使用标志
	 */
	private String flagCpUse;

	public String getFlagCpUse() {
		return flagCpUse;
	}
	public void setFlagCpUse(String flagCpUse) {
		this.flagCpUse = flagCpUse;
	}
	public String getCodeDoctype() {
		return codeDoctype;
	}
	public void setCodeDoctype(String codeDoctype) {
		this.codeDoctype = codeDoctype;
	}
	public String getEuCalltype() {
		return euCalltype;
	}
	public void setEuCalltype(String euCalltype) {
		this.euCalltype = euCalltype;
	}
	
}