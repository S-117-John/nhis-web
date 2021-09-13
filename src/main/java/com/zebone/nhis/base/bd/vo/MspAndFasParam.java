package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdResMsp;
import com.zebone.nhis.common.module.base.bd.res.BdResMspFa;

public class MspAndFasParam {

	/**
	 * 医疗资源
	 */
	private BdResMsp msp = new BdResMsp();
	
	/**
	 * 医疗资源和设备关系
	 */
	List<BdResMspFa> mspFas = new ArrayList<BdResMspFa>();

	public BdResMsp getMsp() {
		return msp;
	}

	public void setMsp(BdResMsp msp) {
		this.msp = msp;
	}

	public List<BdResMspFa> getMspFas() {
		return mspFas;
	}

	public void setMspFas(List<BdResMspFa> mspFas) {
		this.mspFas = mspFas;
	}
}
