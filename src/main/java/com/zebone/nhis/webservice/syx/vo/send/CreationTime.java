package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * PRPA_IN201311UV02 标签中的<creationTime> 内容对象
 * @author yx
 *
 */
@XStreamAlias("creationTime")
public class CreationTime {
	@XStreamAsAttribute
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
