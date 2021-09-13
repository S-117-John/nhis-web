package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class RoomListResSubject {

	//结果代码 0：成功 其他：失败
	@XmlElement(name="resultCode")
    private String resultCode;
	
	@XmlElement(name="errorMsg")
    private String errorMsg;
	
    @XmlElementWrapper(name="itemlist")
    @XmlElement(name="item")
    private List<RoomListRes> item;

	public List<RoomListRes> getItem() {
		return item;
	}

	public void setItem(List<RoomListRes> item) {
		this.item = item;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	

    
}
