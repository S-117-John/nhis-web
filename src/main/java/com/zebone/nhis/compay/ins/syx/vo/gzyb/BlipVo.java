package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.List;

public class BlipVo {
	private String pkpv;
    // 中途结算选择的待结项目主键
    private List<String> pkCgips;
	
    private String euhpdicttype;
    
    private String dateEnd;
    
    private String dateBegin;
	public String getPkpv() {
		return pkpv;
	}
	public void setPkpv(String pkpv) {
		this.pkpv = pkpv;
	}
	public List<String> getPkCgips() {
		return pkCgips;
	}
	public void setPkCgips(List<String> pkCgips) {
		this.pkCgips = pkCgips;
	}
	public String getEuhpdicttype() {
		return euhpdicttype;
	}
	public void setEuhpdicttype(String euhpdicttype) {
		this.euhpdicttype = euhpdicttype;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
    
    
}
