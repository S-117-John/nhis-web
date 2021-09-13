package com.zebone.nhis.nd.record.vo;

import com.zebone.nhis.common.module.nd.record.NdRecord;

public class NdRecordVo extends NdRecord {
	public Integer rowNum;

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	private String reviewNote;

	private String chkStatus;

	public String getReviewNote() {
		return reviewNote;
	}

	public void setReviewNote(String reviewNote) {
		this.reviewNote = reviewNote;
	}

	public String getChkStatus() {
		return chkStatus;
	}

	public void setChkStatus(String chkStatus) {
		this.chkStatus = chkStatus;
	}
}
