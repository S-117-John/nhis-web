package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("value")
public class Value {
	@XStreamAsAttribute
	private String code;
	@XStreamAsAttribute
	private String codeSystem;
	@XStreamAsAttribute
	private String codeSystemName;
	private DisplayName displayName;
	@XStreamAsAttribute
	private String XSI_TYPE;
	@XStreamAsAttribute
	private String value;
	
	private String unit;
	
	private Item item;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getCodeSystemName() {
		return codeSystemName;
	}
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	public DisplayName getDisplayName() {
		if(displayName==null)displayName=new DisplayName();
		return displayName;
	}
	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}
	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Item getItem() {
		if(item==null)item=new Item();
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
