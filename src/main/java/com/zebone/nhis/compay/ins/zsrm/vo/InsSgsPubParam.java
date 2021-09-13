package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="program")
public class InsSgsPubParam {

	   
   // @XmlAttribute(name="return_code")
    @XmlElement(name="return_code") 
	private String return_code;//返回状态码   1>0 -成功，<0 -失败,-9-session过期
    
    //@XmlAttribute(name="return_code_message")
    @XmlElement(name="return_code_message") 
	private String return_code_message;//提示信息

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_code_message() {
		return return_code_message;
	}

	public void setReturn_code_message(String return_code_message) {
		this.return_code_message = return_code_message;
	}
    
    
	
}
