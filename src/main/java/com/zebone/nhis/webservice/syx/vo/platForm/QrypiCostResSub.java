package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class QrypiCostResSub {
	@XmlElement(name="res")
	private QryPiCostResVo  qryPiCostRes;

	public QryPiCostResVo getQryPiCostRes() {
		return qryPiCostRes;
	}

	public void setQryPiCostRes(QryPiCostResVo qryPiCostRes) {
		this.qryPiCostRes = qryPiCostRes;
	}
	
	
}
