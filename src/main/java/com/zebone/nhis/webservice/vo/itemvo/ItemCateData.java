package com.zebone.nhis.webservice.vo.itemvo;

import javax.xml.bind.annotation.XmlElement;

public class ItemCateData {
	private ItemCateDataVo itemCateDataVo;
	@XmlElement(name = "itemCateList")
	public ItemCateDataVo getItemCateDataVo() {
		return itemCateDataVo;
	}

	public void setItemCateDataVo(ItemCateDataVo itemCateDataVo) {
		this.itemCateDataVo = itemCateDataVo;
	}
	
}
