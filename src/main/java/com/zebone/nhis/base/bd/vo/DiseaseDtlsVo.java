package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.InsGzgyDisease;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;

public class DiseaseDtlsVo {
	
	//病种信息
	private InsGzgyDisease diseaseInfo;
	
	//关联药品信息
	private List<InsGzgyDiseaseOrd> diseaseOrdInfo = new ArrayList<InsGzgyDiseaseOrd>();
	
	//关联医保信息
	private List<InsGzgyHpDiv> diseaseHpInfo = new ArrayList<InsGzgyHpDiv>();

	public InsGzgyDisease getDiseaseInfo() {
		return diseaseInfo;
	}

	public void setDiseaseInfo(InsGzgyDisease diseaseInfo) {
		this.diseaseInfo = diseaseInfo;
	}

	public List<InsGzgyDiseaseOrd> getDiseaseOrdInfo() {
		return diseaseOrdInfo;
	}

	public void setDiseaseOrdInfo(List<InsGzgyDiseaseOrd> diseaseOrdInfo) {
		this.diseaseOrdInfo = diseaseOrdInfo;
	}

	public List<InsGzgyHpDiv> getDiseaseHpInfo() {
		return diseaseHpInfo;
	}

	public void setDiseaseHpInfo(List<InsGzgyHpDiv> diseaseHpInfo) {
		this.diseaseHpInfo = diseaseHpInfo;
	}
	
	
}
