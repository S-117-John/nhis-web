package com.zebone.nhis.labor.nis.vo;

import java.util.List;

import com.zebone.nhis.common.module.labor.nis.PvLaborRec;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.ex.pub.vo.PvInfantVo;

public class PvLaborRecVo extends PvLaborRec {
	private List<PvLaborRecDt> dtlist;
	private List<PvInfantVo> inflist;
	
	public List<PvLaborRecDt> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<PvLaborRecDt> dtlist) {
		this.dtlist = dtlist;
	}

	public List<PvInfantVo> getInflist() {
		return inflist;
	}

	public void setInflist(List<PvInfantVo> inflist) {
		this.inflist = inflist;
	}
	

}
