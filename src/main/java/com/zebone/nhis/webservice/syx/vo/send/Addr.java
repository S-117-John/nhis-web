package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("addr")
public class Addr {
	@XStreamAsAttribute
	private String XSI_TYPE;//需要替换为xsi:type
	private Item item;
	private Part part;
	
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}
	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}
	public Item getItem() {
		if(item==null)item=new Item();
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Part getPart() {
		if(part==null)part = new Part();
		return part;
	}
	public void setPart(Part part) {
		this.part = part;
	}
	
}
