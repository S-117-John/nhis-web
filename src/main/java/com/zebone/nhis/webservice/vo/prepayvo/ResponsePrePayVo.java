package com.zebone.nhis.webservice.vo.prepayvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 7.14.查询住院预交金充值记录
查询住院预交金充值记录 VO
 * @ClassName: ResponsePrePayVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 下午2:18:16     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponsePrePayVo {
	private String status;
	private String desc;
	private String errorMessage;
	private PrePayData prePayData;

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
	public PrePayData getPrePayData() {
		return prePayData;
	}

	public void setPrePayData(PrePayData prePayData) {
		this.prePayData = prePayData;
	}

}
