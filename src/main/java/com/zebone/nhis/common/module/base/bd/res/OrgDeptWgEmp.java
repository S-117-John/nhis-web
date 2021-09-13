package com.zebone.nhis.common.module.base.bd.res;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 部门_医疗组_人员
 * Table: ORG_DEPT_WG_EMP  - org_dept_wg_emp 
 *
 * @since 2016-08-23 10:32:06
 */
@Table(value="ORG_DEPT_WG_EMP")
public class OrgDeptWgEmp extends BaseModule  {

	@PK
	@Field(value="PK_WGEMP",id=KeyId.UUID)
    private String pkWgemp;

	@Field(value="PK_WG")
    private String pkWg;

	@Field(value="PK_EMP")
    private String pkEmp;

    /** EU_ROLE_PVPSN - 0 主任，1 主管，2 住院，3进修，4 实习 */
	@Field(value="EU_ROLE_PVPSN")
    private String euRolePvpsn;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;


    public String getPkWgemp(){
        return this.pkWgemp;
    }
    public void setPkWgemp(String pkWgemp){
        this.pkWgemp = pkWgemp;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getEuRolePvpsn(){
        return this.euRolePvpsn;
    }
    public void setEuRolePvpsn(String euRolePvpsn){
        this.euRolePvpsn = euRolePvpsn;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
}