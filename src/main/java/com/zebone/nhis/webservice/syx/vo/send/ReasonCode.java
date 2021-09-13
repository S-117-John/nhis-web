package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("reasonCode")
public class ReasonCode {
	private Item item;

	public Item getItem() {
		if(item==null)item=new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
