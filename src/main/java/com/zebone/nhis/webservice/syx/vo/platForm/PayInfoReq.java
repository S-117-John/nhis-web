package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayInfoReq {

	/**
     * 开始日期（YYYY-MM-DD）
     */
    @XmlElement(name = "startDate")
    private String startDate;
    
    /**
     * 结束日期（YYYY-MM-DD）
     */
    @XmlElement(name = "endDate")
    private String endDate;
    
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
     * 查询标志
     */
    @XmlElement(name = "flag")
    private String flag;
    
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
