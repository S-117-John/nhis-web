package com.zebone.nhis.ma.pub.platform.sd.entity;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: MID_BL_OP - mid_bl_op 
 *
 * @since 2021-06-29 09:19:50
 */
@Table(value="MID_BL_OP")
public class MidBlOp  {

	@PK
	@Field(value="PK_MID_BL_OP",id=KeyId.UUID)
    private String pkMidBlOp;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="CODE_ITEM")
    private String codeItem;
	
	@Field(value="NAME_ITEM")
    private String nameItem;
	
	@Field(value="PK_ITEM")
    private String pkItem;
	
	@Field(value="QUAN")
    private Double quan;
	
	@Field(value="QUAN_CG")
    private Double quanCg;

	@Field(value="QUAN_UNIT")
    private String quanUnit;
   
	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="FLAG_ITEM")
    private String flagItem;

	@Field(value="MSG_ID")
    private String msgId;
	
	@Field(value="DATE_APPT")
    private Date dateAppt;

	@Field(value="FEE_FLAG")
    private String feeFlag;
	
	@Field(value="DEL_FLAG")
	private String delFlag;

	public String getPkMidBlOp() {
		return pkMidBlOp;
	}

	public void setPkMidBlOp(String pkMidBlOp) {
		this.pkMidBlOp = pkMidBlOp;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeApply() {
		return codeApply;
	}

	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getCodeItem() {
		return codeItem;
	}

	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getQuanUnit() {
		return quanUnit;
	}

	public void setQuanUnit(String quanUnit) {
		this.quanUnit = quanUnit;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getFlagItem() {
		return flagItem;
	}

	public void setFlagItem(String flagItem) {
		this.flagItem = flagItem;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Date getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(Date dateAppt) {
		this.dateAppt = dateAppt;
	}

	public String getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public Double getQuanCg() {
		return quanCg;
	}

	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}
	
}