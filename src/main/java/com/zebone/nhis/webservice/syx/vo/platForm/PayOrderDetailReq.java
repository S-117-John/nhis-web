package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrderDetailReq {
    
    /**
     * 外部预约系统的收费订单号
     */
    @XmlElement(name = "orderIdHis")
    private String orderIdHis;

	public String getOrderIdHis() {
		return orderIdHis;
	}

	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}

	
}
