package com.zebone.nhis.webservice.syx.vo.selfmac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "req")
public class SettleInfoDetailReq {
	//方法名
    @XmlElement(name = "actionId")
	private String actionId;
    
  //结算流水号
    @XmlElement(name = "stCode")
	private String stCode;
    
    //医保目录
    @XmlElement(name = "dtHpdicttype")
	private String dtHpdicttype;
    
    //医保计划
    @XmlElement(name = "pkHp")
	private String pkHp;

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getStCode() {
		return stCode;
	}

	public void setStCode(String stCode) {
		this.stCode = stCode;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getDtHpdicttype() {
		return dtHpdicttype;
	}

	public void setDtHpdicttype(String dtHpdicttype) {
		this.dtHpdicttype = dtHpdicttype;
	}
    
}
