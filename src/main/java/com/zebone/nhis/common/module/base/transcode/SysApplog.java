package com.zebone.nhis.common.module.base.transcode;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


@Table(value="SYS_APPLOG")
public class SysApplog extends BaseModule{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8225255322003440350L;

	@PK
	@Field(value="PK_APPLOG",id=KeyId.UUID)
	private String pkApplog;
	
	@Field(value="DATE_OP")
	private Date dateOp;
	
	@Field(value="PK_EMP_OP")
	private String pkEmpOp;
	
	@Field(value="NAME_EMP_OP")
	private String nameEmpOp;
	
	@Field(value="CUSTIP")
	private String custip;
	
	@Field(value="OBJNAME")
	private String objname;
	
	@Field(value="EU_BUTYPE")
	private String euButype;
	
	@Field(value="EU_OPTYPE")
	private String euOptype;
	
	@Field(value="PK_OBJ")
	private String pkObj;
	
	@Field(value="CONTENT_BF")
	private String contentBf;
	
	@Field(value="CONTENT")
	private String content;
	
	@Field(value="NOTE")
	private String note;

	@Field(value="EU_LEVEL")
	private String euLevel;
	
	/** 开始日期 **/
	private Date dateStart;
	
	/** 结束日期 **/
	private Date dateEnd;
	
	
	
	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	
	public String getPkApplog() {
		return pkApplog;
	}

	public void setPkApplog(String pkApplog) {
		this.pkApplog = pkApplog;
	}

	public Date getDateOp() {
		return dateOp;
	}

	public void setDateOp(Date dateOp) {
		this.dateOp = dateOp;
	}

	public String getPkEmpOp() {
		return pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {
		this.pkEmpOp = pkEmpOp;
	}

	public String getNameEmpOp() {
		return nameEmpOp;
	}

	public void setNameEmpOp(String nameEmpOp) {
		this.nameEmpOp = nameEmpOp;
	}

	public String getEuButype() {
		return euButype;
	}

	public void setEuButype(String euButype) {
		this.euButype = euButype;
	}

	public String getEuOptype() {
		return euOptype;
	}

	public void setEuOptype(String euOptype) {
		this.euOptype = euOptype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkObj() {
		return pkObj;
	}

	public void setPkObj(String pkObj) {
		this.pkObj = pkObj;
	}

	public String getCustip() {
		return custip;
	}

	public void setCustip(String custip) {
		this.custip = custip;
	}

	public String getObjname() {
		return objname;
	}

	public void setObjname(String objname) {
		this.objname = objname;
	}

	public String getContentBf() {
		return contentBf;
	}

	public void setContentBf(String contentBf) {
		this.contentBf = contentBf;
	}

	public String getEuLevel() {return euLevel;}

	public void setEuLevel(String euLevel) {this.euLevel = euLevel;}
}
