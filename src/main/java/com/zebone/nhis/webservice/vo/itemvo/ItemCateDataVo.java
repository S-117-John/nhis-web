package com.zebone.nhis.webservice.vo.itemvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ItemCateDataVo {
	private List<ResItemCateVo> resItemCateVos;
	@XmlElement(name = "itemCate")
	public List<ResItemCateVo> getResItemCateVos() {
		return resItemCateVos;
	}

	public void setResItemCateVos(List<ResItemCateVo> resItemCateVos) {
		this.resItemCateVos = resItemCateVos;
	}
   
}
