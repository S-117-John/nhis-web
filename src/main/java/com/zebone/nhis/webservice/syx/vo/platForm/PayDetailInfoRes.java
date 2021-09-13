package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orderDetailInfo")
public class PayDetailInfoRes {

	/**
     * 缴费费别
     */
    @XmlElement(name = "detailFee")
    private String detailFee;
    
    /**
     * 缴费细目流水号
     */
    @XmlElement(name = "detailID")
    private String detailID;
    
    /**
     * 缴费细目名称
     */
    @XmlElement(name = "detailName")
    private String detailName;
    
    /**
     * 缴费细目数量
     */
    @XmlElement(name = "detailCount")
    private String detailCount;
    
    /**
     * 缴费细目单位
     */
    @XmlElement(name = "detailUnit")
    private String detailUnit;
    
    /**
     * 缴费细目金额
     */
    @XmlElement(name = "detailAmout")
    private String detailAmout;

	public String getDetailFee() {
		return detailFee;
	}

	public void setDetailFee(String detailFee) {
		this.detailFee = detailFee;
	}

	public String getDetailID() {
		return detailID;
	}

	public void setDetailID(String detailID) {
		this.detailID = detailID;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(String detailCount) {
		this.detailCount = detailCount;
	}

	public String getDetailUnit() {
		return detailUnit;
	}

	public void setDetailUnit(String detailUnit) {
		this.detailUnit = detailUnit;
	}

	public String getDetailAmout() {
		return detailAmout;
	}

	public void setDetailAmout(String detailAmout) {
		this.detailAmout = detailAmout;
	}
    
    
    
}
