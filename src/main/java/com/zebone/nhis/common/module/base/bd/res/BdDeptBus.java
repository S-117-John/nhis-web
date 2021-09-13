package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEPT_BUS  - bd_dept_bus 
 *
 * @since 2016-09-23 02:18:02
 */
@Table(value="BD_DEPT_BUS")
public class BdDeptBus extends BaseModule  {

	@PK
	@Field(value="PK_DEPTBUS",id=KeyId.UUID)
    private String pkDeptbus;

	@Field(value="PK_DEPTBU")
    private String pkDeptbu;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="SORT")
    private Long sort;

    /** DT_DEPTTYPE - 010200 */
	@Field(value="DT_DEPTTYPE")
    private String dtDepttype;
	
	@Field(value="FLAG_DEF")
	private String flagDef;
	
	@Field(value="TIME_BEGIN")
	private String timeBegin;
	
	@Field(value="TIME_END")
	private String timeEnd;

    @Field(value="EU_USECATE")
    private String euUsecate;

    public String getPkDeptbus(){
        return this.pkDeptbus;
    }
    public void setPkDeptbus(String pkDeptbus){
        this.pkDeptbus = pkDeptbus;
    }

    public String getPkDeptbu(){
        return this.pkDeptbu;
    }
    public void setPkDeptbu(String pkDeptbu){
        this.pkDeptbu = pkDeptbu;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Long getSort(){
        return this.sort;
    }
    public void setSort(Long sort){
        this.sort = sort;
    }

    public String getDtDepttype(){
        return this.dtDepttype;
    }
    public void setDtDepttype(String dtDepttype){
        this.dtDepttype = dtDepttype;
    }

	public String getFlagDef() {
		return flagDef;
	}
	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}

	public String getTimeBegin() {
		return timeBegin;
	}
	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

    public String getEuUsecate() {return euUsecate;}
    public void setEuUsecate(String euUsecate) {this.euUsecate = euUsecate;}
}