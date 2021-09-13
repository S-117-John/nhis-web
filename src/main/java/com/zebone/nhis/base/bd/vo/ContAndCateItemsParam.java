package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.srv.BdCateCont;
import com.zebone.nhis.common.module.base.bd.srv.BdCateItem;

public class ContAndCateItemsParam {

	/**
	 * 收费项目自定义分类内容
	 */
	private BdCateCont contInfo = new BdCateCont();
	
	/**
	 * 收费项目与自定义分类对照
	 */
	private List<BdCateItem> cateItemList = new ArrayList<BdCateItem>();

	public BdCateCont getContInfo() {
		return contInfo;
	}

	public void setContInfo(BdCateCont contInfo) {
		this.contInfo = contInfo;
	}

	public List<BdCateItem> getCateItemList() {
		return cateItemList;
	}

	public void setCateItemList(List<BdCateItem> cateItemList) {
		this.cateItemList = cateItemList;
	}
	
}
