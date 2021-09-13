package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.platform.modules.dao.build.au.Field;


/**
 * 护理级别VO
 */
public class EmrPageNurseLevelVo {

	/**
	 * 护理值
	 */
	@Field(value="quan")
	private Double quan;
	
	/**
	 * 护理名称
	 */
	@Field(value="name_cg")
	private String nameCg;

	/**
	 * 护理编号
	 */
	@Field(value="code")
	private String code;

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
