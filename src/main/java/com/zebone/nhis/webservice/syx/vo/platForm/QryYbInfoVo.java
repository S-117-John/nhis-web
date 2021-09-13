package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;

public class QryYbInfoVo {
	private PvEncounter pv;
	
	private List<BlOpDt> blOpDts = new ArrayList<>();

	public PvEncounter getPv() {
		return pv;
	}

	public void setPv(PvEncounter pv) {
		this.pv = pv;
	}

	public List<BlOpDt> getBlOpDts() {
		return blOpDts;
	}

	public void setBlOpDts(List<BlOpDt> blOpDts) {
		this.blOpDts = blOpDts;
	}
	
	
	
}
