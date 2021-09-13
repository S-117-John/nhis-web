package com.zebone.nhis.sch.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchResourceDt;

public class SchResourceVo extends SchResource{
	
	/** 工号*/
	private String codeEmp;
	
	/** 名称*/
	private String nameEmp;
	
	private String nameDtCnlevel;
	
   //日期分组明细
   private List<SchResourceDt> dtlist;

	public List<SchResourceDt> getDtlist() {
		return dtlist;
	}
	
	public void setDtlist(List<SchResourceDt> dtlist) {
		this.dtlist = dtlist;
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

	public String getNameDtCnlevel() {
		return nameDtCnlevel;
	}

	public void setNameDtCnlevel(String nameDtCnlevel) {
		this.nameDtCnlevel = nameDtCnlevel;
	}
   
   
}
