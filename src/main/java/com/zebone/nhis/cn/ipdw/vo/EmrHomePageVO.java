package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;

/**
 * 病案首页
 */
public class EmrHomePageVO extends EmrHomePage {
	//地区术语
	private List<Map<String, Object>> lstDcdts;
	//病情转归
	private String dtOutcomes;

	public List<Map<String, Object>> getLstDcdts() {
		return lstDcdts;
	}

	public void setLstDcdts(List<Map<String, Object>> lstDcdts) {
		this.lstDcdts = lstDcdts;
	}

	public String getDtOutcomes() {
		return dtOutcomes;
	}

	public void setDtOutcomes(String dtOutcomes) {
		this.dtOutcomes = dtOutcomes;
	}
}
