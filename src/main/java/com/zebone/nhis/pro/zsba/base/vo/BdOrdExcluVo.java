package com.zebone.nhis.pro.zsba.base.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdExclu;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdExcluDt;

public class BdOrdExcluVo {

	//排斥定义
	private BdOrdExclu bdOrdExclu;
	
	//排斥列表
	private List<BdOrdExcluDt> bdOrdExcluDtList;

	public BdOrdExclu getBdOrdExclu() {
		return bdOrdExclu;
	}

	public void setBdOrdExclu(BdOrdExclu bdOrdExclu) {
		this.bdOrdExclu = bdOrdExclu;
	}

	public List<BdOrdExcluDt> getBdOrdExcluDtList() {
		return bdOrdExcluDtList;
	}

	public void setBdOrdExcluDtList(List<BdOrdExcluDt> bdOrdExcluDtList) {
		this.bdOrdExcluDtList = bdOrdExcluDtList;
	}
}
