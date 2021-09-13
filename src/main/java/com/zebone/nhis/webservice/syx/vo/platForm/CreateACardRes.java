package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class CreateACardRes {

	/**
     * 处理结果代码：0-注册成功
     */
    @XmlElement(name = "resultCode")
    private String resultCode;
    
    /**
     * 处理结果描述
     */
    @XmlElement(name = "resultDesc")
    private String resultDesc;
    
    /**
     * 用户对应HIS系统患者ID
     */
    @XmlElement(name = "userHisPatientId")
    private String userHisPatientId;
    
    /**
     * 患者诊疗卡号码
     */
    @XmlElement(name = "patientCardNo")
    private String patientCardNo;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getUserHisPatientId() {
		return userHisPatientId;
	}

	public void setUserHisPatientId(String userHisPatientId) {
		this.userHisPatientId = userHisPatientId;
	}

	public String getPatientCardNo() {
		return patientCardNo;
	}

	public void setPatientCardNo(String patientCardNo) {
		this.patientCardNo = patientCardNo;
	}

	
}
