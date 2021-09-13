package com.zebone.nhis.emr.rec.dict.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zebone.nhis.ma.pub.platform.syx.vo.DirectTarget;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * create by: gao shiheng
 *Table: emr_open_edit_rec
 * @Param: null
 * @return
 */
@Table(value="emr_open_edit_rec")
public class EmrOpenRecPrarm {

    @PK
    @Field(value="pk_edit_rec",id= Field.KeyId.UUID)
    private String pkEditRec;

    @Field(value="pk_org")
    private String pkOrg;

    @Field(value="pk_pv")
    private String pkPv;

    @Field(value="pk_pi")
    private String pkPi;

    @Field(value="times")
    private Long times;

    @Field(value="eu_type")
    private String euType;

    @Field(value="pk_emp_apply")
    private String pkEmpApply;

    @Field(value="pk_dept_apply")
    private String pkDeptApply;

    @Field(value="apply_date")
    private Date applyDate;

    @Field(value="time_limit")
    private Long timeLimit;

    @Field(value="begin_date")
    private Date beginDate;

    @Field(value="end_date")
    private Date endDate;

    @Field(value="apply_txt")
    private String applyTxt;

    @Field(value="eu_status")
    private String euStatus;

    @Field(value="pk_emp_approve")
    private String pkEmpApprove;

    @Field(value="pk_dept_approve")
    private String pkDeptApprove;

    @Field(value="approve_date")
    private Date approveDate;

    @Field(value="approve_txt")
    private String approveTxt;

    @Field(value="del_flag")
    private String delFlag;

    @Field(value="remark")
    private String remark;

    @Field(value="creator")
    private String creator;

    @Field(value="create_time")
    private Date createTime;

    @Field(value="ts")
    private Date ts;

    public String getPkEditRec() {
        return pkEditRec;
    }

    public void setPkEditRec(String pkEditRec) {
        this.pkEditRec = pkEditRec;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public String getPkEmpApply() {
        return pkEmpApply;
    }

    public void setPkEmpApply(String pkEmpApply) {
        this.pkEmpApply = pkEmpApply;
    }

    public String getPkDeptApply() {
        return pkDeptApply;
    }

    public void setPkDeptApply(String pkDeptApply) {
        this.pkDeptApply = pkDeptApply;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getApplyTxt() {
        return applyTxt;
    }

    public void setApplyTxt(String applyTxt) {
        this.applyTxt = applyTxt;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getPkEmpApprove() {
        return pkEmpApprove;
    }

    public void setPkEmpApprove(String pkEmpApprove) {
        this.pkEmpApprove = pkEmpApprove;
    }

    public String getPkDeptApprove() {
        return pkDeptApprove;
    }

    public void setPkDeptApprove(String pkDeptApprove) {
        this.pkDeptApprove = pkDeptApprove;
    }

    public String getApproveTxt() {
        return approveTxt;
    }

    public void setApproveTxt(String approveTxt) {
        this.approveTxt = approveTxt;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
