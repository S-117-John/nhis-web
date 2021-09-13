package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQuedocinfoResTemVo {
	@XmlElement(name = "doctInitials")
    private String doctInitials;
	public String getDoctInitials() {
		return doctInitials;
	}
	public void setDoctInitials(String doctInitials) {
		this.doctInitials = doctInitials;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	
	public String getEmpsrvtype() {
		return empsrvtype;
	}
	public void setEmpsrvtype(String empsrvtype) {
		this.empsrvtype = empsrvtype;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctDesc() {
		return doctDesc;
	}
	public void setDoctDesc(String doctDesc) {
		this.doctDesc = doctDesc;
	}
	@XmlElement(name = "doctCode")
    private String doctCode;
	@XmlElement(name = "doctName")
    private String doctName;
	@XmlElement(name = "rankName")
    private String empsrvtype;
	@XmlElement(name = "codeDept")
    private String codeDept;
	@XmlElement(name = "deptName")
    private String deptName;
	@XmlElement(name = "doctDesc")
    private String doctDesc;

	@XmlElement(name = "photo")
	private String photo;

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
