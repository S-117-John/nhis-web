package com.zebone.nhis.webservice.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * 住院患者基本信息记录结果
 * @author Kongxuedong
 *
 */
@XmlRootElement(name = "RESPONSE")
public class EmrPatInfoRtn {

	@XmlElement(name="RECORD")
	private List<EmrPatInfoResult> rstList;

	@XmlTransient
	public List<EmrPatInfoResult> getRstList() {
		return rstList;
	}

	public void setRstList(List<EmrPatInfoResult> rstList) {
		this.rstList = rstList;
	}
	
	
}
