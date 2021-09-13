package com.zebone.nhis.sch.appt.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.sch.plan.SchSch;

public class OrderSuccessParam {

	private SchSch schSch;
	
	private ExAssistOcc exAssistOcc;

	public SchSch getSchSch() {
		return schSch;
	}

	public void setSchSch(SchSch schSch) {
		this.schSch = schSch;
	}

	public ExAssistOcc getExAssistOcc() {
		return exAssistOcc;
	}

	public void setExAssistOcc(ExAssistOcc exAssistOcc) {
		this.exAssistOcc = exAssistOcc;
	} 
	
	
}
