package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class QryPiCostResponse extends PlatFormRes<QryPiCostResult> {
	@XmlElement(name="result")
	@Override
	public QryPiCostResult getResult() {
		return super.result;
	}

	@Override
	public void setResult(QryPiCostResult result) {
		super.result=result;
	}
	
}
