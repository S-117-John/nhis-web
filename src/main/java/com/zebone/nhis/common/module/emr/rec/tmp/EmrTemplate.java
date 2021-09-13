package com.zebone.nhis.common.module.emr.rec.tmp;

import java.util.Date;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;

/**
 *
 * @author 
 */
public class EmrTemplate{
    /**
     * 
     */
    private String pkTmp;
    /**
     * 
     */
    private String pkOrg;
    
    private String code;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private String describe;
    /**
     * 
     */
    private String typeCode;
    /**
     * 0：全院
1：科室
2：个人
     */
    private Short euTmpLevel;
    /**
     * 
     */
    private String tmpTitle;
    
    private String textTitle;
    
    /**
     * 
     */
    private Integer sortCode;
    /**
     * 
     */
    private String pyCode;
    /**
     * 
     */
    private String dCode;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkWard;
    /**
     * 0、新建
1、医生审核
2、科主任审核
3、医院审核
     */
    private String euStatus;
    /**
     * 0：不需要审签
1：需要审签
     */
    private String flagAudit;
    /**
     * 
     */
    private Short auditLevel;
    
    private String AuditLevelSet;
    
    /**
     * 0：不限制
>0 限制次数
     */
    private Short limitNum;
    /**
     * 
     */
    private String flagDefault;
    /**
     * 
     */
    private String flagOpen;
    
    private String flagMerge;
    
    /**
     * 
     */
    private String pkEmp;
    
    private String flagSys;
    
    private String euType;
    
    private String flagExt;
    
    private String flagPub;
    /**
     * 
     */
    private String delFlag;
    /**
     * BLOB
     */
    private byte[] docData;
    /**
     * CLOB
     */
    private String docXml;
    /**
     * CLOB
     */
    private String associationRules;
    
    private String tmpPath;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;

    private String status;
    
    private EmrDocType docType;
    
    private String pkTmpOld;
    
    private String flagGauge;
    
    private String flagDeptSet;
    
    private String typeName;
    
    private String nameEmp;
    
    

	public String getFlagPub() {
		return flagPub;
	}

	public void setFlagPub(String flagPub) {
		this.flagPub = flagPub;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	/**
     * 
     */
    public String getPkTmp(){
        return this.pkTmp;
    }

    /**
     * 
     */
    public void setPkTmp(String pkTmp){
        this.pkTmp = pkTmp;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getName(){
        return this.name;
    }

    /**
     * 
     */
    public void setName(String name){
        this.name = name;
    }    
    /**
     * 
     */
    public String getDescribe(){
        return this.describe;
    }

    /**
     * 
     */
    public void setDescribe(String describe){
        this.describe = describe;
    }    
    /**
     * 
     */
    public String getTypeCode(){
        return this.typeCode;
    }

    /**
     * 
     */
    public void setTypeCode(String typeCode){
        this.typeCode = typeCode;
    }    
    /**
     * 0：全院
1：科室
2：个人
     */
    public Short getEuTmpLevel(){
        return this.euTmpLevel;
    }

    /**
     * 0：全院
1：科室
2：个人
     */
    public void setEuTmpLevel(Short euTmpLevel){
        this.euTmpLevel = euTmpLevel;
    }    
    /**
     * 
     */
    public String getTmpTitle(){
        return this.tmpTitle;
    }

    /**
     * 
     */
    public void setTmpTitle(String tmpTitle){
        this.tmpTitle = tmpTitle;
    }    
    /**
     * 
     */
    public Integer getSortCode(){
        return this.sortCode;
    }

    /**
     * 
     */
    public void setSortCode(Integer sortCode){
        this.sortCode = sortCode;
    }    
    /**
     * 
     */
    public String getPyCode(){
        return this.pyCode;
    }

    /**
     * 
     */
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }    
    /**
     * 
     */
    public String getdCode(){
        return this.dCode;
    }

    /**
     * 
     */
    public void setdCode(String dCode){
        this.dCode = dCode;
    }    
    /**
     * 
     */
    public String getPkDept(){
        return this.pkDept;
    }

    /**
     * 
     */
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }    
    /**
     * 
     */
    public String getPkWard(){
        return this.pkWard;
    }

    /**
     * 
     */
    public void setPkWard(String pkWard){
        this.pkWard = pkWard;
    }    
    /**
     * 0、新建
1、医生审核
2、科主任审核
3、医院审核
     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 0、新建
1、医生审核
2、科主任审核
3、医院审核
     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * 0：不需要审签
1：需要审签
     */
    public String getFlagAudit(){
        return this.flagAudit;
    }

    /**
     * 0：不需要审签
1：需要审签
     */
    public void setFlagAudit(String flagAudit){
        this.flagAudit = flagAudit;
    }    
    /**
     * 
     */
    public Short getAuditLevel(){
        return this.auditLevel;
    }

    /**
     * 
     */
    public void setAuditLevel(Short auditLevel){
        this.auditLevel = auditLevel;
    }    
    /**
     * 0：不限制
>0 限制次数
     */
    public Short getLimitNum(){
        return this.limitNum;
    }

    /**
     * 0：不限制
>0 限制次数
     */
    public void setLimitNum(Short limitNum){
        this.limitNum = limitNum;
    }    
    /**
     * 
     */
    public String getFlagDefault(){
        return this.flagDefault;
    }

    /**
     * 
     */
    public void setFlagDefault(String flagDefault){
        this.flagDefault = flagDefault;
    }    
    /**
     * 
     */
    public String getFlagOpen(){
        return this.flagOpen;
    }

    /**
     * 
     */
    public void setFlagOpen(String flagOpen){
        this.flagOpen = flagOpen;
    }    
    /**
     * 
     */
    public String getPkEmp(){
        return this.pkEmp;
    }

    /**
     * 
     */
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * BLOB
     */
    public byte[] getDocData(){
        return this.docData;
    }

    /**
     * BLOB
     */
    public void setDocData(byte[] docData){
        this.docData = docData;
    }    
    /**
     * CLOB
     */
    public String getDocXml(){
        return this.docXml;
    }

    /**
     * CLOB
     */
    public void setDocXml(String docXml){
        this.docXml = docXml;
    }    
    /**
     * CLOB
     */
    public String getAssociationRules(){
        return this.associationRules;
    }

    /**
     * CLOB
     */
    public void setAssociationRules(String associationRules){
        this.associationRules = associationRules;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }

	public String getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}

	public String getAuditLevelSet() {
		return AuditLevelSet;
	}

	public void setAuditLevelSet(String auditLevelSet) {
		AuditLevelSet = auditLevelSet;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmrDocType getDocType() {
		return docType;
	}

	public void setDocType(EmrDocType docType) {
		this.docType = docType;
	}

	public String getFlagMerge() {
		return flagMerge;
	}

	public void setFlagMerge(String flagMerge) {
		this.flagMerge = flagMerge;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFlagSys() {
		return flagSys;
	}

	public void setFlagSys(String flagSys) {
		this.flagSys = flagSys;
	}

	public String getPkTmpOld() {
		return pkTmpOld;
	}

	public void setPkTmpOld(String pkTmpOld) {
		this.pkTmpOld = pkTmpOld;
	}

	public String getFlagGauge() {
		return flagGauge;
	}

	public void setFlagGauge(String flagGauge) {
		this.flagGauge = flagGauge;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getFlagExt() {
		return flagExt;
	}

	public void setFlagExt(String flagExt) {
		this.flagExt = flagExt;
	}

	public String getFlagDeptSet() {
		return flagDeptSet;
	}

	public void setFlagDeptSet(String flagDeptSet) {
		this.flagDeptSet = flagDeptSet;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}


    
}