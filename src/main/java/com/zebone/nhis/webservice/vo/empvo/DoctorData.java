package com.zebone.nhis.webservice.vo.empvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 医生data vo
 * @ClassName: DoctorData   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月16日 上午9:44:28     
 * @Copyright: 2019
 */
public class DoctorData {
	private DoctorDataVo doctorDataVo;

	// 返回参数第三层
	@XmlElement(name = "doctorList")
	public DoctorDataVo getDoctorDataVo() {
		return doctorDataVo;
	}

	public void setDoctorDataVo(DoctorDataVo doctorDataVo) {
		this.doctorDataVo = doctorDataVo;
	}

}
