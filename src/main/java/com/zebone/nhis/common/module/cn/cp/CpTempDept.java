package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_DEPT 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_DEPT")
public class CpTempDept extends BaseModule  {

	@PK
	@Field(value="PK_CPDEPT",id=KeyId.UUID)
    private String pkCpdept;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="PK_DEPT_USE")
    private String pkDeptUse;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpdept(){
        return this.pkCpdept;
    }
    public void setPkCpdept(String pkCpdept){
        this.pkCpdept = pkCpdept;
    }

    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
    }

    public String getPkDeptUse(){
        return this.pkDeptUse;
    }
    public void setPkDeptUse(String pkDeptUse){
        this.pkDeptUse = pkDeptUse;
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
	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	private String nameOrgUse;
	private String nameDeptUse;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getNameOrgUse() {
		return nameOrgUse;
	}
	public void setNameOrgUse(String nameOrgUse) {
		this.nameOrgUse = nameOrgUse;
	}
	public String getNameDeptUse() {
		return nameDeptUse;
	}
	public void setNameDeptUse(String nameDeptUse) {
		this.nameDeptUse = nameDeptUse;
	}
}