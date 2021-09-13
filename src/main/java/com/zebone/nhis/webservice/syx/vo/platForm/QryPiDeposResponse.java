package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public class QryPiDeposResponse extends PlatFormRes<QryPiDeposResult> {

	@XmlElement(name="result")
	@Override
	public QryPiDeposResult getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}

	@Override
	public void setResult(QryPiDeposResult result) {
		// TODO Auto-generated method stub
		this.result=result;
	}
	
}
