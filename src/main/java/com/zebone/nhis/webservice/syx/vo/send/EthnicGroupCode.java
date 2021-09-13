package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 民族
 * @author yx
 *
 */
@XStreamAlias("ethnicGroupCode")
public class EthnicGroupCode {
	private Item item ;
	
	private DisplayName displayName;
	public Item getItem() {
		if(item==null)item=new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public DisplayName getDisplayName() {
		if(displayName == null )displayName = new DisplayName();
		return displayName;
	}

	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}
	
}
