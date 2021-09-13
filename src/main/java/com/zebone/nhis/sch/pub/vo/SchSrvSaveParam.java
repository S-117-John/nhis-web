package com.zebone.nhis.sch.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.module.sch.pub.SchSrvOrd;

public class SchSrvSaveParam {
	
	private SchSrv schSrv;
	
	private List<SchSrvOrd> schSrvOrds;

	public SchSrv getSchSrv() {
		return schSrv;
	}

	public void setSchSrv(SchSrv schSrv) {
		this.schSrv = schSrv;
	}

	public List<SchSrvOrd> getSchSrvOrds() {
		return schSrvOrds;
	}

	public void setSchSrvOrds(List<SchSrvOrd> schSrvOrds) {
		this.schSrvOrds = schSrvOrds;
	}
	
	

}
