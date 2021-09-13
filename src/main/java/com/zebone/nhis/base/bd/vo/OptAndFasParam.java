package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdResOpt;
import com.zebone.nhis.common.module.base.bd.res.BdResOptFa;

public class OptAndFasParam {

	/**
	 * 手术台
	 */
	private BdResOpt opt = new BdResOpt();
	
	/**
	 * 手术台与设备关系
	 */
	List<BdResOptFa> optFas = new ArrayList<BdResOptFa>();

	public BdResOpt getOpt() {
		return opt;
	}

	public void setOpt(BdResOpt opt) {
		this.opt = opt;
	}

	public List<BdResOptFa> getOptFas() {
		return optFas;
	}

	public void setOptFas(List<BdResOptFa> optFas) {
		this.optFas = optFas;
	}
}
