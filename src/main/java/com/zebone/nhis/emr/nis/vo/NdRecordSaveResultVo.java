package com.zebone.nhis.emr.nis.vo;

public class NdRecordSaveResultVo {
	private String pkRecord;
	
	private String dtTglevel;
	
	private String pkDept;
	
	private String pkEmpPhy;
	
	private String nameEmpNs;
	
	//pv_er结束时间
	private String dateEnd;
	
	private String dateAdmit;

	public String getPkRecord() {
		return pkRecord;
	}

	public void setPkRecord(String pkRecord) {
		this.pkRecord = pkRecord;
	}

	public String getDtTglevel() {
		return dtTglevel;
	}

	public void setDtTglevel(String dtTglevel) {
		this.dtTglevel = dtTglevel;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(String dateAdmit) {
		this.dateAdmit = dateAdmit;
	}
	
	
}
