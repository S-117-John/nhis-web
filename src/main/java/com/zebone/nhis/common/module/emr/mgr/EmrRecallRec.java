package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_RECALL_REC 
 *
 * @since 2017-12-11 04:14:06
 */
@Table(value="EMR_RECALL_REC")
public class EmrRecallRec extends BaseModule  {

	@PK
	@Field(value="PK_RECALL",id=KeyId.UUID)
    private String pkRecall;

    /** PK_PV - 评分记录ID */
	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PATREC")
    private String pkPatrec;

	@Field(value="PK_EMP_APP")
    private String pkEmpApp;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="APPLY_REASON")
    private String applyReason;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="DATE_RECALL")
    private Date dateRecall;

    /** EU_STATUS - 0：申请
   1：通过
   2：拒绝 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="REMARK")
    private String remark;


    public String getPkRecall(){
        return this.pkRecall;
    }
    public void setPkRecall(String pkRecall){
        this.pkRecall = pkRecall;
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

    public String getPkPatrec(){
        return this.pkPatrec;
    }
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
    }

    public String getPkEmpApp(){
        return this.pkEmpApp;
    }
    public void setPkEmpApp(String pkEmpApp){
        this.pkEmpApp = pkEmpApp;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getApplyReason(){
        return this.applyReason;
    }
    public void setApplyReason(String applyReason){
        this.applyReason = applyReason;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public Date getDateRecall(){
        return this.dateRecall;
    }
    public void setDateRecall(Date dateRecall){
        this.dateRecall = dateRecall;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
}