package com.zebone.nhis.webservice.vo.appoinvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 5.6.查询预约挂号信息 VO
 * @ClassName: ResAppointmentVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月18日 上午9:20:06     
 * @Copyright: 2019
 */
public class ResAppointmentVo {
	// 预约主键
	private String pkSchappt;
	// 就诊治疗日期
	private String dateAppt;
	// 资源名称
	private String shcres;
	
	private String pkEmp;
	
	private String beginTime;
	
	private String endTime;
	
	private String nameDept;
	
	private String pkDept;

	private String code;
	
	private String euStatus;
	
	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	
	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name = "beginTime")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	@XmlElement(name = "pkEmp")
	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	@XmlElement(name = "pkSchappt")
	public String getPkSchappt() {
		return pkSchappt;
	}

	public void setPkSchappt(String pkSchappt) {
		this.pkSchappt = pkSchappt;
	}

	@XmlElement(name = "dateAppt")
	public String getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(String dateAppt) {
		this.dateAppt = dateAppt;
	}

	@XmlElement(name = "shcres")
	public String getShcres() {
		return shcres;
	}

	public void setShcres(String shcres) {
		this.shcres = shcres;
	}

	@XmlElement(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name = "euStatus")
	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	@XmlElement(name = "endTime")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
