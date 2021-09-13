package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BlCgPdParamVo {
	private Map<String,BigDecimal> quanMinMap;//某物品对应的数量
	private List<String> pkPds;//物品主键集合
	public Map<String, BigDecimal> getQuanMinMap() {
		return quanMinMap;
	}

	public void setQuanMinMap(Map<String, BigDecimal> quanMinMap) {
		this.quanMinMap = quanMinMap;
	}

	public List<String> getPkPds() {
		return pkPds;
	}

	public void setPkPds(List<String> pkPds) {
		this.pkPds = pkPds;
	}

}
