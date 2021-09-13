package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiA1028Vo extends  HeadVo {
	@XmlElement(name = "Body")
	private List<UseCardDataVo> useCardDataList;

	public List<UseCardDataVo> getUseCardDataList() {
		return useCardDataList;
	}

	public void setUseCardDataList(List<UseCardDataVo> useCardDataList) {
		this.useCardDataList = useCardDataList;
	}
}
