package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PiAddrInfoRes {

	@XmlElementWrapper(name = "addrList")
	@XmlElement(name="addrInfo")
	private List<PiAddrInfo> addrList;

	public List<PiAddrInfo> getAddrList() {
		return addrList;
	}

	public void setAddrList(List<PiAddrInfo> addrList) {
		this.addrList = addrList;
	}
}
