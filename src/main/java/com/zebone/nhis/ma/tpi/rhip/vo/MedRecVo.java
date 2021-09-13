package com.zebone.nhis.ma.tpi.rhip.vo;

import java.sql.Timestamp;
import java.util.Date;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;


/**
 * @author chengjia
 *
 */
public class MedRecVo{
    /**
     * 
     */
    private String pkRec;
    /**
     * 
     */
    private String pkPatrec;
    /**
     * 
     */
    private String name;
    /**
     * 根据序号发生器产生
     */
    private Long seqNo;
    /**
     * 
     */
    private Date recDate;
    /**
     * 
     */
    private String describe;
    /**
     * 
     */
    private String pkPi;
    /**
     * 
     */
    private Integer times;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkWard;
    /**
     * 
     */
    private String typeCode;
    /**
     * 
     */
    private String pkTmp;
    /**
     * 
     */
    private String pkDoc;
    /**
     * 
     */
    private String flagAudit;
    /**
     * 
     */
    private Short euAuditLevel;
    
    private String auditLevelSet;
    
    /**
     * 0:书写
1：住院医师
2：主治医师
3：主任医师

     */
    private String euDocStatus;
    /**
     * 0：未完
1：完成

     */
    private String euAuditStatus;
    
    private String flagAuditFinish;
    
    private Date submitDate;
    
    private String pkEmpIntern;
    
    private Date internSignDate;
    
    private String pkEmpReferAct;
    
    /**
     * 
     */
    private String pkEmpRefer;
    
    private String referEmpCode;
    
    private String referEmpName;
    
    /**
     * 
     */
    
    private Date referAuditDate;
    
    
    private Date referSignDate;
    /**
     * 
     */
    private String pkEmpConsultAct;
    /**
     * 
     */
    private String pkEmpConsult;
    
    private String consultEmpCode;
    
    private String consultEmpName;
    
    /**
     * 
     */
    private Date consultAuditDate;
    /**
     * 
     */
    private Date consultSignDate;
    /**
     * 
     */
    private String pkEmpDirectorAct;
    /**
     * 
     */
    private String pkEmpDirector;
    
    private String directorEmpCode;
    
    private String directorEmpName;
    
    /**
     * 
     */
    private Date directorAuditDate;
    /**
     * 
     */
    private Date directorSignDate;
    /**
     * 
     */
    private byte[] docData;
    /**
     * 
     */
    private String docXml;
    /**
     * 
     */
    private String delFlag;
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
    
    
    private EmrTemplate template;
    
    private EmrDocType docType;
    
    private String createName;
    
    private EmrMedDoc medDoc;
    
    /**
     * 
     */
    public String getPkRec(){
        return this.pkRec;
    }

    /**
     * 
     */
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
    }    
    /**
     * 
     */
    public String getPkPatrec(){
        return this.pkPatrec;
    }

    /**
     * 
     */
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
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
     * 根据序号发生器产生
     */
    public Long getSeqNo(){
        return this.seqNo;
    }

    /**
     * 根据序号发生器产生
     */
    public void setSeqNo(Long seqNo){
        this.seqNo = seqNo;
    }    
    /**
     * 
     */

    /**
     * 
     */
    public String getDescribe(){
        return this.describe;
    }



	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
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
    public String getPkPi(){
        return this.pkPi;
    }

    /**
     * 
     */
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }    
    /**
     * 
     */
    public Integer getTimes(){
        return this.times;
    }

    /**
     * 
     */
    public void setTimes(Integer times){
        this.times = times;
    }    
    /**
     * 
     */
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
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
    public String getPkDoc(){
        return this.pkDoc;
    }

    /**
     * 
     */
    public void setPkDoc(String pkDoc){
        this.pkDoc = pkDoc;
    }    
    /**
     * 
     */
    public String getFlagAudit(){
        return this.flagAudit;
    }

    /**
     * 
     */
    public void setFlagAudit(String flagAudit){
        this.flagAudit = flagAudit;
    }    
    /**
     * 
     */
    public Short getEuAuditLevel(){
        return this.euAuditLevel;
    }

    /**
     * 
     */
    public void setEuAuditLevel(Short euAuditLevel){
        this.euAuditLevel = euAuditLevel;
    }    
    /**
     * 0:书写
1：住院医师
2：主治医师
3：主任医师

     */
    public String getEuDocStatus(){
        return this.euDocStatus;
    }

    /**
     * 0:书写
1：住院医师
2：主治医师
3：主任医师

     */
    public void setEuDocStatus(String euDocStatus){
        this.euDocStatus = euDocStatus;
    }    
    /**
     * 0：未完
1：完成

     */
    public String getEuAuditStatus(){
        return this.euAuditStatus;
    }

    /**
     * 0：未完
1：完成

     */
    public void setEuAuditStatus(String euAuditStatus){
        this.euAuditStatus = euAuditStatus;
    }    
    /**
     * 
     */
    public String getPkEmpRefer(){
        return this.pkEmpRefer;
    }

    /**
     * 
     */
    public void setPkEmpRefer(String pkEmpRefer){
        this.pkEmpRefer = pkEmpRefer;
    }    
    /**
     * 
     */
    public Date getReferSignDate(){
        return this.referSignDate;
    }

    /**
     * 
     */
    public void setReferSignDate(Date referSignDate){
        this.referSignDate = referSignDate;
    }    
    /**
     * 
     */
    public String getPkEmpConsultAct(){
        return this.pkEmpConsultAct;
    }

    /**
     * 
     */
    public void setPkEmpConsultAct(String pkEmpConsultAct){
        this.pkEmpConsultAct = pkEmpConsultAct;
    }    
    /**
     * 
     */
    public String getPkEmpConsult(){
        return this.pkEmpConsult;
    }

    /**
     * 
     */
    public void setPkEmpConsult(String pkEmpConsult){
        this.pkEmpConsult = pkEmpConsult;
    }    
    /**
     * 
     */
    public Date getConsultAuditDate(){
        return this.consultAuditDate;
    }

    /**
     * 
     */
    public void setConsultAuditDate(Date consultAuditDate){
        this.consultAuditDate = consultAuditDate;
    }    
    /**
     * 
     */
    public Date getConsultSignDate(){
        return this.consultSignDate;
    }

    /**
     * 
     */
    public void setConsultSignDate(Date consultSignDate){
        this.consultSignDate = consultSignDate;
    }    
    /**
     * 
     */
    public String getPkEmpDirectorAct(){
        return this.pkEmpDirectorAct;
    }

    /**
     * 
     */
    public void setPkEmpDirectorAct(String pkEmpDirectorAct){
        this.pkEmpDirectorAct = pkEmpDirectorAct;
    }    
    /**
     * 
     */
    public String getPkEmpDirector(){
        return this.pkEmpDirector;
    }

    /**
     * 
     */
    public void setPkEmpDirector(String pkEmpDirector){
        this.pkEmpDirector = pkEmpDirector;
    }    
    /**
     * 
     */
    public Date getDirectorAuditDate(){
        return this.directorAuditDate;
    }

    /**
     * 
     */
    public void setDirectorAuditDate(Date directorAuditDate){
        this.directorAuditDate = directorAuditDate;
    }    
    /**
     * 
     */
    public Date getDirectorSignDate(){
        return this.directorSignDate;
    }

    /**
     * 
     */
    public void setDirectorSignDate(Date directorSignDate){
        this.directorSignDate = directorSignDate;
    }    
    /**
     * 
     */
    public byte[] getDocData(){
        return this.docData;
    }

    /**
     * 
     */
    public void setDocData(byte[] docData){
        this.docData = docData;
    }    
    /**
     * 
     */
    public String getDocXml(){
        return this.docXml;
    }

    /**
     * 
     */
    public void setDocXml(String docXml){
        this.docXml = docXml;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(EmrTemplate template) {
		this.template = template;
	}

	public EmrDocType getDocType() {
		return docType;
	}

	public void setDocType(EmrDocType docType) {
		this.docType = docType;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getFlagAuditFinish() {
		return flagAuditFinish;
	}

	public void setFlagAuditFinish(String flagAuditFinish) {
		this.flagAuditFinish = flagAuditFinish;
	}

	public String getAuditLevelSet() {
		return auditLevelSet;
	}

	public void setAuditLevelSet(String auditLevelSet) {
		this.auditLevelSet = auditLevelSet;
	}

	public String getPkEmpIntern() {
		return pkEmpIntern;
	}

	public void setPkEmpIntern(String pkEmpIntern) {
		this.pkEmpIntern = pkEmpIntern;
	}

	public Date getInternSignDate() {
		return internSignDate;
	}

	public void setInternSignDate(Date internSignDate) {
		this.internSignDate = internSignDate;
	}

	public String getPkEmpReferAct() {
		return pkEmpReferAct;
	}

	public void setPkEmpReferAct(String pkEmpReferAct) {
		this.pkEmpReferAct = pkEmpReferAct;
	}

	public Date getReferAuditDate() {
		return referAuditDate;
	}

	public void setReferAuditDate(Date referAuditDate) {
		this.referAuditDate = referAuditDate;
	}

	public String getReferEmpCode() {
		return referEmpCode;
	}

	public void setReferEmpCode(String referEmpCode) {
		this.referEmpCode = referEmpCode;
	}

	public EmrMedDoc getMedDoc() {
		return medDoc;
	}

	public void setMedDoc(EmrMedDoc medDoc) {
		this.medDoc = medDoc;
	}

	public String getConsultEmpCode() {
		return consultEmpCode;
	}

	public void setConsultEmpCode(String consultEmpCode) {
		this.consultEmpCode = consultEmpCode;
	}

	public String getDirectorEmpCode() {
		return directorEmpCode;
	}

	public void setDirectorEmpCode(String directorEmpCode) {
		this.directorEmpCode = directorEmpCode;
	}

	public String getReferEmpName() {
		return referEmpName;
	}

	public void setReferEmpName(String referEmpName) {
		this.referEmpName = referEmpName;
	}

	public String getConsultEmpName() {
		return consultEmpName;
	}

	public void setConsultEmpName(String consultEmpName) {
		this.consultEmpName = consultEmpName;
	}

	public String getDirectorEmpName() {
		return directorEmpName;
	}

	public void setDirectorEmpName(String directorEmpName) {
		this.directorEmpName = directorEmpName;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}    
	
}