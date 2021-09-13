package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: bl_settle_budget
 *
 * @since 2020-02-10 09:08:42
 */
@Table(value="BL_SETTLE_BUDGET")
public class BlSettleBudget {
	@PK
	@Field(value="PK_BUDGET",id=KeyId.UUID)
    private String pkBudget;

	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="PK_HP")
    private String pkHp;
	
	@Field(value="AMOUNT")
    private Double amount;
	
	@Field(value="AMOUNT_INSU")
    private Double amountInsu;
	
	@Field(value="AMOUNT_PIDIV")
    private Double amountPidiv;
	
	@Field(value="AMOUNT_PI")
    private Double amountPi;
	
	@Field(value="AMOUNT_INDIV")
    private Double amountIndiv;
	
	@Field(value="PK_EMP_BUD")
    private String pkEmpBud;
	
	@Field(value="NAME_EMP_BUD")
    private String nameEmpBud;
	
	@Field(value="DATE_BUD")
    private Date dateBud;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="MODIFIER")
    private String modifier;
	
	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

	public String getPkBudget() {
		return pkBudget;
	}

	public void setPkBudget(String pkBudget) {
		this.pkBudget = pkBudget;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(Double amountInsu) {
		this.amountInsu = amountInsu;
	}

	public Double getAmountPidiv() {
		return amountPidiv;
	}

	public void setAmountPidiv(Double amountPidiv) {
		this.amountPidiv = amountPidiv;
	}

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public Double getAmountIndiv() {
		return amountIndiv;
	}

	public void setAmountIndiv(Double amountIndiv) {
		this.amountIndiv = amountIndiv;
	}

	public String getPkEmpBud() {
		return pkEmpBud;
	}

	public void setPkEmpBud(String pkEmpBud) {
		this.pkEmpBud = pkEmpBud;
	}

	public String getNameEmpBud() {
		return nameEmpBud;
	}

	public void setNameEmpBud(String nameEmpBud) {
		this.nameEmpBud = nameEmpBud;
	}

	public Date getDateBud() {
		return dateBud;
	}

	public void setDateBud(Date dateBud) {
		this.dateBud = dateBud;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	
	
}
