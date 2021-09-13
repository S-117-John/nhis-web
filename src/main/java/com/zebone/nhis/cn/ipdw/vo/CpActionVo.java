package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.cp.BdCpAction;

public class CpActionVo {
	private List<BdCpAction>  actList = new ArrayList<BdCpAction>();
    private List<BdCpAction> actListDel = new ArrayList<BdCpAction>();
	public List<BdCpAction> getActList() {
		return actList;
	}
	public void setActList(List<BdCpAction> actList) {
		this.actList = actList;
	}
	public List<BdCpAction> getActListDel() {
		return actListDel;
	}
	public void setActListDel(List<BdCpAction> actListDel) {
		this.actListDel = actListDel;
	} 
    
}
