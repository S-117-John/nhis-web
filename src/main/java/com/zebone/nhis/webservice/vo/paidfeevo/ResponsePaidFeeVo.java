package com.zebone.nhis.webservice.vo.paidfeevo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 7.3.查询门诊已缴费用
 * @ClassName: ResponsePaidFeeVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 上午11:17:19     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponsePaidFeeVo {
	private String status;
	private String desc;
	private String errorMessage;
	private PaidFeeData paidFeeData;

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlElement(name = "errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	// 返回参数第二层 data
	@XmlElement(name = "data")
	public PaidFeeData getPaidFeeData() {
		return paidFeeData;
	}

	public void setPaidFeeData(PaidFeeData paidFeeData) {
		this.paidFeeData = paidFeeData;
	}

}
