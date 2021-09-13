package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.ArrayList;
import java.util.List;

public class MiddleHisMz {
	private List<MiddleHisMzxm> middleHisMzxmlist = new ArrayList<MiddleHisMzxm>();
	private List<MiddleHisCfxmJksybz> jksybzlist = new ArrayList<MiddleHisCfxmJksybz>();
	private String datasourcename;
	public List<MiddleHisMzxm> getMiddleHisMzxmlist() {
		return middleHisMzxmlist;
	}
	public void setMiddleHisMzxmlist(List<MiddleHisMzxm> middleHisMzxmlist) {
		this.middleHisMzxmlist = middleHisMzxmlist;
	}
	public List<MiddleHisCfxmJksybz> getJksybzlist() {
		return jksybzlist;
	}
	public void setJksybzlist(List<MiddleHisCfxmJksybz> jksybzlist) {
		this.jksybzlist = jksybzlist;
	}
	public String getDatasourcename() {
		return datasourcename;
	}
	public void setDatasourcename(String datasourcename) {
		this.datasourcename = datasourcename;
	}
	
}
