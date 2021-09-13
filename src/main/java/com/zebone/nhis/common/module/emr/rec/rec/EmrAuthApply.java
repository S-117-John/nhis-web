package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_AUTH_APPLY 
 *
 * @since 2020-11-12 03:31:23
 */
@Table(value="EMR_AUTH_APPLY")
public class EmrAuthApply  extends BaseModule {

	@PK
	@Field(value="PK_APPLY",id=KeyId.UUID)
    private String pkApply;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="TIMES")
    private String times;

	@Field(value="EU_PVTYP")
    private String euPvtyp;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_REC")
    private String pkRec;

	@Field(value="PK_EMP_APPLY")
    private String pkEmpApply;

	@Field(value="NAME_EMP_APPLY")
    private String nameEmpApply;

	@Field(value="PK_DEPT_APPLY")
    private String pkDeptApply;

	@Field(value="APP_DATE")
    private Date appDate;

	@Field(value="APP_TXT")
    private String appTxt;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_APPROVE")
    private String pkEmpApprove;

	@Field(value="PK_DEPT_APPROVE")
    private String pkDeptApprove;

	@Field(value="APPROVE_DATE")
    private Date approveDate;

	@Field(value="APPROVE_TXT")
    private String approveTxt;

	@Field(value="REMARK")
    private String remark;


    public String getPkApply(){
        return this.pkApply;
    }
    public void setPkApply(String pkApply){
        this.pkApply = pkApply;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getTimes(){
        return this.times;
    }
    public void setTimes(String times){
        this.times = times;
    }

    public String getEuPvtyp(){
        return this.euPvtyp;
    }
    public void setEuPvtyp(String euPvtyp){
        this.euPvtyp = euPvtyp;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getPkRec(){
        return this.pkRec;
    }
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
    }

    public String getPkEmpApply(){
        return this.pkEmpApply;
    }
    public void setPkEmpApply(String pkEmpApply){
        this.pkEmpApply = pkEmpApply;
    }

    public String getNameEmpApply(){
        return this.nameEmpApply;
    }
    public void setNameEmpApply(String nameEmpApply){
        this.nameEmpApply = nameEmpApply;
    }

    public String getPkDeptApply(){
        return this.pkDeptApply;
    }
    public void setPkDeptApply(String pkDeptApply){
        this.pkDeptApply = pkDeptApply;
    }

    public Date getAppDate(){
        return this.appDate;
    }
    public void setAppDate(Date appDate){
        this.appDate = appDate;
    }

    public String getAppTxt(){
        return this.appTxt;
    }
    public void setAppTxt(String appTxt){
        this.appTxt = appTxt;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpApprove(){
        return this.pkEmpApprove;
    }
    public void setPkEmpApprove(String pkEmpApprove){
        this.pkEmpApprove = pkEmpApprove;
    }

    public String getPkDeptApprove(){
        return this.pkDeptApprove;
    }
    public void setPkDeptApprove(String pkDeptApprove){
        this.pkDeptApprove = pkDeptApprove;
    }

    public Date getApproveDate(){
        return this.approveDate;
    }
    public void setApproveDate(Date approveDate){
        this.approveDate = approveDate;
    }

    public String getApproveTxt(){
        return this.approveTxt;
    }
    public void setApproveTxt(String approveTxt){
        this.approveTxt = approveTxt;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
}