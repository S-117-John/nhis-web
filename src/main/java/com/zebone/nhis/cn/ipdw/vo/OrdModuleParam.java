package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;

public class OrdModuleParam {
	private String pkOrdSet;
    private BdOrdSet  bdOrdSet = new BdOrdSet() ;
    private List<BdOrdSetDt> bdOrdSetDt = new ArrayList<BdOrdSetDt>();
	public BdOrdSet getBdOrdSet() {
		return bdOrdSet;
	}
	public void setBdOrdSet(BdOrdSet bdOrdSet) {
		this.bdOrdSet = bdOrdSet;
	}
	public List<BdOrdSetDt> getBdOrdSetDt() {
		return bdOrdSetDt;
	}
	public void setBdOrdSetDt(List<BdOrdSetDt> bdOrdSetDt) {
		this.bdOrdSetDt = bdOrdSetDt;
	}
	public String getPkOrdSet() {
		return pkOrdSet;
	}
	public void setPkOrdSet(String pkOrdSet) {
		this.pkOrdSet = pkOrdSet;
	}

    
}
