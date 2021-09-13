package com.zebone.nhis.bl.pub.vo;

import java.util.List;

public class BlCcDetails {
	
	private List<BlCcDepoDetail> depo;
	
	private List<BlCcStDetail> depoSt;
	
	private List<BlCcStGetDetail> depoStGet;

	public List<BlCcDepoDetail> getDepo() {
		return depo;
	}

	public void setDepo(List<BlCcDepoDetail> depo) {
		this.depo = depo;
	}

	public List<BlCcStDetail> getDepoSt() {
		return depoSt;
	}

	public void setDepoSt(List<BlCcStDetail> depoSt) {
		this.depoSt = depoSt;
	}

	public List<BlCcStGetDetail> getDepoStGet() {
		return depoStGet;
	}

	public void setDepoStGet(List<BlCcStGetDetail> depoStGet) {
		this.depoStGet = depoStGet;
	}


}
