package com.zebone.nhis.webservice.syx.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



/**
 * 住院患者入院记录结果
 * @author chengjia
 *
 */
@XmlRootElement(name = "RESPONSE")
public class EmrAdmitRecRtn{

	@XmlElement(name="RECORD")
    private List<EmrAdmitRecResult> rstList;

	@XmlTransient
	public List<EmrAdmitRecResult> getRstList() {
		return rstList;
	}

	public void setRstList(List<EmrAdmitRecResult> rstList) {
		this.rstList = rstList;
	}
    
    
	
}