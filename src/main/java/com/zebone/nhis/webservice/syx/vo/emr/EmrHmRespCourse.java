package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



/**
 * 日常病程记录结果
 * @author chengjia
 *
 */
@XmlRootElement(name = "Response")
public class EmrHmRespCourse{

	@XmlElement(name="Record")
    private List<EmrHmCourseRslt> rstList;

	@XmlTransient
	public List<EmrHmCourseRslt> getRstList() {
		return rstList;
	}

	public void setRstList(List<EmrHmCourseRslt> rstList) {
		this.rstList = rstList;
	}
    

}