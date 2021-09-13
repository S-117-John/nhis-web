package com.zebone.nhis.ex.nis.ns.vo;

import com.zebone.nhis.common.module.base.bd.mk.BdTermFreqTime;

public class FreqTimeVo extends BdTermFreqTime{
	public String euCycle;//周期类型

	public String getEuCycle() {
		return euCycle;
	}

	public void setEuCycle(String euCycle) {
		this.euCycle = euCycle;
	}

}
