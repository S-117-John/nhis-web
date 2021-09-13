package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.ArrayList;
import java.util.List;

public class MiddleHis {
	private List<MiddleHisCfxm> middleHisCfxmlist = new ArrayList<MiddleHisCfxm>();
	private List<MiddleHisCfxmJksybz> jksybzlist = new ArrayList<MiddleHisCfxmJksybz>();
	private String datasourcename;
	public List<MiddleHisCfxm> getMiddleHisCfxmlist() {
		return middleHisCfxmlist;
	}
	public void setMiddleHisCfxmlist(List<MiddleHisCfxm> middleHisCfxmlist) {
		this.middleHisCfxmlist = middleHisCfxmlist;
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
