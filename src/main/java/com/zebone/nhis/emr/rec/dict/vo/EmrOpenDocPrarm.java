package com.zebone.nhis.emr.rec.dict.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * create by: gao shiheng
 *
 * @Param: null
 * @return
 */
@Table(value="emr_open_edit_doc")
public class EmrOpenDocPrarm {

    @PK
    @Field(value="pk_edit_doc",id= Field.KeyId.UUID)
    private String pkEditDoc;

    @Field(value="pk_org")
    private String pkOrg;

    @Field(value="pk_edit_rec")
    private String pkEditRec;

    @Field(value="pk_rec")
    private String pkRec;

    @Field(value="eu_type")
    private String euType;

    @Field(value="pk_emp_apply")
    private String pkEmpApply;

    @Field(value="pk_dept_apply")
    private String pkDeptApply;

    @Field(value="apply_date")
    private Date applyDate;


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

    @Field(value="eu_edit_type")
    private String euEditType;
    
    @Field(value="type_code")
    private String typeCode;
    
    public String getEuEditType() {
		return euEditType;
	}

	public void setEuEditType(String euEditType) {
		this.euEditType = euEditType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

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

    public String getPkEditDoc() {
        return pkEditDoc;
    }

    public void setPkEditDoc(String pkEditDoc) {
        this.pkEditDoc = pkEditDoc;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkEditRec() {
        return pkEditRec;
    }

    public void setPkEditRec(String pkEditRec) {
        this.pkEditRec = pkEditRec;
    }

    public String getPkRec() {
        return pkRec;
    }

    public void setPkRec(String pkRec) {
        this.pkRec = pkRec;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setTs(Date ts) {
        this.ts = ts;
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

    public Date getApproveDate() {
        return approveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getTs() {
        return ts;
    }
}
