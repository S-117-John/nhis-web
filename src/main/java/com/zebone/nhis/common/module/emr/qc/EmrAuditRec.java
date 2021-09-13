package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

public class EmrAuditRec {
    private String pkAudit;

    private String pkOrg;

    private String pkPv;

    private String pkRec;

    private String euLevel;

    private String content;

    private String pkEmpApp;

    private Date dateApp;

    private String pkEmpAudit;

    private String euStatus;

    private Date dateReceive;

    private Date dateFinish;

    private String remark;

    private String delFlag;

    private String creator;

    private Date createTime;

    private Date ts;

    private String status;
    
    private EmrAuditDoc doc;
    
    
    public EmrAuditDoc getDoc() {
		return doc;
	}

	public void setDoc(EmrAuditDoc doc) {
		this.doc = doc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPkAudit() {
        return pkAudit;
    }

    public void setPkAudit(String pkAudit) {
        this.pkAudit = pkAudit == null ? null : pkAudit.trim();
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg == null ? null : pkOrg.trim();
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv == null ? null : pkPv.trim();
    }

    public String getPkRec() {
        return pkRec;
    }

    public void setPkRec(String pkRec) {
        this.pkRec = pkRec == null ? null : pkRec.trim();
    }

    public String getEuLevel() {
        return euLevel;
    }

    public void setEuLevel(String euLevel) {
        this.euLevel = euLevel == null ? null : euLevel.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPkEmpApp() {
        return pkEmpApp;
    }

    public void setPkEmpApp(String pkEmpApp) {
        this.pkEmpApp = pkEmpApp == null ? null : pkEmpApp.trim();
    }

    public Date getDateApp() {
        return dateApp;
    }

    public void setDateApp(Date dateApp) {
        this.dateApp = dateApp;
    }

    public String getPkEmpAudit() {
        return pkEmpAudit;
    }

    public void setPkEmpAudit(String pkEmpAudit) {
        this.pkEmpAudit = pkEmpAudit == null ? null : pkEmpAudit.trim();
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus == null ? null : euStatus.trim();
    }

    public Date getDateReceive() {
        return dateReceive;
    }

    public void setDateReceive(Date dateReceive) {
        this.dateReceive = dateReceive;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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