package com.zebone.nhis.emr.nis.vo;

import java.util.List;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;

public class NdRecordRowsVo extends NdRecordRow {

	public List<NdRecordDt> dtlist;
	private Integer rid;//记录id，排序用

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
