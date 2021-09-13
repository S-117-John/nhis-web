package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("coveredPartyOf")
public class CoveredPartyOf {
	@XStreamAsAttribute
	private String typeCode;
	private CoverageRecord coverageRecord;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public CoverageRecord getCoverageRecord() {
		if(coverageRecord==null)coverageRecord=new CoverageRecord();
		return coverageRecord;
	}

	public void setCoverageRecord(CoverageRecord coverageRecord) {
		this.coverageRecord = coverageRecord;
	}

}
