package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.List;

public class ZsBlCcDetails {
	
	private List<ZsBlCcDepoDetail> depo;
	
	private List<ZsBlCcStDetail> depoSt;
	
	private List<ZsBlCcStGetDetail> depoStGet;

	public List<ZsBlCcDepoDetail> getDepo() {
		return depo;
	}

	public void setDepo(List<ZsBlCcDepoDetail> depo) {
		this.depo = depo;
	}

	public List<ZsBlCcStDetail> getDepoSt() {
		return depoSt;
	}

	public void setDepoSt(List<ZsBlCcStDetail> depoSt) {
		this.depoSt = depoSt;
	}

	public List<ZsBlCcStGetDetail> getDepoStGet() {
		return depoStGet;
	}

	public void setDepoStGet(List<ZsBlCcStGetDetail> depoStGet) {
		this.depoStGet = depoStGet;
	}


}
