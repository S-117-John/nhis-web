package com.zebone.nhis.webservice.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.syx.vo.bl.BlCgVo;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlRequest {
	@XmlElement(name="func_id")
	private String funcId;
	
	@XmlElementWrapper(name = "datalist")
	@XmlElement(name = "data")
	private List<BlCgVo> datalist;

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public List<BlCgVo> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<BlCgVo> datalist) {
		this.datalist = datalist;
	}
	
}
