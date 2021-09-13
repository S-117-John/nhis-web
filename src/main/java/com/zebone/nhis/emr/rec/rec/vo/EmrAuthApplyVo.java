package com.zebone.nhis.emr.rec.rec.vo;

import com.zebone.nhis.common.module.emr.rec.rec.EmrAuthApply;

import java.util.Date;

/**
 * @author liH
 * @version 1.0
 * @date 2020/11/12 11:26
 * @description
 * @currentMinute zebone_CZ
 */
public class EmrAuthApplyVo extends EmrAuthApply {
	public String namePi;
	public String dtSex;
	public String codePi;
	public Date dateBegin;
	public Date docDate;
	public String docName;
	public String pkDept;
	public String docCreator;

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getDocCreator() {
		return docCreator;
	}

	public void setDocCreator(String docCreator) {
		this.docCreator = docCreator;
	}
}
