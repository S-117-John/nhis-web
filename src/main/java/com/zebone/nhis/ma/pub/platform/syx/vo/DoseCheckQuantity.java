package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("doseCheckQuantity")
public class DoseCheckQuantity {
	@XStreamAsAttribute
	private String XSI_TYPE;
	private Item item;
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}
	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}
	public Item getItem() {
		if(item==null) item=new Item();
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
}
