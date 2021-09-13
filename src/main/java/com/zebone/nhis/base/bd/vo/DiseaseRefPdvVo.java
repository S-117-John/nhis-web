package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.InsGzgyDiseaseOrd;
import com.zebone.nhis.common.module.scm.pub.BdPd;

public class DiseaseRefPdvVo {
	private String pkPd;
	private String pdCode;
	private String pdName;
	private List<InsGzgyDiseaseOrd> disOrdList;
	private String pkGzgydisease;
	private List<DisRefPdVo> disRefPdList;
	
	
	public List<DisRefPdVo> getDisRefPdList() {
		return disRefPdList;
	}
	public void setDisRefPdList(List<DisRefPdVo> disRefPdList) {
		this.disRefPdList = disRefPdList;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getPdCode() {
		return pdCode;
	}
	public void setPdCode(String pdCode) {
		this.pdCode = pdCode;
	}
	public String getPdName() {
		return pdName;
	}
	public void setPdName(String pdName) {
		this.pdName = pdName;
	}
	public List<InsGzgyDiseaseOrd> getDisOrdList() {
		return disOrdList;
	}
	public void setDisOrdList(List<InsGzgyDiseaseOrd> disOrdList) {
		this.disOrdList = disOrdList;
	}
	public String getPkGzgydisease() {
		return pkGzgydisease;
	}
	public void setPkGzgydisease(String pkGzgydisease) {
		this.pkGzgydisease = pkGzgydisease;
	}
}
