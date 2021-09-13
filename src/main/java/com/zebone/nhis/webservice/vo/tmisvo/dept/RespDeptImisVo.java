package com.zebone.nhis.webservice.vo.tmisvo.dept;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 输血返回构造xml
 * @author frank
 * 科室
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespDeptImisVo {
	@XmlElement(name = "ResultCode")
	private String resultCode;
	@XmlElement(name = "ResultContent")
	private String resultContent;
	
	
	@XmlElement(name = "DeptInfos")
	private ResponseDeptSVo deptInfos;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultContent() {
		return resultContent;
	}
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	public ResponseDeptSVo getDeptInfos() {
		return deptInfos;
	}
	public void setDeptInfos(ResponseDeptSVo deptInfos) {
		this.deptInfos = deptInfos;
	}
	
	
	
	
}
