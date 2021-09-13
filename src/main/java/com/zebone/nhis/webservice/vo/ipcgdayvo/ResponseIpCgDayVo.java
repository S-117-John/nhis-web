package com.zebone.nhis.webservice.vo.ipcgdayvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 7.16.查询住院一日清单
 * @ClassName: ResponseIpCgDayVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月22日 上午10:50:40     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponseIpCgDayVo {
	private String status;
	private String desc;
	private String errorMessage;
	private IpCgDayData ipCgCgDayData;

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
	public IpCgDayData getIpCgCgDayData() {
		return ipCgCgDayData;
	}

	public void setIpCgCgDayData(IpCgDayData ipCgCgDayData) {
		this.ipCgCgDayData = ipCgCgDayData;
	}

}
