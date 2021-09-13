package com.zebone.nhis.webservice.vo.hospinfovo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 6.2.查询患者住院就诊信息 
  *查询患者当前就诊状态的住院就诊信息。
 * @ClassName: ResponseHospInfoVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月19日 上午11:20:59     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponseHospInfoVo {
	private String status;
	private String desc;
	private String errorMessage;
	private HospInfoData hospInfoData;

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
	public HospInfoData getHospInfoData() {
		return hospInfoData;
	}

	public void setHospInfoData(HospInfoData hospInfoData) {
		this.hospInfoData = hospInfoData;
	}

}
