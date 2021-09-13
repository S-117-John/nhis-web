package com.zebone.nhis.ex.nis.emr.vo;

import java.util.List;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;

public class ExVtsOccVo extends ExVtsOcc {
	private String bedNo;  
	private String codePv;
	private String codeIp;
	private String namePi;
    private String pkPvAs;//pv表的pk_pv,以这个字段为准
    private int sortNo;
   
	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	private List<ExVtsOccDt> details;
    
	public String getPkPvAs() {
		return pkPvAs;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public void setPkPvAs(String pkPvAs) {
		this.pkPvAs = pkPvAs;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public List<ExVtsOccDt> getDetails() {
		return details;
	}

	public void setDetails(List<ExVtsOccDt> details) {
		this.details = details;
	}

}
