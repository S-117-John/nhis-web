package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PayDepoResponse extends PlatFormRes<PayDepoResult> {

	@XmlElement(name="result")
	@Override
	public PayDepoResult getResult() {
		return super.result;
	}

	@Override
	public void setResult(PayDepoResult result) {
		super.result=result;
	}
	
}
