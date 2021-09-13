package com.zebone.nhis.webservice.vo.picardvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 6.1.查询是否存在就诊卡 
 *查询患者是否存在在用就诊卡。
 * @ClassName: ResponsePiCardVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月18日 下午3:02:28     
 * @Copyright: 2019
 */
//返回结构第一层
@XmlRootElement(name = "res")
public class ResponsePiCardVo {
	private String status;
	private String desc;
	private String errorMessage;
	private ResPiCardVo resPiCardVo;

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
	public ResPiCardVo getResPiCardVo() {
		return resPiCardVo;
	}

	public void setResPiCardVo(ResPiCardVo resPiCardVo) {
		this.resPiCardVo = resPiCardVo;
	}

}
