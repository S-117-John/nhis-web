package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *公共返回体
 */
@XmlRootElement(name = "respCommon")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespCommonVo {
	/**
	 *交易结果
	 *1：成功；
     *0：失败；
     *-1：异常，
	 */
	@XmlElement(name="resultCode")
    private String resultCode;
	
	/**
	 *结果描述
	 */
	@XmlElement(name="resultMsg")
    private String resultMsg;
	
	/**
	 *系统时间
	 */
	@XmlElement(name="systemTime")
    private String systemTime;
	
	/**
	 *预留字段
	 */
	@XmlElement(name="reserved")
    private String reserved;


    public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
    public void setReserved(String reserved) {
         this.reserved = reserved;
     }
     public String getReserved() {
         return reserved;
     }
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}
     
     
}
