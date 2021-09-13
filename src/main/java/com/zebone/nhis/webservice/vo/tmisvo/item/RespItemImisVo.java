package com.zebone.nhis.webservice.vo.tmisvo.item;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 输血返回构造xml
 * @author frank
 *收费项目
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespItemImisVo {
	@XmlElement(name = "ResultCode")
	private String resultCode;
	@XmlElement(name = "ResultContent")
	private String resultContent;
	
	
	@XmlElement(name = "FeeItemInfos")
	private ResponseFeeItemSInfoVo responseFeeItemSInfoVo;
	
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
	public ResponseFeeItemSInfoVo getResponseFeeItemSInfoVo() {
		return responseFeeItemSInfoVo;
	}
	public void setResponseFeeItemSInfoVo(
			ResponseFeeItemSInfoVo responseFeeItemSInfoVo) {
		this.responseFeeItemSInfoVo = responseFeeItemSInfoVo;
	}
	
	
	
}
