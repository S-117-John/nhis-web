package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import com.zebone.nhis.common.module.cn.ipdw.SchIpDt;

public class SchIpDtVO {

	public String pkEmp;
	
	public String codeEmp;
	
	public String nameEmp;
	
	public String pkDept;
	
	public List<SchIpDt> list;

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public List<SchIpDt> getList() {
		return list;
	}

	public void setList(List<SchIpDt> list) {
		this.list = list;
	}
}
