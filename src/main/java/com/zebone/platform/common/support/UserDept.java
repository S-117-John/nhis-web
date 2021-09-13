package com.zebone.platform.common.support;

import java.io.Serializable;

public class UserDept implements Serializable{

	private String codeDept;
	
	private String pkDept;
	
	private String nameDept;
	
	private String isdefualt;
	
	private String pkOrg;
	
	private String codeOrg;

	private String pyCode;
	
	private String dCode;
	
	private int seqno;

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getIsdefualt() {
		return isdefualt;
	}

	public void setIsdefualt(String isdefualt) {
		this.isdefualt = isdefualt;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

}
