package com.zebone.nhis.sch.plan.vo;

import java.util.List;

import com.zebone.nhis.common.module.sch.plan.SchSch;

public class SchAdjustParam {
	
	private String adjustType;
	
	private String euSchclass;
	
	private List<SchExclude> schExcludes;
	
	private List<SchAdjustDate> schAdjustDates;

	public String getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	public List<SchExclude> getSchExcludes() {
		return schExcludes;
	}

	public void setSchExcludes(List<SchExclude> schExcludes) {
		this.schExcludes = schExcludes;
	}

	public List<SchAdjustDate> getSchAdjustDates() {
		return schAdjustDates;
	}

	public void setSchAdjustDates(List<SchAdjustDate> schAdjustDates) {
		this.schAdjustDates = schAdjustDates;
	}

	public String getEuSchclass() {
		return euSchclass;
	}

	public void setEuSchclass(String euSchclass) {
		this.euSchclass = euSchclass;
	}
	
	


}
