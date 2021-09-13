package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="Response")
public class QryPatiInfoResponse extends PlatFormRes<QryPatiInfoResult>{

	@XmlElement(name="result")
	@Override
	public QryPatiInfoResult getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}

	@Override
	public void setResult(QryPatiInfoResult result) {
		// TODO Auto-generated method stub
		super.result=result;
	}
	
}
