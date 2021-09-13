package com.zebone.nhis.webservice.syx.vo.selfmac;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SettleInfoDetail {


	private List<SettleInfoVo> result;

	public List<SettleInfoVo> getResult() {
		return result;
	}

	public void setResult(List<SettleInfoVo> result) {
		this.result = result;
	}
	
	
}
