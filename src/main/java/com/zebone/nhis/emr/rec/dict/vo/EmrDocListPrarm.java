package com.zebone.nhis.emr.rec.dict.vo;

import java.util.Date;

/**
 * create by: gao shiheng
 *已科室质控提交、未归档病历vo
 * @Param: null
 * @return
 */
public class EmrDocListPrarm {

    //文档id
    private String pkRec;

    //患者病历记录ID
    private String pkPatrec;

    //文档名称
    private String name;

    //文档分类编码
    private String typeCode;
    
    //文档分类名称
    private String typeName;

    public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
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

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	//创建者
    private String createName;

    //住院医师
    private String referName;

    //病区
    private String pkWard;

    //科室
    private String pkDept;

    //病人id
    private String pkPi;

    private String pkPv;

    //住院医师
    private String flagCourse;

    //病人性别
    private String dtSex;

    //患者姓名
    private String namePi;

    //住院号
    private String codePi;

    //开始时间
    private Date beginDate;

    //截止时间
    private Date endDate;

    private String creator;

    //住院医师
    private String pkEmpRefeR;

    //审批意见
    private String approveTxt;

    //审批人
    private String pkEmpApprove;

    //审批科室
    private String pkDeptApprove;

    //审批时间
    private String approveDate;

    //就诊次数
    private Long times;

    //申请人
    private String pkEmpApply;

    //申请科室
    private String pkDeptApply;

    //申请时间
    private Date applyDate;

    //申请期限
    private long timeLimit;

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

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getPkRec() {
        return pkRec;
    }

    public void setPkRec(String pkRec) {
        this.pkRec = pkRec;
    }

    public String getPkPatrec() {
        return pkPatrec;
    }

    public void setPkPatrec(String pkPatrec) {
        this.pkPatrec = pkPatrec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getReferName() {
        return referName;
    }

    public void setReferName(String referName) {
        this.referName = referName;
    }

    public String getPkWard() {
        return pkWard;
    }

    public void setPkWard(String pkWard) {
        this.pkWard = pkWard;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getFlagCourse() {
        return flagCourse;
    }

    public void setFlagCourse(String flagCourse) {
        this.flagCourse = flagCourse;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPkEmpRefeR() {
        return pkEmpRefeR;
    }

    public void setPkEmpRefeR(String pkEmpRefeR) {
        this.pkEmpRefeR = pkEmpRefeR;
    }

    public String getApproveTxt() {
        return approveTxt;
    }

    public void setApproveTxt(String approveTxt) {
        this.approveTxt = approveTxt;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }
}
