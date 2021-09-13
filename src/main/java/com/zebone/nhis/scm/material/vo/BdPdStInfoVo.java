package com.zebone.nhis.scm.material.vo;

import java.util.ArrayList;
import java.util.List;

public class BdPdStInfoVo {
	
	private BdPdStVo bdPdSt;
	
	private List<BdPdStDetailsVo> detailsList = new ArrayList<>();

	public BdPdStVo getBdPdSt() {
		return bdPdSt;
	}

	public void setBdPdSt(BdPdStVo bdPdSt) {
		this.bdPdSt = bdPdSt;
	}

	public List<BdPdStDetailsVo> getDetailsList() {
		return detailsList;
	}

	public void setDetailsList(List<BdPdStDetailsVo> detailsList) {
		this.detailsList = detailsList;
	}
	
}
