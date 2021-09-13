package com.zebone.nhis.pi.fix.vo;

import java.util.List;

import com.zebone.nhis.pi.pub.vo.PiMasterVo;

public class PagePiListVo {
	
	private List<PiMasterVo> patiList;
	
	private int totalCount;
	
	public List<PiMasterVo> getPatiList() {
		return patiList;
	}

	public void setPatiList(List<PiMasterVo> patiList) {
		this.patiList = patiList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
