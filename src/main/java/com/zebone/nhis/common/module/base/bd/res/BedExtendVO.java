package com.zebone.nhis.common.module.base.bd.res;

import java.util.List;

public class BedExtendVO  extends BdResBed {
	
	/**
	 * 要新增和修改的费用明细集合
	 */
	private List<BdItemBedVO> bdItemBeds;
	
	/**
	 * 要删除的费用明细集合
	 */
	private List<BdItemBedVO> delBdItemBeds;
	
	/**
	 * 1-博爱明细模式，清空bd_res_bed中的费用明细; 0-原模式清空bd_item_bed
	 */
	private String isBedCharge;

	public List<BdItemBedVO> getBdItemBeds() {
		return bdItemBeds;
	}

	public void setBdItemBeds(List<BdItemBedVO> bdItemBeds) {
		this.bdItemBeds = bdItemBeds;
	}

	public List<BdItemBedVO> getDelBdItemBeds() {
		return delBdItemBeds;
	}

	public void setDelBdItemBeds(List<BdItemBedVO> delBdItemBeds) {
		this.delBdItemBeds = delBdItemBeds;
	}

	public String getIsBedCharge() {
		return isBedCharge;
	}

	public void setIsBedCharge(String isBedCharge) {
		this.isBedCharge = isBedCharge;
	}
	
}
