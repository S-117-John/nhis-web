package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * 科室医生排班号源
 */
@XmlRootElement(name = "counts")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueSchCount {
	/*
	 *剩余号源数
	 */
	@XmlElement(name="haveCount")
	private String haveCount;
	
	//该时段可预约数量
    @XmlElement(name="availableCount")
	private String availableCount;
    
    //余数
    @XmlElement(name="thanCount")
	private String thanCount;
	/*
	 *时段描述
	 *排班描述，例如：上午，8:00-9:00
	 */
	@XmlElement(name="phaseDesc")
	private String phaseDesc;
	/*
	 *时段编码
	 */
	@XmlElement(name="phaseCode")
	private String phaseCode;
	public String getHaveCount() {
		return haveCount;
	}
	public void setHaveCount(String haveCount) {
		this.haveCount = haveCount;
	}
	public String getAvailableCount() {
		return availableCount;
	}
	public void setAvailableCount(String availableCount) {
		this.availableCount = availableCount;
	}
	public String getPhaseDesc() {
		return phaseDesc;
	}
	public void setPhaseDesc(String phaseDesc) {
		this.phaseDesc = phaseDesc;
	}
	public String getPhaseCode() {
		return phaseCode;
	}
	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}
	public String getThanCount() {
		return thanCount;
	}
	public void setThanCount(String thanCount) {
		this.thanCount = thanCount;
	}
	
}
