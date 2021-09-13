package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.compay.ins.syx.InsGzybItem;

public class SaveInsGzybItem {
	private String euItemtype;
	private String pkhp; 
    private List<InsGzybItem>  addlist = new ArrayList<InsGzybItem>();
	public String getEuItemtype() {
		return euItemtype;
	}
	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}
	public String getPkhp() {
		return pkhp;
	}
	public void setPkhp(String pkhp) {
		this.pkhp = pkhp;
	}
	public List<InsGzybItem> getAddlist() {
		return addlist;
	}
	public void setAddlist(List<InsGzybItem> addlist) {
		this.addlist = addlist;
	}
    
    
}
