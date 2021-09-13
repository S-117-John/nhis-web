package com.zebone.nhis.scm.st.vo;

import java.util.List;

import com.zebone.nhis.scm.pub.vo.PdRepriceHistVo;

public class PdRepriceSubVo {
	private List<PdRepriceHistVo> histList;
	
	private String PkPdrep;
	
	private String euRepmode;

	public List<PdRepriceHistVo> getHistList() {
		return histList;
	}

	public void setHistList(List<PdRepriceHistVo> histList) {
		this.histList = histList;
	}

	public String getPkPdrep() {
		return PkPdrep;
	}

	public void setPkPdrep(String pkPdrep) {
		PkPdrep = pkPdrep;
	}

	public String getEuRepmode() {
		return euRepmode;
	}

	public void setEuRepmode(String euRepmode) {
		this.euRepmode = euRepmode;
	}
	
	
}
