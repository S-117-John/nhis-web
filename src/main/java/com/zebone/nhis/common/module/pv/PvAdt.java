package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_ADT  - 患者就诊-转科 
 *
 * @since 2016-09-19 01:07:33
 */
@Table(value="PV_ADT")
public class PvAdt extends BaseModule  {

	@PK
	@Field(value="PK_ADT",id=KeyId.UUID)
    private String pkAdt;

	@Field(value="PK_PV")
    private String pkPv;

    /** DATE_BEGIN - 转科操作时，目的科室记录写入日期 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 转科操作时，源科室记录写入日期 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** PK_EMP_BEGIN - 转科操作时，目的科室记录写入操作人员 */
	@Field(value="PK_EMP_BEGIN")
    private String pkEmpBegin;

	@Field(value="NAME_EMP_BEGIN")
    private String nameEmpBegin;

    /** PK_EMP_END - 转科操作时，源科室记录写入操作人员 */
	@Field(value="PK_EMP_END")
    private String pkEmpEnd;

	@Field(value="NAME_EMP_END")
    private String nameEmpEnd;

	@Field(value="FLAG_ADMIT")
    private String flagAdmit;

	@Field(value="FLAG_DIS")
    private String flagDis;

	@Field(value="DT_PATIEVA")
    private String dtPatieva;

	@Field(value="DESC_PATIEVA")
    private String descPatieva;

    /** PK_ADT_SOURCE - 对应于源转科记录主键，对入院来说为空。连接源科室的记录 */
	@Field(value="PK_ADT_SOURCE")
    private String pkAdtSource;

    /** EU_STATUS - 0申请 1应答 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value = "FLAG_NONE")
	private String flagNone;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkAdt(){
        return this.pkAdt;
    }
    public void setPkAdt(String pkAdt){
        this.pkAdt = pkAdt;
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

    public String getFlagAdmit(){
        return this.flagAdmit;
    }
    public void setFlagAdmit(String flagAdmit){
        this.flagAdmit = flagAdmit;
    }

    public String getFlagDis(){
        return this.flagDis;
    }
    public void setFlagDis(String flagDis){
        this.flagDis = flagDis;
    }

    public String getDtPatieva(){
        return this.dtPatieva;
    }
    public void setDtPatieva(String dtPatieva){
        this.dtPatieva = dtPatieva;
    }

    public String getDescPatieva(){
        return this.descPatieva;
    }
    public void setDescPatieva(String descPatieva){
        this.descPatieva = descPatieva;
    }

    public String getPkAdtSource(){
        return this.pkAdtSource;
    }
    public void setPkAdtSource(String pkAdtSource){
        this.pkAdtSource = pkAdtSource;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getFlagNone() {return flagNone;}
    public void setFlagNone(String flagNone) {this.flagNone = flagNone;}
}
