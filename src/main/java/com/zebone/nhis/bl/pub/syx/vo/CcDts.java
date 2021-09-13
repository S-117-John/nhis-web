package com.zebone.nhis.bl.pub.syx.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlCc;

public class CcDts {
	
	private BlCc blCc;
	
	private List<Map<String,Object>> payInfoList;
	
	private List<Map<String,Object>> canlInvInfoList;
	
	private List<Map<String,Object>> cgInfoList;
	
	private List<Map<String,Object>> hpInfoList;
	
	
	public List<Map<String, Object>> getCgInfoList() {
		return cgInfoList;
	}

	public void setCgInfoList(List<Map<String, Object>> cgInfoList) {
		this.cgInfoList = cgInfoList;
	}

	public List<Map<String, Object>> getHpInfoList() {
		return hpInfoList;
	}

	public void setHpInfoList(List<Map<String, Object>> hpInfoList) {
		this.hpInfoList = hpInfoList;
	}

	public List<Map<String, Object>> getCanlInvInfoList() {
		return canlInvInfoList;
	}

	public void setCanlInvInfoList(List<Map<String, Object>> canlInvInfoList) {
		this.canlInvInfoList = canlInvInfoList;
	}

	public BlCc getBlCc() {
		return blCc;
	}

	public void setBlCc(BlCc blCc) {
		this.blCc = blCc;
	}

	public List<Map<String, Object>> getPayInfoList() {
		return payInfoList;
	}

	public void setPayInfoList(List<Map<String, Object>> payInfoList) {
		this.payInfoList = payInfoList;
	}

	
	
	
	
}
