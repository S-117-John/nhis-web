package com.zebone.nhis.pi.hd.vo;

import java.util.List;

import com.zebone.nhis.common.module.pi.PiHd;
import com.zebone.nhis.common.module.pi.PiMaster;

public class PiMasterHdVo {
	
	/** 患者信息 **/
	private List<PiMaster> patiList;
	
	/** 患者透析 **/
	private List<PiHd> piHdList;

	public List<PiMaster> getPatiList() {
		return patiList;
	}

	public void setPatiList(List<PiMaster> patiList) {
		this.patiList = patiList;
	}

	public List<PiHd> getPiHdList() {
		return piHdList;
	}

	public void setPiHdList(List<PiHd> piHdList) {
		this.piHdList = piHdList;
	}
	
	
	
}
