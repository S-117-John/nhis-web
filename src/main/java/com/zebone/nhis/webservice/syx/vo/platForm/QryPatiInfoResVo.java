package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="res")
public class QryPatiInfoResVo {

	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
	@XmlElement(name="patiInfo")
	private List<QryPatiInfoVo> patiInfoVos;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public List<QryPatiInfoVo> getPatiInfoVos() {
		return patiInfoVos;
	}

	public void setPatiInfoVos(List<QryPatiInfoVo> patiInfoVos) {
		this.patiInfoVos = patiInfoVos;
	}
	
	
}
