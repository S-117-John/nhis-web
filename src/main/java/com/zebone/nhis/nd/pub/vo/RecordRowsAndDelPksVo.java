package com.zebone.nhis.nd.pub.vo;

import java.util.List;

public class RecordRowsAndDelPksVo {
	
	private List<NdRecordRowVo> ndRows;
	private List<String> pkRecordrows;
	public List<NdRecordRowVo> getNdRows() {
		return ndRows;
	}
	public void setNdRows(List<NdRecordRowVo> ndRows) {
		this.ndRows = ndRows;
	}
	public List<String> getPkRecordrows() {
		return pkRecordrows;
	}
	public void setPkRecordrows(List<String> pkRecordrows) {
		this.pkRecordrows = pkRecordrows;
	}
	
	
}
