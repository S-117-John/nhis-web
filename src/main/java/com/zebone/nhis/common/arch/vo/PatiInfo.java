package com.zebone.nhis.common.arch.vo;

import java.util.Date;
import java.util.List;

public class PatiInfo {
	

    public String pkPv;
    public String codePv;
    public String namePi;
    public String codePi;
    public int ipTimes;
    public String pkDept;
    public String nameDept;
    public Date dateBegin;
    public Date dateEnd;
    public String pkArchive;


    public String code;

    public String name;

    public List<String>sysToUp ;

    public String isChecked ;

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
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

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public int getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(int ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getPkArchive() {
		return pkArchive;
	}

	public void setPkArchive(String pkArchive) {
		this.pkArchive = pkArchive;
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

	public List<String> getSysToUp() {
		return sysToUp;
	}

	public void setSysToUp(List<String> sysToUp) {
		this.sysToUp = sysToUp;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

    
}
