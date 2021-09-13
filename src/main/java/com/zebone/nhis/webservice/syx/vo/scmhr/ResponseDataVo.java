package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 华润接受xml转化vo
 * @author jd
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="ROOT")
public class ResponseDataVo {
	/**
	 * 返回参数
	 */
	@XmlElement(name="RETVAL")
	private String retval;
	/**
	 * 执行信息或错误信 息 
	 */
	@XmlElement(name="RETMSG")
    private String retmsg;
	/**
	 * 方法是否执行成功  1 执行成功  其他值 执行失败 
	 */
	@XmlElement(name="RETCODE")
    private String retcode;
	
	public String getRetval() {
		return retval;
	}
	public void setRetval(String retval) {
		this.retval = retval;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	
	
}
