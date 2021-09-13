package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("targetSiteCode")
public class TargetSiteCode {
	
	private Item item;

	public Item getItem() {
		if(item==null)item = new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
}
