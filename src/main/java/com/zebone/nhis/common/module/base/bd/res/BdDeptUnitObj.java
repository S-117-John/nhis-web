package com.zebone.nhis.common.module.base.bd.res;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEPT_UNIT_OBJ 
 *
 * @since 2018-08-04 09:55:15
 */
@Table(value="BD_DEPT_UNIT_OBJ")
public class BdDeptUnitObj extends BaseModule  {

	@PK
	@Field(value="PK_UNITOBJ",id=KeyId.UUID)
    private String pkUnitobj;

	@Field(value="PK_DEPTUNIT")
    private String pkDeptunit;

	@Field(value="EU_OBJTYPE")
    private String euObjtype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	/** 优先级*/
    @Field(value="LEVEL_NUM")
    private Integer levelNum;

	private String codeDept;//科室編碼

	private String nameDept;//科室名稱
	
	private String pyCode;//拼音码

    private String dtDepttype;//科室类型

    public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkUnitobj(){
        return this.pkUnitobj;
    }
    public void setPkUnitobj(String pkUnitobj){
        this.pkUnitobj = pkUnitobj;
    }

    public String getPkDeptunit(){
        return this.pkDeptunit;
    }
    public void setPkDeptunit(String pkDeptunit){
        this.pkDeptunit = pkDeptunit;
    }

    public String getEuObjtype(){
        return this.euObjtype;
    }
    public void setEuObjtype(String euObjtype){
        this.euObjtype = euObjtype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
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

    public String getDtDepttype() {
        return dtDepttype;
    }

    public void setDtDepttype(String dtDepttype) {
        this.dtDepttype = dtDepttype;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }
}