package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class DataElementmini implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JSONField(name = "DATA_ELEMENT_EN_NAME",ordinal = 1)
    private String enName;

    @JSONField(name = "DATA_ELEMENT_VALUE",ordinal = 2)
    private Object value;

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

    
}
