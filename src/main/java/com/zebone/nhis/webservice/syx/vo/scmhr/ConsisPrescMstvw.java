package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 处方主表
 * 
 * @author jd
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CONSIS_PRESC_MSTVW")
public class ConsisPrescMstvw {
	/*
	 * 处方日期
	 */
	@XmlElement(name = "PRESC_DATE")
	private Date prescDate;
	@XmlElement(name = "PRESC_NO")
	/*
	 * 处方号
	 */
	private String prescNo;
	
	/*
	 * 发药药房编码
	 */
	@XmlElement(name = "DISPENSARY")
	private String dispensary;
	
	/*
	 * 处方明细
	 */
	@XmlElement(name = "CONSIS_PRESC_DTLVW")
	private List<ConsisPrescDtlvw> consisPrescDtlvw;
	
	@XmlElement(name = "OPWINID_CODE")
	private String opwinidCode;
	
	@XmlElement(name = "OPWINID_STATUS")
	private String opwinidStatus;
	
	@XmlElement(name = "OPDATE")
	private String opdate;

	public String getOpwinidCode() {
		return opwinidCode;
	}

	public void setOpwinidCode(String opwinidCode) {
		this.opwinidCode = opwinidCode;
	}

	public String getOpwinidStatus() {
		return opwinidStatus;
	}

	public void setOpwinidStatus(String opwinidStatus) {
		this.opwinidStatus = opwinidStatus;
	}

	public String getOpdate() {
		return opdate;
	}

	public void setOpdate(String opdate) {
		this.opdate = opdate;
	}

	public Date getPrescDate() {
		return prescDate;
	}

	public void setPrescDate(Date prescDate) {
		this.prescDate = prescDate;
	}

	public String getPrescNo() {
		return prescNo;
	}

	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getDispensary() {
		return dispensary;
	}

	public void setDispensary(String dispensary) {
		this.dispensary = dispensary;
	}

	public List<ConsisPrescDtlvw> getConsisPrescDtlvw() {
		return consisPrescDtlvw;
	}

	public void setConsisPrescDtlvw(List<ConsisPrescDtlvw> consisPrescDtlvw) {
		this.consisPrescDtlvw = consisPrescDtlvw;
	}
	
	
}
