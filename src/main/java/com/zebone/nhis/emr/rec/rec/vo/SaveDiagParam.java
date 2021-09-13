package com.zebone.nhis.emr.rec.rec.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.emr.oth.PvDiag;


public class SaveDiagParam {
    private String pkPv;
    private List<PvDiag> pvDiagList = new ArrayList<PvDiag>();
    
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public List<PvDiag> getPvDiagList() {
		return pvDiagList;
	}
	public void setPvDiagList(List<PvDiag> pvDiagList) {
		this.pvDiagList = pvDiagList;
	}
	
    
}
