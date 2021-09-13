package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



/**
 * 首次病程记录结果
 * @author chengjia
 *
 */
@XmlRootElement(name = "Response")
public class EmrHmRespFirstCourse{

	@XmlElement(name="Record")
    private List<EmrHmFirstCourseRslt> rstList;

	@XmlTransient
	public List<EmrHmFirstCourseRslt> getRstList() {
		return rstList;
	}

	public void setRstList(List<EmrHmFirstCourseRslt> rstList) {
		this.rstList = rstList;
	}
    

}