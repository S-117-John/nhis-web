package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_EXP  - bd_cp_exp 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="BD_CP_EXP")
public class BdCpExp extends BaseModule  {

	@PK
	@Field(value="PK_CPEXP",id=KeyId.UUID)
    private String pkCpexp;

	@Field(value="CODE_EXP")
    private String codeExp;

	@Field(value="NAME_EXP")
    private String nameExp;

	@Field(value="DESC_EXP")
    private String descExp;

	@Field(value="MODIFIER")
    private String modifier;
	
	@Field(value="MODITY_TIME")
	private Date modityTime;

	@Field(value="SPCODE")
    private String spcode;
	
	@Field(value="D_CODE")
    private String dCode;
	
	@Field(value = "DT_CPEXPTYPE")
	private String dtCpexptype;
	
	@Field(value = "SORTNO")
	private String sortno;
	
	@Field(value = "FLAG_NOTE")
	private String flagNote;
	
    public String getFlagNote() {
		return flagNote;
	}
	public void setFlagNote(String flagNote) {
		this.flagNote = flagNote;
	}
	public String getPkCpexp(){
        return this.pkCpexp;
    }
    public void setPkCpexp(String pkCpexp){
        this.pkCpexp = pkCpexp;
    }

    public String getCodeExp(){
        return this.codeExp;
    }
    public void setCodeExp(String codeExp){
        this.codeExp = codeExp;
    }

    public String getNameExp(){
        return this.nameExp;
    }
    public void setNameExp(String nameExp){
        this.nameExp = nameExp;
    }

    public String getDescExp(){
        return this.descExp;
    }
    public void setDescExp(String descExp){
        this.descExp = descExp;
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
	public String getDtCpexptype() {
		return dtCpexptype;
	}
	public void setDtCpexptype(String dtCpexptype) {
		this.dtCpexptype = dtCpexptype;
	}
	public String getSortno() {
		return sortno;
	}
	public void setSortno(String sortno) {
		this.sortno = sortno;
	}  
	
}