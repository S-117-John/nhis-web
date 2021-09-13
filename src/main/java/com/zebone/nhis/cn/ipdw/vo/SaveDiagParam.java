package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.pi.PatInfo;
import com.zebone.nhis.common.module.pv.PvDiag;

public class SaveDiagParam {
	private String flagUpPvmode;
	private String pkDiag;
	private String pkHp;
	private String flagQryHp; 
    private String pk_pv;
    private PatInfo patInfo;
    private List<PvDiag> pvDiagList = new ArrayList<PvDiag>();
    
    
    
	public PatInfo getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(PatInfo patInfo) {
		this.patInfo = patInfo;
	}
	public String getPk_pv() {
		return pk_pv;
	}
	public void setPk_pv(String pk_pv) {
		this.pk_pv = pk_pv;
	}
	public List<PvDiag> getPvDiagList() {
		return pvDiagList;
	}
	public void setPvDiagList(List<PvDiag> pvDiagList) {
		this.pvDiagList = pvDiagList;
	}
	public String getFlagQryHp() {
		return flagQryHp;
	}
	public void setFlagQryHp(String flagQryHp) {
		this.flagQryHp = flagQryHp;
	}
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getFlagUpPvmode() {
		return flagUpPvmode;
	}
	public void setFlagUpPvmode(String flagUpPvmode) {
		this.flagUpPvmode = flagUpPvmode;
	}
	
}
