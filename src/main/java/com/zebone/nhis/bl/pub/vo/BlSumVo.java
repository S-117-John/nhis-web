package com.zebone.nhis.bl.pub.vo;

import java.util.List;

public class BlSumVo {
	
	private List<BlSumDepoVo> depos;
	
	private List<BlSumPayVo> payos;
	
	private BlSumRecords sumInfo;

	public BlSumRecords getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(BlSumRecords sumInfo) {
		this.sumInfo = sumInfo;
	}

	public List<BlSumDepoVo> getDepos() {
		return depos;
	}

	public void setDepos(List<BlSumDepoVo> depos) {
		this.depos = depos;
	}

	public List<BlSumPayVo> getPayos() {
		return payos;
	}

	public void setPayos(List<BlSumPayVo> payos) {
		this.payos = payos;
	}

	
}
