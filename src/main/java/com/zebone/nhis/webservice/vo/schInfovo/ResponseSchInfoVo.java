package com.zebone.nhis.webservice.vo.schInfovo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 5.1.根据科室查询出诊专家
 * @ClassName: ResponseSchInfoVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月16日 下午3:55:18     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponseSchInfoVo {
	private String status;
	private String desc;
	private String errorMessage;
	private SchInfoData schInfoData;

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
	public SchInfoData getSchInfoData() {
		return schInfoData;
	}

	public void setSchInfoData(SchInfoData schInfoData) {
		this.schInfoData = schInfoData;
	}

}
