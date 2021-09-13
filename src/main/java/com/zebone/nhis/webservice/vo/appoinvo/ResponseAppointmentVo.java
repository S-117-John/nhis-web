package com.zebone.nhis.webservice.vo.appoinvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 4.3.查询医生信息VO 返回
 *  
 * @ClassName: ResponseDoctorVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月15日 下午5:43:01     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponseAppointmentVo {
	private String status;
	private String desc;
	private String errorMessage;
	private AppointmentData datalist;

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
	public AppointmentData getResAppointmentVo() {
		return datalist;
	}

	public void setResAppointmentVo(AppointmentData datalist) {
		this.datalist = datalist;
	}

}
