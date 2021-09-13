package com.zebone.nhis.emr.comm.vo;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;

import java.util.Date;

public class EmrMedRecPatVo extends EmrMedRec {
	private String codeIp;
	private String patName;
	private String codePv;
	private String codePi;
	private Date dateBegin;
    private String referEmpCode;
    private Date dateAdmit;
    private String deptName;

    public String getReferEmpCode() {
        return referEmpCode;
    }

    public void setReferEmpCode(String referEmpCode) {
        this.referEmpCode = referEmpCode;
    }

    public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
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


	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
