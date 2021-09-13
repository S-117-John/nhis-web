package com.zebone.nhis.pv.hd.vo;

import com.zebone.nhis.common.module.cn.ipdw.PvDiag;
import com.zebone.nhis.common.module.pi.PiHd;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvHd;
import com.zebone.nhis.common.module.pv.PvOp;

public class SavePiVo {
	//患者就诊-就诊记录
	public PvEncounter pve;
	
	//患者就诊-门诊属性
	public PvOp pvo;
		
	//患者就诊-透析记录
	public PvHd pvd;
	
	//透析档案
	public PiHd pid;
	
	//患者信息
	public PiMaster pi;
	
	//诊断信息
	public PvDiag diagVo;
	
	public PiMaster getPi() {
		return pi;
	}
	public void setPi(PiMaster pi) {
		this.pi = pi;
	}
	public PvEncounter getPve() {
		return pve;
	}
	public void setPve(PvEncounter pve) {
		this.pve = pve;
	}

	public PvHd getPvd() {
		return pvd;
	}

	public void setPvd(PvHd pvd) {
		this.pvd = pvd;
	}

	public PiHd getPid() {
		return pid;
	}

	public void setPid(PiHd pid) {
		this.pid = pid;
	}
	public PvOp getPvo() {
		return pvo;
	}
	public void setPvo(PvOp pvo) {
		this.pvo = pvo;
	}
	public PvDiag getDiagVo() {
		return diagVo;
	}
	public void setDiagVo(PvDiag diagVo) {
		this.diagVo = diagVo;
	}
	
}
