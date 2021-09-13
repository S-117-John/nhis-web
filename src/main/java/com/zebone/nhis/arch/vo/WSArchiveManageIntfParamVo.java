package com.zebone.nhis.arch.vo;

/**
 * 归档管理接口访问参数VO
 * 
 * 2017-5-15 11:06:41
 * 
 * 规定所有参数类型都是字符串类型 ，根据中山博爱医院接口文档，目前确定这三个参数为与各个系统直接互联互通的标准协议
 * 
 * @author gongxy
 * 
 */
public class WSArchiveManageIntfParamVo {

	/**
	 * 病人ID
	 */
	private String patient_id;

	/**
	 * 就诊次数
	 */
	private String times;

	/**
	 * 类型：ZY：住院；MZ:门诊；TJ:体检 字母部分全部大写
	 */
	private String visit_flag;

	public WSArchiveManageIntfParamVo() {

		super();
	}

	public WSArchiveManageIntfParamVo(String patient_id, String times, String visit_flag) {

		super();
		this.patient_id = patient_id;
		this.times = times;
		this.visit_flag = visit_flag;
	}

	public String getPatient_id() {

		return patient_id;
	}

	public void setPatient_id(String patient_id) {

		this.patient_id = patient_id;
	}

	public String getTimes() {

		return times;
	}

	public void setTimes(String times) {

		this.times = times;
	}

	public String getVisit_flag() {

		return visit_flag;
	}

	public void setVisit_flag(String visit_flag) {

		this.visit_flag = visit_flag;
	}

}
