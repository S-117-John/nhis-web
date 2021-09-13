package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.BdReport;
import com.zebone.nhis.common.module.base.bd.code.BdReportParam;

public class BdReportVo extends BdReport {
	public List<BdReportParam> params;

	public List<BdReportParam> getParams() {
		return params;
	}

	public void setParams(List<BdReportParam> params) {
		this.params = params;
	}

}
