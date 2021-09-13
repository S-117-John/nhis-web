package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class QrypiCostSubject {
	@XmlElement(name="req")
	private QryPiCostReq qryPiCostReq;

	public QryPiCostReq getQryPiCostReq() {
		return qryPiCostReq;
	}

	public void setQryPiCostReq(QryPiCostReq qryPiCostReq) {
		this.qryPiCostReq = qryPiCostReq;
	}

	
}
