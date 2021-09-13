package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PRPA_IN201311UV02 标签中的<acceptAckCode> 内容对象
 * 
 * @author yx
 * 
 */
@XStreamAlias("acceptAckCode")
public class AcceptAckCode {
	@XStreamAsAttribute
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
