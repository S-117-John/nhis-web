package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Ipt_LeaveDiagnosis_new")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptLeaveDiagnosisNew {

    @XmlElement(name = "CYZDQTZDJBMC")
    protected String cyzdqtzdjbmc;
    @XmlElement(name = "CYZDQTZDJBBM")
    protected String cyzdqtzdjbbm;
    @XmlElement(name = "CYZDQTZDCYQK")
    protected String cyzdqtzdcyqk;
    
	public String getCyzdqtzdjbmc() {
		return cyzdqtzdjbmc;
	}
	public void setCyzdqtzdjbmc(String cyzdqtzdjbmc) {
		this.cyzdqtzdjbmc = cyzdqtzdjbmc;
	}
	public String getCyzdqtzdjbbm() {
		return cyzdqtzdjbbm;
	}
	public void setCyzdqtzdjbbm(String cyzdqtzdjbbm) {
		this.cyzdqtzdjbbm = cyzdqtzdjbbm;
	}
	public String getCyzdqtzdcyqk() {
		return cyzdqtzdcyqk;
	}
	public void setCyzdqtzdcyqk(String cyzdqtzdcyqk) {
		this.cyzdqtzdcyqk = cyzdqtzdcyqk;
	}

}
