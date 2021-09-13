package com.zebone.nhis.common.module.base.bd.res;


import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_RES_BED  - bd_res_bed 
 *
 * @since 2016-08-30 10:00:42
 */
@Table(value="BD_RES_BED")
public class BdResBed extends BaseModule  {

	@PK
	@Field(value="PK_BED",id=KeyId.UUID)
    private String pkBed;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="DT_BEDTYPE")
    private String dtBedtype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_ITEM")
    private String pkItem;

	/** PK_ITEM_ADD 包床收费项目 */
	@Field(value="PK_ITEM_ADD")
    private String pkItemAdd;

	
	@Field(value="HOUSENO")
    private String houseno;

	@Field(value="DT_SEX")
    private String dtSex;

    /** FLAG_ACTIVE - Y:开放
	N:不开放 */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    /** FLAG_TEMP - Y:加床
	N:非加床 */
	@Field(value="FLAG_TEMP")
    private String flagTemp;

    /** EU_STATUS - 01:空闲
	02:占用
	03:整理
	04:预约 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** FLAG_OCUPY - Y：占用
	N：不占用 */
	@Field(value="FLAG_OCUPY")
    private String flagOcupy;

	@Field(value="PK_DEPT_USED")
    private String pkDeptUsed;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="CODE_FA")
    private String codeFa;

	@Field(value="NAME_PLACE")
    private String namePlace;

	@Field(value="PK_PHY_EMP")
    private String pkPhyEmp;

	@Field(value="PHY_EMP_NAME")
    private String phyEmpName;

	@Field(value="PK_NS_EMP")
    private String pkNsEmp;

	@Field(value="NS_EMP_NAME")
    private String nsEmpName;

	@Field(value="PK_WARD")
    private String pkWard;

	@Field(value="SORTNO")
    private String sortno;
	
    public String getPkBed(){
        return this.pkBed;
    }
    public void setPkBed(String pkBed){
        this.pkBed = pkBed;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getDtBedtype(){
        return this.dtBedtype;
    }
    public void setDtBedtype(String dtBedtype){
        this.dtBedtype = dtBedtype;
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

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getHouseno(){
        return this.houseno;
    }
    public void setHouseno(String houseno){
        this.houseno = houseno;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getFlagTemp(){
        return this.flagTemp;
    }
    public void setFlagTemp(String flagTemp){
        this.flagTemp = flagTemp;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagOcupy(){
        return this.flagOcupy;
    }
    public void setFlagOcupy(String flagOcupy){
        this.flagOcupy = flagOcupy;
    }

    public String getPkDeptUsed(){
        return this.pkDeptUsed;
    }
    public void setPkDeptUsed(String pkDeptUsed){
        this.pkDeptUsed = pkDeptUsed;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodeFa(){
        return this.codeFa;
    }
    public void setCodeFa(String codeFa){
        this.codeFa = codeFa;
    }

    public String getNamePlace(){
        return this.namePlace;
    }
    public void setNamePlace(String namePlace){
        this.namePlace = namePlace;
    }

    public String getPkPhyEmp(){
        return this.pkPhyEmp;
    }
    public void setPkPhyEmp(String pkPhyEmp){
        this.pkPhyEmp = pkPhyEmp;
    }

    public String getPhyEmpName(){
        return this.phyEmpName;
    }
    public void setPhyEmpName(String phyEmpName){
        this.phyEmpName = phyEmpName;
    }

    public String getPkNsEmp(){
        return this.pkNsEmp;
    }
    public void setPkNsEmp(String pkNsEmp){
        this.pkNsEmp = pkNsEmp;
    }

    public String getNsEmpName(){
        return this.nsEmpName;
    }
    public void setNsEmpName(String nsEmpName){
        this.nsEmpName = nsEmpName;
    }

    public String getPkWard(){
        return this.pkWard;
    }
    public void setPkWard(String pkWard){
        this.pkWard = pkWard;
    }
	public String getSortno() {
		return sortno;
	}
	public void setSortno(String sortno) {
		this.sortno = sortno;
	}
	public String getPkItemAdd() {
		return pkItemAdd;
	}
	public void setPkItemAdd(String pkItemAdd) {
		this.pkItemAdd = pkItemAdd;
	}
	
}