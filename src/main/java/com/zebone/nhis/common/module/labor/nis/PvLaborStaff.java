package com.zebone.nhis.common.module.labor.nis;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_LABOR_STAFF - PV_LABOR_STAFF 
 *
 * @since 2017-05-18 05:52:37
 */
@Table(value="PV_LABOR_STAFF")
public class PvLaborStaff  extends BaseModule {

	@PK
	@Field(value="PK_PVLABOR_STAFF",id=KeyId.UUID)
    private String pkPvlaborStaff;

	@Field(value="PK_PVLABOR")
    private String pkPvlabor;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

    /** DT_ROLE - 01：主治医生，02：责任护士，03：接生者，04：婴护者 */
	@Field(value="DT_ROLE")
    private String dtRole;
	
	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
    public String getPkPvlaborStaff(){
        return this.pkPvlaborStaff;
    }
    public void setPkPvlaborStaff(String pkPvlaborStaff){
        this.pkPvlaborStaff = pkPvlaborStaff;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPvlabor(){
        return this.pkPvlabor;
    }
    public void setPkPvlabor(String pkPvlabor){
        this.pkPvlabor = pkPvlabor;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
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

    public String getDtRole(){
        return this.dtRole;
    }
    public void setDtRole(String dtRole){
        this.dtRole = dtRole;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

}