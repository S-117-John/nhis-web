package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class QryPatiInfoRegResponse extends PlatFormRes<QryPatiInfoRegResult> {

	@XmlElement(name="result")
	@Override
	public QryPatiInfoRegResult getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}

	@Override
	public void setResult(QryPatiInfoRegResult result) {
		// TODO Auto-generated method stub
		super.result=result;
	}

}
