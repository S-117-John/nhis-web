package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.BdInvcateItem;
import com.zebone.nhis.common.module.base.bd.price.BdInvcateItemcate;

public class InvcateItemAndItemCatesParam {

	private BdInvcateItem invItem = new BdInvcateItem();
	
	private List<BdInvcateItemcate> invcateItemcateList = new ArrayList<BdInvcateItemcate>();

	public BdInvcateItem getInvItem() {
		return invItem;
	}

	public void setInvItem(BdInvcateItem invItem) {
		this.invItem = invItem;
	}

	public List<BdInvcateItemcate> getInvcateItemcateList() {
		return invcateItemcateList;
	}

	public void setInvcateItemcateList(List<BdInvcateItemcate> invcateItemcateList) {
		this.invcateItemcateList = invcateItemcateList;
	}
}
