package com.zebone.nhis.common.module.sch.schta;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_TA 
 *
 * @since 2017-11-08 11:48:08
 */
@Table(value="SCH_TA")
public class SchTa extends BaseModule  {

	@PK
	@Field(value="PK_SCHTA",id=KeyId.UUID)
    private String pkSchta;

	@Field(value="DT_TASCHTYPE")
    private String dtTaschtype;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="DT_TATYPE")
    private String dtTatype;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_SCH")
    private Date dateSch;

	@Field(value="PK_EMP_SCH")
    private String pkEmpSch;

	@Field(value="NAME_EMP_SCH")
    private String nameEmpSch;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	@Field(value="PK_EMP_TEACHER")
	private String pkEmpTeacher;
	
	@Field(value="NAME_EMP_TEACHER")
	private String nameEmpTeacher;		
	
	public String getPkEmpTeacher() {
		return pkEmpTeacher;
	}
	public void setPkEmpTeacher(String pkEmpTeacher) {
		this.pkEmpTeacher = pkEmpTeacher;
	}
	public String getNameEmpTeacher() {
		return nameEmpTeacher;
	}
	public void setNameEmpTeacher(String nameEmpTeacher) {
		this.nameEmpTeacher = nameEmpTeacher;
	}
	public String getPkSchta(){
        return this.pkSchta;
    }
    public void setPkSchta(String pkSchta){
        this.pkSchta = pkSchta;
    }

    public String getDtTaschtype(){
        return this.dtTaschtype;
    }
    public void setDtTaschtype(String dtTaschtype){
        this.dtTaschtype = dtTaschtype;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getDtTatype(){
        return this.dtTatype;
    }
    public void setDtTatype(String dtTatype){
        this.dtTatype = dtTatype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getDateSch(){
        return this.dateSch;
    }
    public void setDateSch(Date dateSch){
        this.dateSch = dateSch;
    }

    public String getPkEmpSch(){
        return this.pkEmpSch;
    }
    public void setPkEmpSch(String pkEmpSch){
        this.pkEmpSch = pkEmpSch;
    }

    public String getNameEmpSch(){
        return this.nameEmpSch;
    }
    public void setNameEmpSch(String nameEmpSch){
        this.nameEmpSch = nameEmpSch;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}