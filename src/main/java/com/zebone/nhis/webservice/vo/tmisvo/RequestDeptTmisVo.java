package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 输血请求参数构造xml
 * @author frank
 *
 */
@XmlRootElement(name = "DeptID")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestDeptTmisVo {
	@XmlElement(name = "DeptID")
	private String deptID;
	
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	
}
