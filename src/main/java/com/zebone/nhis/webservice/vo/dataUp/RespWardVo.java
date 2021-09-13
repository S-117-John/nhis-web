package com.zebone.nhis.webservice.vo.dataUp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wardInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespWardVo {
	
	@XmlElement(name="wardId")
    private String wardId;
	
	
	@XmlElement(name="wardName")
    private String wardName;

	@XmlElement(name="deptId")
    private String deptId;
	
	
	@XmlElement(name="deptName")
    private String deptName;


	public String getWardId() {
		return wardId;
	}


	public void setWardId(String wardId) {
		this.wardId = wardId;
	}


	public String getWardName() {
		return wardName;
	}


	public void setWardName(String wardName) {
		this.wardName = wardName;
	}


	public String getDeptId() {
		return deptId;
	}


	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}


	public String getDeptName() {
		return deptName;
	}


	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	

}
