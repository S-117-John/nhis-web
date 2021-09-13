package com.zebone.nhis.webservice.vo.tmisvo.dept;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeptInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseDeptSVo {
	@XmlElement(name = "DeptInfo")
	private List<ResponseDeptVo> deptInfo;

	public List<ResponseDeptVo> getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(List<ResponseDeptVo> deptInfo) {
		this.deptInfo = deptInfo;
	}
	
	
}
