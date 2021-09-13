package com.zebone.nhis.ma.kangMei.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.zebone.nhis.ma.kangMei.vo.HeadReq;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {"headReq","dataReq"
})
@XmlRootElement(name = "orderInfo")
public class OrderInfoReq {

	@XmlElement(name = "data")
	private DataReq dataReq;

	@XmlElement(name = "head")
	private HeadReq headReq;

	public DataReq getDataReq() {
		if (dataReq == null) {
			dataReq = new DataReq();
		}
		return dataReq;
	}

	public void setDataReq(DataReq dataReq) {
		this.dataReq = dataReq;
	}

	public HeadReq getHeadReq() {
		if (headReq == null) {
			headReq = new HeadReq();
		}
		return headReq;
	}

	public void setHeadReq(HeadReq headReq) {
		this.headReq = headReq;
	}

}
