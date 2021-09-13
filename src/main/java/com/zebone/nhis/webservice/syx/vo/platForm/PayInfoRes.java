package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payListInfo")
public class PayInfoRes {

	/**
     * HIS就诊登记号
     */
    @XmlElement(name = "infoSeq")
    private String infoSeq;
    
    /**
     * HIS流水号备注说明
     */
    @XmlElement(name = "infoRmk")
    private String infoRmk;
    
    /**
     * 接诊专科名称
     */
    @XmlElement(name = "deptName")
    private String deptName;
    
    /**
     * 接诊医生姓名
     */
    @XmlElement(name = "doctorName")
    private String doctorName;
    
    /**
     * 未缴费总金额
     */
    @XmlElement(name = "payAmout")
    private String payAmout;
    
    /**
     * 患者类型名称
     */
    @XmlElement(name = "patientType")
    private String patientType;
    
    /**
     * 门诊业务类型
     */
    @XmlElement(name = "oPType")
    private String oPType;

	public String getInfoSeq() {
		return infoSeq;
	}

	public void setInfoSeq(String infoSeq) {
		this.infoSeq = infoSeq;
	}

	public String getInfoRmk() {
		return infoRmk;
	}

	public void setInfoRmk(String infoRmk) {
		this.infoRmk = infoRmk;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getoPType() {
		return oPType;
	}

	public void setoPType(String oPType) {
		this.oPType = oPType;
	}
    
    
}
