package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_BED  - 患者就诊_床位记录 
 *
 * @since 2016-09-23 11:01:25
 */
@Table(value="PV_BED")
public class PvBed extends BaseModule  {

	@PK
	@Field(value="PK_PVBED",id=KeyId.UUID)
    private String pkPvbed;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="BEDNO")
    private String bedno;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_BED_WARD")
    private String pkBedWard;

    /** EU_HOLD - 0正常 1包床 */
	@Field(value="EU_HOLD")
    private String euHold;

	@Field(value="PK_EMP_BEGIN")
    private String pkEmpBegin;

	@Field(value="NAME_EMP_BEGIN")
    private String nameEmpBegin;

	@Field(value="PK_EMP_END")
    private String pkEmpEnd;

	@Field(value="NAME_EMP_END")
    private String nameEmpEnd;

    /** FLAG_MAJ - 1主床位,入科接收时安排的床位为主床位 */
	@Field(value="FLAG_MAJ")
    private String flagMaj;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkPvbed(){
        return this.pkPvbed;
    }
    public void setPkPvbed(String pkPvbed){
        this.pkPvbed = pkPvbed;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public String getBedno(){
        return this.bedno;
    }
    public void setBedno(String bedno){
        this.bedno = bedno;
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

    public String getPkBedWard(){
        return this.pkBedWard;
    }
    public void setPkBedWard(String pkBedWard){
        this.pkBedWard = pkBedWard;
    }

    public String getEuHold(){
        return this.euHold;
    }
    public void setEuHold(String euHold){
        this.euHold = euHold;
    }

    public String getPkEmpBegin(){
        return this.pkEmpBegin;
    }
    public void setPkEmpBegin(String pkEmpBegin){
        this.pkEmpBegin = pkEmpBegin;
    }

    public String getNameEmpBegin(){
        return this.nameEmpBegin;
    }
    public void setNameEmpBegin(String nameEmpBegin){
        this.nameEmpBegin = nameEmpBegin;
    }

    public String getPkEmpEnd(){
        return this.pkEmpEnd;
    }
    public void setPkEmpEnd(String pkEmpEnd){
        this.pkEmpEnd = pkEmpEnd;
    }

    public String getNameEmpEnd(){
        return this.nameEmpEnd;
    }
    public void setNameEmpEnd(String nameEmpEnd){
        this.nameEmpEnd = nameEmpEnd;
    }

    public String getFlagMaj(){
        return this.flagMaj;
    }
    public void setFlagMaj(String flagMaj){
        this.flagMaj = flagMaj;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}