package com.zebone.nhis.emr.rec.dict.vo;

import java.util.List;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;

public class EmrDoctorParam {

	private String pkWg;
	
	private List<EmrDoctor> emrDoctor;

	public String getPkWg() {
		return pkWg;
	}

	public void setPkWg(String pkWg) {
		this.pkWg = pkWg;
	}

	public List<EmrDoctor> getEmrDoctor() {
		return emrDoctor;
	}

	public void setEmrDoctor(List<EmrDoctor> emrDoctor) {
		this.emrDoctor = emrDoctor;
	}
	
	
}
