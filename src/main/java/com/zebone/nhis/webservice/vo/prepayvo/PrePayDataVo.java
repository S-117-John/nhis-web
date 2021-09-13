package com.zebone.nhis.webservice.vo.prepayvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PrePayDataVo {
	private List<ResPrePayVo> resPrePayVo;

	@XmlElement(name = "rechargeRec")

	public List<ResPrePayVo> getResPrePayVo() {
		return resPrePayVo;
	}

	public void setResPrePayVo(List<ResPrePayVo> resPrePayVo) {
		this.resPrePayVo = resPrePayVo;
	}

}
