package com.zebone.nhis.scm.material.vo;

/***
 * 物品基本属性：主键、包装单位、包装量
 * 
 * @author wj
 *
 */

public class MtlPdBaseParam {
	
	/** 物品主键 */
	private String pkPd;
	
	/** 包装单位 */
	private String pkUnitPack;
	
	/** 包装量 */
	private Integer packSize;

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

}
