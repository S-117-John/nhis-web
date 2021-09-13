package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="response")
public class QryAppSettleResponse extends PlatFormRes<QryappSettleResult>{

	@XmlElement(name="result")
	@Override
	public QryappSettleResult getResult() {
		// TODO Auto-generated method stub
		return super.result;
	}

	@Override
	public void setResult(QryappSettleResult result) {
		// TODO Auto-generated method stub
		super.result=result;
	}
	
 
}
