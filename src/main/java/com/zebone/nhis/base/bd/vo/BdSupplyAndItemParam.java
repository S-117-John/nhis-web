package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdSupplyItem;

/**
 * 医嘱用法以及医嘱用法附加费用
 * @author wangpeng
 * @date 2016年8月29日
 *
 */
public class BdSupplyAndItemParam {
	
	/** 医嘱用法 */
	private BdSupply supply = new BdSupply();
	
	/** 医嘱用法附加费用 */
	private List<BdSupplyItem> itemList = new ArrayList<BdSupplyItem>();

	public BdSupply getSupply() {
		return supply;
	}

	public void setSupply(BdSupply supply) {
		this.supply = supply;
	}

	public List<BdSupplyItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BdSupplyItem> itemList) {
		this.itemList = itemList;
	}
}
