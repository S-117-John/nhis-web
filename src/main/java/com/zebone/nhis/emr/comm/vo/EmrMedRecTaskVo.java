package com.zebone.nhis.emr.comm.vo;

import java.util.Date;

import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;

/**
 *
 * @author 
 */
public class EmrMedRecTaskVo extends EmrMedRecTask{

    private String pkRec;
    private String taskId;
    private String docName;
    private Date recDate;
    private String creatorName;
    private String euDocStatus;
    private String taskTypeCode;
    private String taskTypeName;
    private String taskCode;
    private String taskName;
    private String taskTxt;
    
    private EmrPatList patList;

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEuDocStatus() {
		return euDocStatus;
	}

	public void setEuDocStatus(String euDocStatus) {
		this.euDocStatus = euDocStatus;
	}

	public String getTaskTypeCode() {
		return taskTypeCode;
	}

	public void setTaskTypeCode(String taskTypeCode) {
		this.taskTypeCode = taskTypeCode;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskTxt() {
		return taskTxt;
	}

	public void setTaskTxt(String taskTxt) {
		this.taskTxt = taskTxt;
	}

	public EmrPatList getPatList() {
		return patList;
	}

	public void setPatList(EmrPatList patList) {
		this.patList = patList;
	}

 
}