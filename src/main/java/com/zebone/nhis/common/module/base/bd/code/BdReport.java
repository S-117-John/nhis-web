package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_REPORT  - bd_report 
 *
 * @since 2016-09-22 08:45:58
 */
@Table(value="BD_REPORT")
public class BdReport extends BaseModule  {

	@PK
	@Field(value="PK_REPORT",id=KeyId.UUID)
    private String pkReport;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** DT_RPTTYPE - 110302 */
	@Field(value="DT_RPTTYPE")
    private String dtRpttype;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="CONDITION")
    private String condition;

	@Field(value="FLAG_SYS")
    private String flagSys;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.ALL)
    private Date modityTime;

	@Field(value="REPORT_COM")
    private String reportCom;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="SQL")
    private String sql;
	
	@Field(value="DESC_DS")
    private String descDs;
	
    public String getReportCom() {
		return reportCom;
	}
	public void setReportCom(String reportCom) {
		this.reportCom = reportCom;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getPkReport(){
        return this.pkReport;
    }
    public void setPkReport(String pkReport){
        this.pkReport = pkReport;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDtRpttype(){
        return this.dtRpttype;
    }
    public void setDtRpttype(String dtRpttype){
        this.dtRpttype = dtRpttype;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getCondition(){
        return this.condition;
    }
    public void setCondition(String condition){
        this.condition = condition;
    }

    public String getFlagSys(){
        return this.flagSys;
    }
    public void setFlagSys(String flagSys){
        this.flagSys = flagSys;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getDescDs() {
		return descDs;
	}
	public void setDescDs(String descDs) {
		this.descDs = descDs;
	}

}