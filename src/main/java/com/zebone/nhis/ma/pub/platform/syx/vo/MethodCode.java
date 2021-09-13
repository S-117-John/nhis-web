package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("methodCode")
public class MethodCode {
	@XStreamImplicit
	private List<Item> items;
	private Item item;
	public List<Item> getItems() {
		if(items==null)items=new ArrayList<Item>();
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Item getItem() {
		if(item==null)item=new Item();
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
}
