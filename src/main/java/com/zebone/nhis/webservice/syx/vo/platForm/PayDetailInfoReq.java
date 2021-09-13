package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayDetailInfoReq {

    /**
     * 操作来源
     */
    @XmlElement(name = "operateType")
    private String operateType;
    
    /**
     * 用户证件类型
     */
    @XmlElement(name = "userCardType")
    private String userCardType;
    
    /**
     * 用户证件号码
     */
    @XmlElement(name = "userCardId")
    private String userCardId;
    
    /**
     * HIS就诊登记号
     */
    @XmlElement(name = "infoSeq")
    private String infoSeq;

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getUserCardType() {
		return userCardType;
	}

	public void setUserCardType(String userCardType) {
		this.userCardType = userCardType;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getInfoSeq() {
		return infoSeq;
	}

	public void setInfoSeq(String infoSeq) {
		this.infoSeq = infoSeq;
	}
    
	
	
	
}
