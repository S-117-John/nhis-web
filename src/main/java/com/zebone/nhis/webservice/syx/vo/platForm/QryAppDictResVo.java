package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="res")
public class QryAppDictResVo {
	@XmlElement(name="item")
	private List<QryAppItem> itemList;

	public List<QryAppItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<QryAppItem> itemList) {
		this.itemList = itemList;
	} 
	
	
}
