package com.zebone.nhis.common.module.labor.nis;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_LABOR - PV_LABOR 
 *
 * @since 2017-05-18 03:36:01
 */
@Table(value="PV_LABOR")
public class PvLabor  extends BaseModule{

	@PK
	@Field(value="PK_PVLABOR",id=KeyId.UUID)
    private String pkPvlabor;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PILABOR")
    private String pkPilabor;

	@Field(value="BED_NO")
    private String bedNo;

    /** EU_STATUS - 0：转入申请，1：就诊中，2：就诊结束 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_IN")
    private Date dateIn;

	@Field(value="DATE_PREBIRTH")
    private Date datePrebirth;

	@Field(value="LAB_WEEK")
    private Double labWeek;

	@Field(value="DATE_OUT")
    private Date dateOut;

	@Field(value="PK_EMP_DOCTOR")
    private String pkEmpDoctor;

	@Field(value="NAME_EMP_DOCTOR")
    private String nameEmpDoctor;

	@Field(value="PK_EMP_NURSE")
    private String pkEmpNurse;

	@Field(value="NAME_EMP_NURSE")
    private String nameEmpNurse;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="PK_DEPT_SRC")
    private String pkDeptSrc;

	@Field(value="PK_DEPT_NS_SRC")
    private String pkDeptNsSrc;

	@Field(value="FLAG_IN")
    private String flagIn;

	@Field(value="NOTE")
    private String note;

	@Field(value="OUT_REASON")
    private String outReason;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    @Field(value="FLAG_INFANT")
	private String flagInfant;

    @Field(value="PK_PVLABOR_MOTHER")
    private String pkPvlaborMother;

    public String getPkPvlabor(){
        return this.pkPvlabor;
    }
    public void setPkPvlabor(String pkPvlabor){
        this.pkPvlabor = pkPvlabor;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPilabor(){
        return this.pkPilabor;
    }
    public void setPkPilabor(String pkPilabor){
        this.pkPilabor = pkPilabor;
    }

    public String getBedNo(){
        return this.bedNo;
    }
    public void setBedNo(String bedNo){
        this.bedNo = bedNo;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateIn(){
        return this.dateIn;
    }
    public void setDateIn(Date dateIn){
        this.dateIn = dateIn;
    }

    public Date getDatePrebirth(){
        return this.datePrebirth;
    }
    public void setDatePrebirth(Date datePrebirth){
        this.datePrebirth = datePrebirth;
    }

    public Double getLabWeek(){
        return this.labWeek;
    }
    public void setLabWeek(Double labWeek){
        this.labWeek = labWeek;
    }

    public Date getDateOut(){
        return this.dateOut;
    }
    public void setDateOut(Date dateOut){
        this.dateOut = dateOut;
    }

    public String getPkEmpDoctor(){
        return this.pkEmpDoctor;
    }
    public void setPkEmpDoctor(String pkEmpDoctor){
        this.pkEmpDoctor = pkEmpDoctor;
    }

    public String getNameEmpDoctor(){
        return this.nameEmpDoctor;
    }
    public void setNameEmpDoctor(String nameEmpDoctor){
        this.nameEmpDoctor = nameEmpDoctor;
    }

    public String getPkEmpNurse(){
        return this.pkEmpNurse;
    }
    public void setPkEmpNurse(String pkEmpNurse){
        this.pkEmpNurse = pkEmpNurse;
    }

    public String getNameEmpNurse(){
        return this.nameEmpNurse;
    }
    public void setNameEmpNurse(String nameEmpNurse){
        this.nameEmpNurse = nameEmpNurse;
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

    public String getPkDeptSrc(){
        return this.pkDeptSrc;
    }
    public void setPkDeptSrc(String pkDeptSrc){
        this.pkDeptSrc = pkDeptSrc;
    }

    public String getPkDeptNsSrc(){
        return this.pkDeptNsSrc;
    }
    public void setPkDeptNsSrc(String pkDeptNsSrc){
        this.pkDeptNsSrc = pkDeptNsSrc;
    }

    public String getFlagIn(){
        return this.flagIn;
    }
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getOutReason(){
        return this.outReason;
    }
    public void setOutReason(String outReason){
        this.outReason = outReason;
    }
   
    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getFlagInfant() {return flagInfant;}
    public void setFlagInfant(String flagInfant) {this.flagInfant = flagInfant;}

    public String getPkPvlaborMother() {return pkPvlaborMother;}
    public void setPkPvlaborMother(String pkPvlaborMother) {this.pkPvlaborMother = pkPvlaborMother;}
}