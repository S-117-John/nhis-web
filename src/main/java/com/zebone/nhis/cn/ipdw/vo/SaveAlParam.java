package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.pi.PiAllergic;

public class SaveAlParam {

	private String pk_pi;
	private List<PiAllergic> piAlList = new ArrayList<PiAllergic>();
	public String getPk_pi() {
		return pk_pi;
	}
	public void setPk_pi(String pk_pi) {
		this.pk_pi = pk_pi;
	}
	public List<PiAllergic> getPiAlList() {
		return piAlList;
	}
	public void setPiAlList(List<PiAllergic> piAlList) {
		this.piAlList = piAlList;
	}
	
}
