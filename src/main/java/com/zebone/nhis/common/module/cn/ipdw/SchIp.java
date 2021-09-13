package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="SCH_IP")
public class SchIp extends BaseModule{

	@PK
	@Field(value="PK_SCHIP",id=KeyId.UUID)
    private String pkSchip;

	@Field(value="EU_TYPE")
    private String euType;
	
	@Field(value="PK_DEPT")
    private String pkDept;
	
	@Field(value="MONTH_SCH")
	private String monthSch;
	
	@Field(value="DATE_BEGIN")
	private Date dateBegin;
	
	@Field(value="DATE_END")
	private Date dateEnd;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="DATE_SCH")
	private Date dateSch;
	
	@Field(value="PK_EMP_SCH")
	private String pkEmpSch;
	
	@Field(value="NAME_EMP_SCH")
	private String nameEmpSch;

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkSchip() {
		return pkSchip;
	}

	public void setPkSchip(String pkSchip) {
		this.pkSchip = pkSchip;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getMonthSch() {
		return monthSch;
	}

	public void setMonthSch(String monthSch) {
		this.monthSch = monthSch;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	

	public Date getDateSch() {
		return dateSch;
	}

	public void setDateSch(Date dateSch) {
		this.dateSch = dateSch;
	}

	public String getPkEmpSch() {
		return pkEmpSch;
	}

	public void setPkEmpSch(String pkEmpSch) {
		this.pkEmpSch = pkEmpSch;
	}

	public String getNameEmpSch() {
		return nameEmpSch;
	}

	public void setNameEmpSch(String nameEmpSch) {
		this.nameEmpSch = nameEmpSch;
	}
}
