package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="REQUEST")
public class EmrRequestData {
	
	//住院患者ID/PATIENTID
	@XmlElement(name="PATIENTID")
    private String codePi;
    //住院号/IPSEQNOTEXT
	@XmlElement(name="IPSEQNOTEXT")
    private String codeIp;
    //住院次数/IPTIMES
	@XmlElement(name="IPTIMES")
    private String ipTimes;
	//操作日期
	@XmlElement(name="OPERATION_TIME")
    private String operTime;
	//患者姓名/PATIENTNAME
	@XmlElement(name="PATIENTNAME")
	private String name;
	@XmlElement(name="EMP_CODE")
	private String empCode;
	@XmlElement(name="EMP_NAME")
	private String empName;
	
	@XmlElement(name="DOCDATA")
	private EmrReqDocData docData;
	
	@XmlTransient
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlTransient
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	
	@XmlTransient
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	
	@XmlTransient
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	
	@XmlTransient
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	
	@XmlTransient
	public EmrReqDocData getDocData() {
		return docData;
	}
	public void setDocData(EmrReqDocData docData) {
		this.docData = docData;
	}
	@XmlTransient
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	@XmlTransient
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	

	
}
