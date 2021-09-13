package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
/**
 * Table: INS_GZGY_ST - INS_GZGY_ST 
 *
 * 
 */
@Table(value="INS_GZGY_ST")
public class InsGzgySt {
	
	@PK
	@Field(value="PK_GZGYST",id=KeyId.UUID)
	private String pkGzgyst;
	
	@Field(value="CODE_ORG")
	private String codeOrg;
	
	@Field(value="PK_PV")
	private String pkPv;
	
	@Field(value="PK_HP")
	private String pkHp;
	
	@Field(value="PK_SETTLE")
	private String pkSettle;
	
	@Field(value="AMOUNT")
	private Double amount;
	
	@Field(value="AMOUNT_PI")
	private Double amountPi;
	
	@Field(value="AMOUNT_INS_DRUG")
	private Double amountInsDrug;
	
	@Field(value="AMOUNT_INS")
	private Double amountIns;
	
	@Field(value="AMOUNT_UNIT")
	private Double amountUnit;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="DEL_FLAG")
	private String delFlag = "0";
	
	@Field(date=FieldType.ALL)
	private Date ts;

	public String getPkGzgyst() {
		return pkGzgyst;
	}

	public void setPkGzgyst(String pkGzgyst) {
		this.pkGzgyst = pkGzgyst;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
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

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public Double getAmountInsDrug() {
		return amountInsDrug;
	}

	public void setAmountInsDrug(Double amountInsDrug) {
		this.amountInsDrug = amountInsDrug;
	}

	public Double getAmountIns() {
		return amountIns;
	}

	public void setAmountIns(Double amountIns) {
		this.amountIns = amountIns;
	}

	public Double getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(Double amountUnit) {
		this.amountUnit = amountUnit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
	
}
