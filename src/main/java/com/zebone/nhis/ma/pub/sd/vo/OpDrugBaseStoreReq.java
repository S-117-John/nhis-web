package com.zebone.nhis.ma.pub.sd.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MedInfo")
public class OpDrugBaseStoreReq {
	@XmlElement(name="Med")
    private List<OpPdBaseStoreInfo> storeBaseInfo;

	public List<OpPdBaseStoreInfo> getStoreBaseInfo() {
		return storeBaseInfo;
	}

	public void setStoreBaseInfo(List<OpPdBaseStoreInfo> storeBaseInfo) {
		this.storeBaseInfo = storeBaseInfo;
	}
	
  
}
