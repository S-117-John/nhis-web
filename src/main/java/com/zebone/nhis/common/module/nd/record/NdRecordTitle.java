package com.zebone.nhis.common.module.nd.record;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD_TITLE 
 *
 * @since 2017-06-08 11:29:37
 */
@Table(value="ND_RECORD_TITLE")
public class NdRecordTitle extends BaseModule  {
	@PK
	@Field(value="PK_RECORDTITLE",id=KeyId.UUID)
    private String pkRecordTitle;

	@Field(value="PK_RECORD")
    private String pkRecord;

	@Field(value="ROWNO")
    private String rowNo;

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

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	@Field(value="COLNO")
    private String colNo;

	@Field(value="COLNAME")
    private String colName;

	@Field(value="VAL")
    private String val;

	@Field(value="VAL2")
    private String val2;

	@Field(value="MODITY_TIME")
    private Date modityTime;

}