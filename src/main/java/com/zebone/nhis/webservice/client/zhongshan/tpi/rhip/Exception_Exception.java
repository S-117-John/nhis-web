package com.zebone.nhis.webservice.client.zhongshan.tpi.rhip;

import javax.xml.ws.WebFault;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebFault(name = "Exception", targetNamespace = "http://ws.adapter.bsoft.com/")
public class Exception_Exception extends java.lang.Exception {

	/**
	 * Java type that goes as soapenv:Fault detail element.
	 * 
	 */
	private com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.Exception faultInfo;

	/**
	 * 
	 * @param message
	 * @param faultInfo
	 */
	public Exception_Exception(String message,
			com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.Exception faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	/**
	 * 
	 * @param message
	 * @param faultInfo
	 * @param cause
	 */
	public Exception_Exception(String message,
			com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.Exception faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	/**
	 * 
	 * @return returns fault bean:
	 *         com.zebone.nhis.webservice.client.tpi.rhip.Exception
	 */
	public com.zebone.nhis.webservice.client.zhongshan.tpi.rhip.Exception getFaultInfo() {
		return faultInfo;
	}

}
