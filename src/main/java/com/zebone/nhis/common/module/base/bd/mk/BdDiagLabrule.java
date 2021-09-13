package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table BD_DIAG_LABRULE
 * 
 * @since 2019-06-24 09:14:25
 *
 */
@Table(value="BD_DIAG_LABRULE")
public class BdDiagLabrule extends BaseModule{
	//主键
	@PK
	@Field(value="PK_LABRULE",id=KeyId.UUID)
	private String pkLabrule;
	//机构
	@Field(value="PK_ORG")
	private String pkOrg;
	//分娩结局
	@Field(value="DT_LABRESULT")
	private String dtLabresult;
	//胎方位
	@Field(value="DT_FETALPOS")
	private String dtFetalpos;
	//分娩方式
	@Field(value="DT_LABORTYPE")
	private String dtLabortype;
	
	//icd编码
	@Field(value="CODE_ICD")
	private String codeIcd;
	//诊断名称
	@Field(value="NAME_ICD")
	private String nameIcd;
	//附加编码
	@Field(value="CODE_ADD")
	private String codeAdd;
	//附加诊断名称
	@Field(value="NAME_ADD")
	private String nameAdd;
	//附加编码1
	@Field(value="CODE_ADD1")
	private String codeAdd1;
	//附加诊断名称1
	@Field(value="NAME_ADD1")
	private String nameAdd1;
	//备注
	@Field(value="NOTE")
	private String note;
	
	public String getPkLabrule() {
		return pkLabrule;
	}
	public void setPkLabrule(String pkLabrule) {
		this.pkLabrule = pkLabrule;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getDtLabresult() {
		return dtLabresult;
	}
	public void setDtLabresult(String dtLabresult) {
		this.dtLabresult = dtLabresult;
	}
	public String getDtFetalpos() {
		return dtFetalpos;
	}
	public void setDtFetalpos(String dtFetalpos) {
		this.dtFetalpos = dtFetalpos;
	}
	public String getDtLabortype() {
		return dtLabortype;
	}
	public void setDtLabortype(String dtLabortype) {
		this.dtLabortype = dtLabortype;
	}
	public String getCodeIcd() {
		return codeIcd;
	}
	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}
	public String getNameIcd() {
		return nameIcd;
	}
	public void setNameIcd(String nameIcd) {
		this.nameIcd = nameIcd;
	}
	public String getCodeAdd() {
		return codeAdd;
	}
	public void setCodeAdd(String codeAdd) {
		this.codeAdd = codeAdd;
	}
	public String getNameAdd() {
		return nameAdd;
	}
	public void setNameAdd(String nameAdd) {
		this.nameAdd = nameAdd;
	}
	public String getCodeAdd1() {
		return codeAdd1;
	}
	public void setCodeAdd1(String codeAdd1) {
		this.codeAdd1 = codeAdd1;
	}
	public String getNameAdd1() {
		return nameAdd1;
	}
	public void setNameAdd1(String nameAdd1) {
		this.nameAdd1 = nameAdd1;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
