package com.zebone.nhis.webservice.syx.vo.selfmac;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SettleInfoDt {

	private List<SettleInfoDtVo> result;

	public List<SettleInfoDtVo> getResult() {
		return result;
	}

	public void setResult(List<SettleInfoDtVo> result) {
		this.result = result;
	}
}
