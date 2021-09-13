package com.zebone.nhis.common.module.sch.plan;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_PLAN_EMP  - sch_plan_emp 
 *
 * @since 2016-09-18 10:38:27
 */
@Table(value="SCH_PLAN_EMP")
public class SchPlanEmp extends BaseModule  {

	@PK
	@Field(value="PK_PLANEMP",id=KeyId.UUID)
    private String pkPlanemp;

	@Field(value="PK_SCHPLAN")
    private String pkSchplan;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="CNT_EMP")
    private Double cntEmp;
	
	@Field(value="PK_DEPTUNIT")
    private String pkDeptunit;

	private String nameDeptunit;
	public String getNameDeptunit() {
		return nameDeptunit;
	}

	public void setNameDeptunit(String nameDeptunit) {
		this.nameDeptunit = nameDeptunit;
	}
	
    public String getPkPlanemp(){
        return this.pkPlanemp;
    }
    public void setPkPlanemp(String pkPlanemp){
        this.pkPlanemp = pkPlanemp;
    }

    public String getPkSchplan(){
        return this.pkSchplan;
    }
    public void setPkSchplan(String pkSchplan){
        this.pkSchplan = pkSchplan;
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

    public Double getCntEmp(){
        return this.cntEmp;
    }
    public void setCntEmp(Double cntEmp){
        this.cntEmp = cntEmp;
    }
      
    public String getPkDeptunit() {
  		return pkDeptunit;
  	}
  	public void setPkDeptunit(String pkDeptunit) {
  		this.pkDeptunit = pkDeptunit;
  	}

}