package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import java.util.Date;

public class SyxPvDoc extends PvDoc{

	private String name;
	
	private String pkDept;
	
	private String nameDept;

	private  Date dateClinic;
	
	private String oldEmrData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getDateClinic(){
		return this.dateClinic;
	}
	public void setDateClinic(Date dateClinic){
		this.dateClinic = dateClinic;
	}

	public String getOldEmrData() {
		return oldEmrData;
	}

	public void setOldEmrData(String oldEmrData) {
		this.oldEmrData = oldEmrData;
	}
	
	
}
