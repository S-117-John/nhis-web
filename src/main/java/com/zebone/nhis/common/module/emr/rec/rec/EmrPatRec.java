package com.zebone.nhis.common.module.emr.rec.rec;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 患者病历记录
 * @author 
 */
public class EmrPatRec{
    /**
     * 
     */
    private String pkPatrec;
    /**
     * 
     */
    private String pkOrg;
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
     * 1、书写
2、完成
3、质控
4、提交
5、终末质控
6、终末提交
7、归档

     */
    private String euStatus;
    /**
     * lastPageNo:lastRowNo(上次打印内容最后位置的页码:上次打印内容最后位置的行号)
     */
    private String printRecCourse;
    /**
     * 2：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    private String printRecCoursePage;
    /**
     * 3：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    private String printRecCourseBak;
    /**
     * 
     */
    private Date printRecLastTime;
    /**
     * 
     */
    private String pkEmpFinish;
    /**
     * 
     */
    private Date finishDate;
    /**
     * 
     */
    private String pkEmpQc;
    /**
     * 
     */
    private Date qcDate;
    /**
     * 
     */
    private String pkEmpSubmit;
    /**
     * 
     */
    private Date submitDate;
    
    private String flagArchive;
    
    /**
     * 
     */
    private String pkEmpArchive;
    /**
     * 
     */
    private Date archiveDate;
    /**
     * A/B/C
     */
    private String euGrade;
    /**
     * 
     */
    private BigDecimal score;
    /**
     * 
     */
    private String euLinkQcGrade;
    /**
     * 
     */
    private BigDecimal linkQcScore;
    
    private String flagLinkQc;
    /**
     * 
     */
    private String pkEmpLinkQc;
    /**
     * 
     */
    private Date linkQcDate;
    /**
     * 
     */
    private String euEmpQcGrade;
    /**
     * 
     */
    private BigDecimal empQcScore;
    /**
     * 
     */
    private String pkEmpEmpQc;
    /**
     * 
     */
    private Date empQcDate;
    /**
     * 
     */
    private String euDeptQcGrade;
    /**
     * 
     */
    private BigDecimal deptQcScore;
    
    private String flagDeptQc;
    
    /**
     * 
     */
    private String pkEmpDeptQc;
    /**
     * 
     */
    private Date deptQcDate;
    /**
     * 
     */
    private String euFinalQcGrade;
    /**
     * 
     */
    private BigDecimal finalQcScore;
    
    private String flagFinalQc;
    
    /**
     * 
     */
    private String pkEmpFinalQc;
    /**
     * 
     */
    private Date finalQcDate;
    /**
     * 
     */
    private String diagAdmitTxt;
    /**
     * 
     */
    private String diagDisTxt;
    
    private String pkEmpIntern;
    
    
    /**
     * 
     */
    private String pkEmpRefer;
    /**
     * 
     */
    private String pkEmpConsult;
    /**
     * 
     */
    private String pkEmpDirector;
    
    private String pkEmpNsCharge;
    /**
     * 
     */
    private String pkEmpNsHead;
    /**
     * 
     */
    private String pkEmpQcDis;
    
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
    
    private String flagOpenEdit;
    
    private Date editBeginDate;
    
    private Date editEndDate;
    
    private String flagReceive;
    
    private Date receiveDate;
    
    private String pkEmpReceive;
    
    private String ReceiveName;
    
    private String flagCode;
    
    private String codeName;
    
    private String codeDate;
    
    private String pkEmpDeptQcSubmit;
    
    private Date deptQcSubmitDate;
    
    private String pkEmpFinalQcSubmit;
    
    private Date finalQcSubmitDate;
    
    private String flagEmrArchive;
    
    private String pkEmpEmrArchive;
    
    private Date emrArchiveDate;
    
    private String flagAmendDept;
    
    private String flagAmendFinal;
    
    private String flagSeal;//病历封存标志
    
    private String flagCopy;//病历复印标志
    
    private String flagTimeoutQc;//超时归档标志
    
    private String flagTimeoutCode;//超时归档编码
    
    
    
    public String getFlagSeal() {
		return flagSeal;
	}

	public void setFlagSeal(String flagSeal) {
		this.flagSeal = flagSeal;
	}

	public String getFlagCopy() {
		return flagCopy;
	}

	public void setFlagCopy(String flagCopy) {
		this.flagCopy = flagCopy;
	}

	public String getFlagTimeoutQc() {
		return flagTimeoutQc;
	}

	public void setFlagTimeoutQc(String flagTimeoutQc) {
		this.flagTimeoutQc = flagTimeoutQc;
	}

	public String getFlagTimeoutCode() {
		return flagTimeoutCode;
	}

	public void setFlagTimeoutCode(String flagTimeoutCode) {
		this.flagTimeoutCode = flagTimeoutCode;
	}

	public String getFlagOpenEdit() {
		return flagOpenEdit;
	}

	public void setFlagOpenEdit(String flagOpenEdit) {
		this.flagOpenEdit = flagOpenEdit;
	}

	public Date getEditBeginDate() {
		return editBeginDate;
	}

	public void setEditBeginDate(Date editBeginDate) {
		this.editBeginDate = editBeginDate;
	}

	public Date getEditEndDate() {
		return editEndDate;
	}

	public void setEditEndDate(Date editEndDate) {
		this.editEndDate = editEndDate;
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
     * 1、书写
2、完成
3、质控
4、提交
5、归档

     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 1、书写
2、完成
3、质控
4、提交
5、归档

     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * lastPageNo:lastRowNo(上次打印内容最后位置的页码:上次打印内容最后位置的行号)
     */
    public String getPrintRecCourse(){
        return this.printRecCourse;
    }

    /**
     * lastPageNo:lastRowNo(上次打印内容最后位置的页码:上次打印内容最后位置的行号)
     */
    public void setPrintRecCourse(String printRecCourse){
        this.printRecCourse = printRecCourse;
    }    
    /**
     * 2：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    public String getPrintRecCoursePage(){
        return this.printRecCoursePage;
    }

    /**
     * 2：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    public void setPrintRecCoursePage(String printRecCoursePage){
        this.printRecCoursePage = printRecCoursePage;
    }    
    /**
     * 3：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    public String getPrintRecCourseBak(){
        return this.printRecCourseBak;
    }

    /**
     * 3：满页打
lastPageNo:上一次打印时文档末页的页码
     */
    public void setPrintRecCourseBak(String printRecCourseBak){
        this.printRecCourseBak = printRecCourseBak;
    }    
    /**
     * 
     */
    public Date getPrintRecLastTime(){
        return this.printRecLastTime;
    }

    /**
     * 
     */
    public void setPrintRecLastTime(Date printRecLastTime){
        this.printRecLastTime = printRecLastTime;
    }    
    /**
     * 
     */
    public String getPkEmpFinish(){
        return this.pkEmpFinish;
    }

    /**
     * 
     */
    public void setPkEmpFinish(String pkEmpFinish){
        this.pkEmpFinish = pkEmpFinish;
    }    
    /**
     * 
     */
    public Date getFinishDate(){
        return this.finishDate;
    }

    /**
     * 
     */
    public void setFinishDate(Date finishDate){
        this.finishDate = finishDate;
    }    
    /**
     * 
     */
    public String getPkEmpQc(){
        return this.pkEmpQc;
    }

    /**
     * 
     */
    public void setPkEmpQc(String pkEmpQc){
        this.pkEmpQc = pkEmpQc;
    }    
    /**
     * 
     */
    public Date getQcDate(){
        return this.qcDate;
    }

    /**
     * 
     */
    public void setQcDate(Date qcDate){
        this.qcDate = qcDate;
    }    
    /**
     * 
     */
    public String getPkEmpSubmit(){
        return this.pkEmpSubmit;
    }

    /**
     * 
     */
    public void setPkEmpSubmit(String pkEmpSubmit){
        this.pkEmpSubmit = pkEmpSubmit;
    }    
    /**
     * 
     */
    public Date getSubmitDate(){
        return this.submitDate;
    }

    /**
     * 
     */
    public void setSubmitDate(Date submitDate){
        this.submitDate = submitDate;
    }    
    /**
     * 
     */
    public String getPkEmpArchive(){
        return this.pkEmpArchive;
    }

    /**
     * 
     */
    public void setPkEmpArchive(String pkEmpArchive){
        this.pkEmpArchive = pkEmpArchive;
    }    
    /**
     * 
     */
    public Date getArchiveDate(){
        return this.archiveDate;
    }

    /**
     * 
     */
    public void setArchiveDate(Date archiveDate){
        this.archiveDate = archiveDate;
    }    
    /**
     * A/B/C
     */
    public String getEuGrade(){
        return this.euGrade;
    }

    /**
     * A/B/C
     */
    public void setEuGrade(String euGrade){
        this.euGrade = euGrade;
    }    
    /**
     * 
     */
    public BigDecimal getScore(){
        return this.score;
    }

    /**
     * 
     */
    public void setScore(BigDecimal score){
        this.score = score;
    }    
    /**
     * 
     */
    public String getEuLinkQcGrade(){
        return this.euLinkQcGrade;
    }

    /**
     * 
     */
    public void setEuLinkQcGrade(String euLinkQcGrade){
        this.euLinkQcGrade = euLinkQcGrade;
    }    
    /**
     * 
     */
    public BigDecimal getLinkQcScore(){
        return this.linkQcScore;
    }

    /**
     * 
     */
    public void setLinkQcScore(BigDecimal linkQcScore){
        this.linkQcScore = linkQcScore;
    }    
    /**
     * 
     */
    public String getPkEmpLinkQc(){
        return this.pkEmpLinkQc;
    }

    /**
     * 
     */
    public void setPkEmpLinkQc(String pkEmpLinkQc){
        this.pkEmpLinkQc = pkEmpLinkQc;
    }    
    /**
     * 
     */
    public Date getLinkQcDate(){
        return this.linkQcDate;
    }

    /**
     * 
     */
    public void setLinkQcDate(Date linkQcDate){
        this.linkQcDate = linkQcDate;
    }    
    /**
     * 
     */
    public String getEuEmpQcGrade(){
        return this.euEmpQcGrade;
    }

    /**
     * 
     */
    public void setEuEmpQcGrade(String euEmpQcGrade){
        this.euEmpQcGrade = euEmpQcGrade;
    }    
    /**
     * 
     */
    public BigDecimal getEmpQcScore(){
        return this.empQcScore;
    }

    /**
     * 
     */
    public void setEmpQcScore(BigDecimal empQcScore){
        this.empQcScore = empQcScore;
    }    
    /**
     * 
     */
    public String getPkEmpEmpQc(){
        return this.pkEmpEmpQc;
    }

    /**
     * 
     */
    public void setPkEmpEmpQc(String pkEmpEmpQc){
        this.pkEmpEmpQc = pkEmpEmpQc;
    }    
    /**
     * 
     */
    public Date getEmpQcDate(){
        return this.empQcDate;
    }

    /**
     * 
     */
    public void setEmpQcDate(Date empQcDate){
        this.empQcDate = empQcDate;
    }    
    /**
     * 
     */
    public String getEuDeptQcGrade(){
        return this.euDeptQcGrade;
    }

    /**
     * 
     */
    public void setEuDeptQcGrade(String euDeptQcGrade){
        this.euDeptQcGrade = euDeptQcGrade;
    }    
    /**
     * 
     */
    public BigDecimal getDeptQcScore(){
        return this.deptQcScore;
    }

    /**
     * 
     */
    public void setDeptQcScore(BigDecimal deptQcScore){
        this.deptQcScore = deptQcScore;
    }    
    /**
     * 
     */
    public String getPkEmpDeptQc(){
        return this.pkEmpDeptQc;
    }

    /**
     * 
     */
    public void setPkEmpDeptQc(String pkEmpDeptQc){
        this.pkEmpDeptQc = pkEmpDeptQc;
    }    
    /**
     * 
     */
    public Date getDeptQcDate(){
        return this.deptQcDate;
    }

    /**
     * 
     */
    public void setDeptQcDate(Date deptQcDate){
        this.deptQcDate = deptQcDate;
    }    
    /**
     * 
     */
    public String getEuFinalQcGrade(){
        return this.euFinalQcGrade;
    }

    /**
     * 
     */
    public void setEuFinalQcGrade(String euFinalQcGrade){
        this.euFinalQcGrade = euFinalQcGrade;
    }    
    /**
     * 
     */
    public BigDecimal getFinalQcScore(){
        return this.finalQcScore;
    }

    /**
     * 
     */
    public void setFinalQcScore(BigDecimal finalQcScore){
        this.finalQcScore = finalQcScore;
    }    
    /**
     * 
     */
    public String getPkEmpFinalQc(){
        return this.pkEmpFinalQc;
    }

    /**
     * 
     */
    public void setPkEmpFinalQc(String pkEmpFinalQc){
        this.pkEmpFinalQc = pkEmpFinalQc;
    }    
    /**
     * 
     */
    public Date getFinalQcDate(){
        return this.finalQcDate;
    }

    /**
     * 
     */
    public void setFinalQcDate(Date finalQcDate){
        this.finalQcDate = finalQcDate;
    }    
    /**
     * 
     */
    public String getDiagAdmitTxt(){
        return this.diagAdmitTxt;
    }

    /**
     * 
     */
    public void setDiagAdmitTxt(String diagAdmitTxt){
        this.diagAdmitTxt = diagAdmitTxt;
    }    
    /**
     * 
     */
    public String getDiagDisTxt(){
        return this.diagDisTxt;
    }

    /**
     * 
     */
    public void setDiagDisTxt(String diagDisTxt){
        this.diagDisTxt = diagDisTxt;
    }    


    public String getPkEmpRefer() {
		return pkEmpRefer;
	}

	public void setPkEmpRefer(String pkEmpRefer) {
		this.pkEmpRefer = pkEmpRefer;
	}

	public String getPkEmpConsult() {
		return pkEmpConsult;
	}

	public void setPkEmpConsult(String pkEmpConsult) {
		this.pkEmpConsult = pkEmpConsult;
	}

	public String getPkEmpDirector() {
		return pkEmpDirector;
	}

	public void setPkEmpDirector(String pkEmpDirector) {
		this.pkEmpDirector = pkEmpDirector;
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

	public String getPkEmpNsCharge() {
		return pkEmpNsCharge;
	}

	public void setPkEmpNsCharge(String pkEmpNsCharge) {
		this.pkEmpNsCharge = pkEmpNsCharge;
	}

	public String getPkEmpNsHead() {
		return pkEmpNsHead;
	}

	public void setPkEmpNsHead(String pkEmpNsHead) {
		this.pkEmpNsHead = pkEmpNsHead;
	}

	public String getPkEmpQcDis() {
		return pkEmpQcDis;
	}

	public void setPkEmpQcDis(String pkEmpQcDis) {
		this.pkEmpQcDis = pkEmpQcDis;
	}

	public String getPkEmpIntern() {
		return pkEmpIntern;
	}

	public void setPkEmpIntern(String pkEmpIntern) {
		this.pkEmpIntern = pkEmpIntern;
	}

	public String getFlagReceive() {
		return flagReceive;
	}

	public void setFlagReceive(String flagReceive) {
		this.flagReceive = flagReceive;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getPkEmpReceive() {
		return pkEmpReceive;
	}

	public void setPkEmpReceive(String pkEmpReceive) {
		this.pkEmpReceive = pkEmpReceive;
	}

	public String getReceiveName() {
		return ReceiveName;
	}

	public void setReceiveName(String receiveName) {
		ReceiveName = receiveName;
	}

	public String getFlagCode() {
		return flagCode;
	}

	public void setFlagCode(String flagCode) {
		this.flagCode = flagCode;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeDate() {
		return codeDate;
	}

	public void setCodeDate(String codeDate) {
		this.codeDate = codeDate;
	}

	public String getPkEmpDeptQcSubmit() {
		return pkEmpDeptQcSubmit;
	}

	public void setPkEmpDeptQcSubmit(String pkEmpDeptQcSubmit) {
		this.pkEmpDeptQcSubmit = pkEmpDeptQcSubmit;
	}

	public Date getDeptQcSubmitDate() {
		return deptQcSubmitDate;
	}

	public void setDeptQcSubmitDate(Date deptQcSubmitDate) {
		this.deptQcSubmitDate = deptQcSubmitDate;
	}

	public String getPkEmpFinalQcSubmit() {
		return pkEmpFinalQcSubmit;
	}

	public void setPkEmpFinalQcSubmit(String pkEmpFinalQcSubmit) {
		this.pkEmpFinalQcSubmit = pkEmpFinalQcSubmit;
	}

	public Date getFinalQcSubmitDate() {
		return finalQcSubmitDate;
	}

	public void setFinalQcSubmitDate(Date finalQcSubmitDate) {
		this.finalQcSubmitDate = finalQcSubmitDate;
	}

	public String getPkEmpEmrArchive() {
		return pkEmpEmrArchive;
	}

	public void setPkEmpEmrArchive(String pkEmpEmrArchive) {
		this.pkEmpEmrArchive = pkEmpEmrArchive;
	}

	public Date getEmrArchiveDate() {
		return emrArchiveDate;
	}

	public void setEmrArchiveDate(Date emrArchiveDate) {
		this.emrArchiveDate = emrArchiveDate;
	}

	public String getFlagAmendDept() {
		return flagAmendDept;
	}

	public void setFlagAmendDept(String flagAmendDept) {
		this.flagAmendDept = flagAmendDept;
	}

	public String getFlagAmendFinal() {
		return flagAmendFinal;
	}

	public void setFlagAmendFinal(String flagAmendFinal) {
		this.flagAmendFinal = flagAmendFinal;
	}

	public String getFlagArchive() {
		return flagArchive;
	}

	public void setFlagArchive(String flagArchive) {
		this.flagArchive = flagArchive;
	}

	public String getFlagEmrArchive() {
		return flagEmrArchive;
	}

	public void setFlagEmrArchive(String flagEmrArchive) {
		this.flagEmrArchive = flagEmrArchive;
	}

	public String getFlagLinkQc() {
		return flagLinkQc;
	}

	public void setFlagLinkQc(String flagLinkQc) {
		this.flagLinkQc = flagLinkQc;
	}

	public String getFlagDeptQc() {
		return flagDeptQc;
	}

	public void setFlagDeptQc(String flagDeptQc) {
		this.flagDeptQc = flagDeptQc;
	}

	public String getFlagFinalQc() {
		return flagFinalQc;
	}

	public void setFlagFinalQc(String flagFinalQc) {
		this.flagFinalQc = flagFinalQc;
	}

}