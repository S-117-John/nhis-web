package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_PAT_REC 
 *
 * @since 2016-10-20 05:20:48
 */
@Table(value="EMR_PAT_REC")
public class EmrPatRec   {

	@PK
	@Field(value="PK_PATREC",id=KeyId.UUID)
    private String pkPatrec;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="TIMES")
    private Integer times;

	@Field(value="PK_PV")
    private String pkPv;

    /** EU_STATUS - 1、书写
2、完成
3、质控
4、提交
5、归档 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** PRINT_REC_COURSE - lastPageNo:lastRowNo(上次打印内容最后位置的页码:上次打印内容最后位置的行号) */
	@Field(value="PRINT_REC_COURSE")
    private String printRecCourse;

    /** PRINT_REC_COURSE_PAGE - 2：满页打
lastPageNo:上一次打印时文档末页的页码 */
	@Field(value="PRINT_REC_COURSE_PAGE")
    private String printRecCoursePage;

    /** PRINT_REC_COURSE_BAK - 3：满页打
lastPageNo:上一次打印时文档末页的页码 */
	@Field(value="PRINT_REC_COURSE_BAK")
    private String printRecCourseBak;

	@Field(value="PRINT_REC_LAST_TIME")
    private Date printRecLastTime;

	@Field(value="PK_EMP_FINISH")
    private String pkEmpFinish;

	@Field(value="FINISH_DATE")
    private Date finishDate;

	@Field(value="PK_EMP_QC")
    private String pkEmpQc;

	@Field(value="QC_DATE")
    private Date qcDate;

	@Field(value="PK_EMP_SUBMIT")
    private String pkEmpSubmit;

	@Field(value="SUBMIT_DATE")
    private Date submitDate;

	@Field(value="FLAG_ARCHIVE")
    private String flagArchive;
	
	@Field(value="PK_EMP_ARCHIVE")
    private String pkEmpArchive;

	@Field(value="ARCHIVE_DATE")
    private Date archiveDate;

    /** EU_GRADE - A/B/C */
	@Field(value="EU_GRADE")
    private String euGrade;

	@Field(value="SCORE")
    private Double score;

	@Field(value="FLAG_LINK_QC")
    private String flagLinkQc;
	
	@Field(value="EU_LINK_QC_GRADE")
    private String euLinkQcGrade;

	@Field(value="LINK_QC_SCORE")
    private Double linkQcScore;

	@Field(value="PK_EMP_LINK_QC")
    private String pkEmpLinkQc;

	@Field(value="LINK_QC_DATE")
    private Date linkQcDate;

	@Field(value="EU_EMP_QC_GRADE")
    private String euEmpQcGrade;

	@Field(value="EMP_QC_SCORE")
    private Double empQcScore;

	@Field(value="PK_EMP_EMP_QC")
    private String pkEmpEmpQc;

	@Field(value="EMP_QC_DATE")
    private Date empQcDate;

	@Field(value="FLAG_DEPT_QC")
    private String flagDeptQc;
	
	@Field(value="EU_DEPT_QC_GRADE")
    private String euDeptQcGrade;

	@Field(value="DEPT_QC_SCORE")
    private Double deptQcScore;

	@Field(value="PK_EMP_DEPT_QC")
    private String pkEmpDeptQc;

	@Field(value="DEPT_QC_DATE")
    private Date deptQcDate;

	@Field(value="FLAG_FINAL_QC")
    private String flag_final_qc;
	
	@Field(value="EU_FINAL_QC_GRADE")
    private String euFinalQcGrade;

	@Field(value="FINAL_QC_SCORE")
    private Double finalQcScore;

	@Field(value="PK_EMP_FINAL_QC")
    private String pkEmpFinalQc;

	@Field(value="FINAL_QC_DATE")
    private Date finalQcDate;

	@Field(value="PK_EMP_REFER")
    private String pkEmpRefer;

	@Field(value="PK_EMP_CONSULT")
    private String pkEmpConsult;

	@Field(value="PK_EMP_DIRECTOR")
    private String pkEmpDirector;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="DIAG_ADMIT_TXT")
    private String diagAdmitTxt;

	@Field(value="DIAG_DIS_TXT")
    private String diagDisTxt;
	
	@Field(value="FLAG_RECEIVE")
	private String flagReceive;
	
	@Field(value="PK_EMP_RECEIVE")
	private String pkEmpReceive;
	
	@Field(value="RECEIVE_DATE")
	private Date receiveDate;
	
	@Field(value="FLAG_EMR_ARCHIVE")
    private String flagEmrArchvie;
	
    public String getPkPatrec(){
        return this.pkPatrec;
    }
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public Integer getTimes(){
        return this.times;
    }
    public void setTimes(Integer times){
        this.times = times;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPrintRecCourse(){
        return this.printRecCourse;
    }
    public void setPrintRecCourse(String printRecCourse){
        this.printRecCourse = printRecCourse;
    }

    public String getPrintRecCoursePage(){
        return this.printRecCoursePage;
    }
    public void setPrintRecCoursePage(String printRecCoursePage){
        this.printRecCoursePage = printRecCoursePage;
    }

    public String getPrintRecCourseBak(){
        return this.printRecCourseBak;
    }
    public void setPrintRecCourseBak(String printRecCourseBak){
        this.printRecCourseBak = printRecCourseBak;
    }

    public Date getPrintRecLastTime(){
        return this.printRecLastTime;
    }
    public void setPrintRecLastTime(Date printRecLastTime){
        this.printRecLastTime = printRecLastTime;
    }

    public String getPkEmpFinish(){
        return this.pkEmpFinish;
    }
    public void setPkEmpFinish(String pkEmpFinish){
        this.pkEmpFinish = pkEmpFinish;
    }

    public Date getFinishDate(){
        return this.finishDate;
    }
    public void setFinishDate(Date finishDate){
        this.finishDate = finishDate;
    }

    public String getPkEmpQc(){
        return this.pkEmpQc;
    }
    public void setPkEmpQc(String pkEmpQc){
        this.pkEmpQc = pkEmpQc;
    }

    public Date getQcDate(){
        return this.qcDate;
    }
    public void setQcDate(Date qcDate){
        this.qcDate = qcDate;
    }

    public String getPkEmpSubmit(){
        return this.pkEmpSubmit;
    }
    public void setPkEmpSubmit(String pkEmpSubmit){
        this.pkEmpSubmit = pkEmpSubmit;
    }

    public Date getSubmitDate(){
        return this.submitDate;
    }
    public void setSubmitDate(Date submitDate){
        this.submitDate = submitDate;
    }

    public String getPkEmpArchive(){
        return this.pkEmpArchive;
    }
    public void setPkEmpArchive(String pkEmpArchive){
        this.pkEmpArchive = pkEmpArchive;
    }

    public Date getArchiveDate(){
        return this.archiveDate;
    }
    public void setArchiveDate(Date archiveDate){
        this.archiveDate = archiveDate;
    }

    public String getEuGrade(){
        return this.euGrade;
    }
    public void setEuGrade(String euGrade){
        this.euGrade = euGrade;
    }

    public Double getScore(){
        return this.score;
    }
    public void setScore(Double score){
        this.score = score;
    }

    public String getEuLinkQcGrade(){
        return this.euLinkQcGrade;
    }
    public void setEuLinkQcGrade(String euLinkQcGrade){
        this.euLinkQcGrade = euLinkQcGrade;
    }

    public Double getLinkQcScore(){
        return this.linkQcScore;
    }
    public void setLinkQcScore(Double linkQcScore){
        this.linkQcScore = linkQcScore;
    }

    public String getPkEmpLinkQc(){
        return this.pkEmpLinkQc;
    }
    public void setPkEmpLinkQc(String pkEmpLinkQc){
        this.pkEmpLinkQc = pkEmpLinkQc;
    }

    public Date getLinkQcDate(){
        return this.linkQcDate;
    }
    public void setLinkQcDate(Date linkQcDate){
        this.linkQcDate = linkQcDate;
    }

    public String getEuEmpQcGrade(){
        return this.euEmpQcGrade;
    }
    public void setEuEmpQcGrade(String euEmpQcGrade){
        this.euEmpQcGrade = euEmpQcGrade;
    }

    public Double getEmpQcScore(){
        return this.empQcScore;
    }
    public void setEmpQcScore(Double empQcScore){
        this.empQcScore = empQcScore;
    }

    public String getPkEmpEmpQc(){
        return this.pkEmpEmpQc;
    }
    public void setPkEmpEmpQc(String pkEmpEmpQc){
        this.pkEmpEmpQc = pkEmpEmpQc;
    }

    public Date getEmpQcDate(){
        return this.empQcDate;
    }
    public void setEmpQcDate(Date empQcDate){
        this.empQcDate = empQcDate;
    }

    public String getEuDeptQcGrade(){
        return this.euDeptQcGrade;
    }
    public void setEuDeptQcGrade(String euDeptQcGrade){
        this.euDeptQcGrade = euDeptQcGrade;
    }

    public Double getDeptQcScore(){
        return this.deptQcScore;
    }
    public void setDeptQcScore(Double deptQcScore){
        this.deptQcScore = deptQcScore;
    }

    public String getPkEmpDeptQc(){
        return this.pkEmpDeptQc;
    }
    public void setPkEmpDeptQc(String pkEmpDeptQc){
        this.pkEmpDeptQc = pkEmpDeptQc;
    }

    public Date getDeptQcDate(){
        return this.deptQcDate;
    }
    public void setDeptQcDate(Date deptQcDate){
        this.deptQcDate = deptQcDate;
    }

    public String getEuFinalQcGrade(){
        return this.euFinalQcGrade;
    }
    public void setEuFinalQcGrade(String euFinalQcGrade){
        this.euFinalQcGrade = euFinalQcGrade;
    }

    public Double getFinalQcScore(){
        return this.finalQcScore;
    }
    public void setFinalQcScore(Double finalQcScore){
        this.finalQcScore = finalQcScore;
    }

    public String getPkEmpFinalQc(){
        return this.pkEmpFinalQc;
    }
    public void setPkEmpFinalQc(String pkEmpFinalQc){
        this.pkEmpFinalQc = pkEmpFinalQc;
    }

    public Date getFinalQcDate(){
        return this.finalQcDate;
    }
    public void setFinalQcDate(Date finalQcDate){
        this.finalQcDate = finalQcDate;
    }

    public String getPkEmpRefer(){
        return this.pkEmpRefer;
    }
    public void setPkEmpRefer(String pkEmpRefer){
        this.pkEmpRefer = pkEmpRefer;
    }

    public String getPkEmpConsult(){
        return this.pkEmpConsult;
    }
    public void setPkEmpConsult(String pkEmpConsult){
        this.pkEmpConsult = pkEmpConsult;
    }

    public String getPkEmpDirector(){
        return this.pkEmpDirector;
    }
    public void setPkEmpDirector(String pkEmpDirector){
        this.pkEmpDirector = pkEmpDirector;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getDiagAdmitTxt(){
        return this.diagAdmitTxt;
    }
    public void setDiagAdmitTxt(String diagAdmitTxt){
        this.diagAdmitTxt = diagAdmitTxt;
    }

    public String getDiagDisTxt(){
        return this.diagDisTxt;
    }
    public void setDiagDisTxt(String diagDisTxt){
        this.diagDisTxt = diagDisTxt;
    }
    public String getFlagReceive() {
		return flagReceive;
	}
	public void setFlagReceive(String flagReceive) {
		this.flagReceive = flagReceive;
	}
	public String getPkEmpReceive() {
		return pkEmpReceive;
	}
	public void setPkEmpReceive(String pkEmpReceive) {
		this.pkEmpReceive = pkEmpReceive;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getFlagArchive() {
		return flagArchive;
	}
	public void setFlagArchive(String flagArchive) {
		this.flagArchive = flagArchive;
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
	public String getFlag_final_qc() {
		return flag_final_qc;
	}
	public void setFlag_final_qc(String flag_final_qc) {
		this.flag_final_qc = flag_final_qc;
	}
	public String getFlagEmrArchvie() {
		return flagEmrArchvie;
	}
	public void setFlagEmrArchvie(String flagEmrArchvie) {
		this.flagEmrArchvie = flagEmrArchvie;
	}
	
}