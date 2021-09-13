package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 机构响应消息体
 */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class BdOuOrgVo {
	/**
	 * 医院编码
	 */
	@XmlElement(name = "hospId")
	private String codeOrg;
	/**
	 * 医院名称
	 */
	@XmlElement(name = "hospName")
	private String nameOrg;
	/**
	 * 公共返回体实体
	 */
	@XmlElement(name = "respCommon")
	private RespCommonVo resVo;
	
	public String getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public RespCommonVo getResVo() {
		return resVo;
	}
	public void setResVo(RespCommonVo resVo) {
		this.resVo = resVo;
	}
}
