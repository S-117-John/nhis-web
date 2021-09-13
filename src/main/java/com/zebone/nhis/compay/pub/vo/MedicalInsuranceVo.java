package com.zebone.nhis.compay.pub.vo;

import java.util.List;

/**
 * 医保字典保存参数
 */
public class MedicalInsuranceVo {

	private String pkHp;
	private List<DictTypesData> dictTypes;
	
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public List<DictTypesData> getDictTypes() {
		return dictTypes;
	}
	public void setDictTypes(List<DictTypesData> dictTypes) {
		this.dictTypes = dictTypes;
	}
}
