package com.zebone.nhis.base.ou.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_DEPT_TYPE 
 *
 * @since 2018-08-06 12:05:20
 */
@Table(value="BD_OU_DEPT_TYPE")
public class BdOuDeptType extends BaseModule  {

	@PK
	@Field(value="PK_DEPTTYPE",id=KeyId.UUID)
    private String pkDepttype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="DT_DEPTTYPE")
    private String dtDepttype;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	private String name;

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPkDepttype(){
        return this.pkDepttype;
    }
    public void setPkDepttype(String pkDepttype){
        this.pkDepttype = pkDepttype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getDtDepttype(){
        return this.dtDepttype;
    }
    public void setDtDepttype(String dtDepttype){
        this.dtDepttype = dtDepttype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}