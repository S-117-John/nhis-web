package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;

public class SaveHerMoudParam {
	private String pkOrdset;
	private List<BdOrdSetDt> herMouDtList = new ArrayList<BdOrdSetDt>();
	public String getPkOrdset() {
		return pkOrdset;
	}
	public void setPkOrdset(String pkOrdset) {
		this.pkOrdset = pkOrdset;
	}
	public List<BdOrdSetDt> getHerMouDtList() {
		return herMouDtList;
	}
	public void setHerMouDtList(List<BdOrdSetDt> herMouDtList) {
		this.herMouDtList = herMouDtList;
	}
	
	
}
