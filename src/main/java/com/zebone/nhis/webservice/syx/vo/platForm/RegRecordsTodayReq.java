package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegRecordsTodayReq {

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
     * 片区代码：0-院本部；1-南院
     */
    @XmlElement(name = "districtDeptId")
    private String districtDeptId;
    
    /**
     * 患者姓名
     */
    @XmlElement(name = "userName")
    private String userName;
    
    /**
     * HIS系统患者ID
     */
    @XmlElement(name = "userHisPatientId")
    private String userHisPatientId;

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

	public String getDistrictDeptId() {
		return districtDeptId;
	}

	public void setDistrictDeptId(String districtDeptId) {
		this.districtDeptId = districtDeptId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHisPatientId() {
		return userHisPatientId;
	}

	public void setUserHisPatientId(String userHisPatientId) {
		this.userHisPatientId = userHisPatientId;
	}
    
	
	
}
