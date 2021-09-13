package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PRPA_IN201311UV02 标签中的<processingCode> 内容对象
 * @author IBM
 *
 */
@XStreamAlias("processingCode")
public class ProcessingCode {
	@XStreamAsAttribute
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
