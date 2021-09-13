package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QueueListReq {

	/**
     * 出诊日期：yyyymmdd
     */
    @XmlElement(name = "clinicDate")
    private String clinicDate;
    
    /*
     * 出诊午别
     */
    @XmlElement(name = "clinicTime")
    private String clinicTime;

    /**
     * 叫号状态（0：等候，1：就诊中，2：过号，3：完成，4：取消, 空为全部）
     */
    @XmlElement(name = "callingFlag")
    private String callingFlag;

	public String getClinicDate() {
		return clinicDate;
	}

	public void setClinicDate(String clinicDate) {
		this.clinicDate = clinicDate;
	}

	public String getClinicTime() {
		return clinicTime;
	}

	public void setClinicTime(String clinicTime) {
		this.clinicTime = clinicTime;
	}

	public String getCallingFlag() {
		return callingFlag;
	}

	public void setCallingFlag(String callingFlag) {
		this.callingFlag = callingFlag;
	}
    
}
