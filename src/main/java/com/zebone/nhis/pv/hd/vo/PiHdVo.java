package com.zebone.nhis.pv.hd.vo;

import com.zebone.nhis.pi.pub.vo.PiMasterAndAddr;

public class PiHdVo extends PiMasterAndAddr {
	private String dtHdtype;
	private String codeHd;
	private Integer cntWeek;
	
	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public Integer getCntWeek() {
		return cntWeek;
	}

	public void setCntWeek(Integer cntWeek) {
		this.cntWeek = cntWeek;
	}

	public String getCodeHd() {
		return codeHd;
	}

	public void setCodeHd(String codeHd) {
		this.codeHd = codeHd;
	}
}
