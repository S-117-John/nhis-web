package com.zebone.nhis.nd.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;

public class NdRecordRowVo extends NdRecordRow {
	public List<NdRecordDt> dtlist;
	private Integer rid;//记录id，排序用

	private String pkEmpQcLevel;//签署人审签级别
	
	private String pkEmpChkQcLevel;//复核人审签级别
	
	
	public String getPkEmpQcLevel() {
		return pkEmpQcLevel;
	}

	public void setPkEmpQcLevel(String pkEmpQcLevel) {
		this.pkEmpQcLevel = pkEmpQcLevel;
	}

	public String getPkEmpChkQcLevel() {
		return pkEmpChkQcLevel;
	}

	public void setPkEmpChkQcLevel(String pkEmpChkQcLevel) {
		this.pkEmpChkQcLevel = pkEmpChkQcLevel;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public List<NdRecordDt> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<NdRecordDt> dtlist) {
		this.dtlist = dtlist;
	}

}
