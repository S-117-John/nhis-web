package com.zebone.nhis.scm.st.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdPay;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;

public class PdPayVo extends PdPay {
	private String spr;
	private String bank;
	private String paymode;
	private List<PdStDtVo> dtlist;
	
	

	public String getPaymode() {
		return paymode;
	}

	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}

	public List<PdStDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<PdStDtVo> dtlist) {
		this.dtlist = dtlist;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

}
