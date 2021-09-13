package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

public class EmrAuditDoc {
    private String pkAuditDoc;

    private String pkAudit;

    private String remark;

    private String delFlag;

    private String creator;

    private Date createTime;

    private Date ts;

    private byte[] docDataBak;

    private String docXmlBak;

    private String cdaXmlBak;

    private String docCaBak;

    private byte[] docData;

    private String docXml;

    private String cdaXml;

    private String docCa;

    private String status;
    
    private String dbName;
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getDocDataBak() {
        return docDataBak;
    }

    public void setDocDataBak(byte[] docDataBak) {
        this.docDataBak = docDataBak;
    }

    public String getDocXmlBak() {
        return docXmlBak;
    }

    public void setDocXmlBak(String docXmlBak) {
        this.docXmlBak = docXmlBak == null ? null : docXmlBak.trim();
    }

    public String getCdaXmlBak() {
        return cdaXmlBak;
    }

    public void setCdaXmlBak(String cdaXmlBak) {
        this.cdaXmlBak = cdaXmlBak == null ? null : cdaXmlBak.trim();
    }

    public String getDocCaBak() {
        return docCaBak;
    }

    public void setDocCaBak(String docCaBak) {
        this.docCaBak = docCaBak == null ? null : docCaBak.trim();
    }

    public byte[] getDocData() {
        return docData;
    }

    public void setDocData(byte[] docData) {
        this.docData = docData;
    }

    public String getDocXml() {
        return docXml;
    }

    public void setDocXml(String docXml) {
        this.docXml = docXml == null ? null : docXml.trim();
    }

    public String getCdaXml() {
        return cdaXml;
    }

    public void setCdaXml(String cdaXml) {
        this.cdaXml = cdaXml == null ? null : cdaXml.trim();
    }

    public String getDocCa() {
        return docCa;
    }

    public void setDocCa(String docCa) {
        this.docCa = docCa == null ? null : docCa.trim();
    }
    
    public String getPkAuditDoc() {
        return pkAuditDoc;
    }

    public void setPkAuditDoc(String pkAuditDoc) {
        this.pkAuditDoc = pkAuditDoc == null ? null : pkAuditDoc.trim();
    }

    public String getPkAudit() {
        return pkAudit;
    }

    public void setPkAudit(String pkAudit) {
        this.pkAudit = pkAudit == null ? null : pkAudit.trim();
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

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
    
}