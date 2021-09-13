package com.zebone.nhis.emr.nis.vo;

import java.util.List;

import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;

public class NdRecordDtAndRowVo {
	
	
	private  NdRecordRow ndRecordRow;
	
	private String pkRecord;
	
	private List<Integer> colnoArray;
	
	private List<String> valArray;
	
	private List<String> colnameArray;



	public NdRecordRow getNdRecordRow() {
		return ndRecordRow;
	}

	public void setNdRecordRow(NdRecordRow ndRecordRow) {
		this.ndRecordRow = ndRecordRow;
	}

	public String getPkRecord() {
		return pkRecord;
	}

	public void setPkRecord(String pkRecord) {
		this.pkRecord = pkRecord;
	}

	public List<Integer> getColnoArray() {
		return colnoArray;
	}

	public void setColnoArray(List<Integer> colnoArray) {
		this.colnoArray = colnoArray;
	}

	public List<String> getValArray() {
		return valArray;
	}

	public void setValArray(List<String> valArray) {
		this.valArray = valArray;
	}

	public List<String> getColnameArray() {
		return colnameArray;
	}

	public void setColnameArray(List<String> colnameArray) {
		this.colnameArray = colnameArray;
	}
	
}
