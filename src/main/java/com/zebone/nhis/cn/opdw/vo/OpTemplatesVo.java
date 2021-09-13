package com.zebone.nhis.cn.opdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;

public class OpTemplatesVo {
	private List<BdOrdSet> bdOrderSet = new ArrayList<BdOrdSet>();
	private List<BdOrdSetDt> bdOrderSetDt = new ArrayList<BdOrdSetDt>();
	
	public List<BdOrdSet> getBdOrderSet() {
		return bdOrderSet;
	}
	public void setBdOrderSet(List<BdOrdSet> bdOrderSet) {
		this.bdOrderSet = bdOrderSet;
	}
	public List<BdOrdSetDt> getBdOrderSetDt() {
		return bdOrderSetDt;
	}
	public void setBdOrderSetDt(List<BdOrdSetDt> bdOrderSetDt) {
		this.bdOrderSetDt = bdOrderSetDt;
	}
	
}
