package com.zebone.nhis.arch.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;

@Table(value="PV_ARCH_PRT")
public class PvArchPrt{

	@PK
	@Field(value="pk_prt")
    private String pkPrt;
	
    @Field(value="PK_PRT_REC")
    private String pkPrtRec;
	
	@Field(value="pk_doc")
    private String pkDoc;
	
	@Field(value="code")
    private String code;
	
	@Field(value="name")
    private String name;
	
	@Field(value="date_prt")
    private Date datePrt;
	
	@Field(value="pk_emp_prt")
    private String pkEmpPrt;
	
	@Field(value="name_emp_prt")
    private String nameEmpPrt;
	
	@Field(value="pages")
    private String pages;
	
	@Field(value="price")
    private String price;
	
	@Field(value="amount")
    private String amount;

	@Field(value="pk_org")
    public String pkOrg;

	@Field(value="creator")
    public String creator;
	
	@Field(value="modifier")
    private String modifier;

	@Field(value="create_time")
	public Date createTime;
    
	@Field(value="ts")
	public Date ts;
	
	@Field(value="flag_del")
	public String flagDel;  // 0未删除  1：删除
	
	public String getPkPrt() {
		return pkPrt;
	}

	public void setPkPrt(String pkPrt) {
		this.pkPrt = pkPrt;
	}

	public String getPkPrtRec() {
		return pkPrtRec;
	}

	public void setPkPrtRec(String pkPrtRec) {
		this.pkPrtRec = pkPrtRec;
	}

	public String getPkDoc() {
		return pkDoc;
	}

	public void setPkDoc(String pkDoc) {
		this.pkDoc = pkDoc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Date getDatePrt() {
		return datePrt;
	}

	public void setDatePrt(Date datePrt) {
		this.datePrt = datePrt;
	}

	public String getPkEmpPrt() {
		return pkEmpPrt;
	}

	public void setPkEmpPrt(String pkEmpPrt) {
		this.pkEmpPrt = pkEmpPrt;
	}

	public String getNameEmpPrt() {
		return nameEmpPrt;
	}

	public void setNameEmpPrt(String nameEmpPrt) {
		this.nameEmpPrt = nameEmpPrt;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getFlagDel() {
		return flagDel;
	}

	public void setFlagDel(String flagDel) {
		this.flagDel = flagDel;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	
	
}
