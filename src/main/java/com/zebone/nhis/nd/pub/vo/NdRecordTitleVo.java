package com.zebone.nhis.nd.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordTitle;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
@Table(value="ND_RECORD_TITLE")
public class NdRecordTitleVo extends NdRecordTitle{

	   
	public String getPkRecordTitle() {
		return pkRecordTitle;
	}
	public void setPkRecordTitle(String pkRecordTitle) {
		this.pkRecordTitle = pkRecordTitle;
	}
	public String getPkRecord() {
		return pkRecord;
	}
	public void setPkRecord(String pkRecord) {
		this.pkRecord = pkRecord;
	}
	public String getRowNo() {
		return rowNo;
	}
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}
	public String getColNo() {
		return colNo;
	}
	public void setColNo(String colNo) {
		this.colNo = colNo;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getVal2() {
		return val2;
	}
	public void setVal2(String val2) {
		this.val2 = val2;
	}
		private String pkRecordTitle;
	    private String pkRecord;
	    private String rowNo;
	    private String colNo;
	    private String colName;
	    private String val;
	    private String val2;
}
