package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdDaycgSet;
import com.zebone.nhis.common.module.base.bd.price.BdDaycgSetItem;

/**
 * 
 * 固定收费套定义以及收费项目
 * @author wangpeng
 * @date 2016年9月1日
 *
 */
public class BdDaycgSetAndItemParam {
	
	/** 固定收费套定义 */
	private BdDaycgSet daycgSet = new BdDaycgSet();
	
	/** 固定收费套收费项目 */
	private List<BdDaycgSetItem> itemList = new ArrayList<BdDaycgSetItem>();

	public BdDaycgSet getDaycgSet() {
		return daycgSet;
	}

	public void setDaycgSet(BdDaycgSet daycgSet) {
		this.daycgSet = daycgSet;
	}

	public List<BdDaycgSetItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BdDaycgSetItem> itemList) {
		this.itemList = itemList;
	}

}
