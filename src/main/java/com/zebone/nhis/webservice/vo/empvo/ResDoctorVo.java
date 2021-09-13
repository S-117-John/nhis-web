package com.zebone.nhis.webservice.vo.empvo;

import javax.xml.bind.annotation.XmlElement;

/**
 *  医生相关信息VO
 * @ClassName: ResDoctorVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月15日 下午5:37:52     
 * @Copyright: 2019
 */
public class ResDoctorVo {
	// 科室
	private String pkDept;
	// 所属机构
	private String pkOrg;
	// 医生唯一标识
	private String pkEmp;
	// 医生名称
	private String nameEmp;
	// 医生编码
	private String codeEmp;
	// 性别
	private String sex;
	// 工作电话
	private String workphone;
	// 职称
	private String empsrvtype;
	// 专长
	private String spec;

	@XmlElement(name = "pkEmp")
	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	@XmlElement(name = "nameEmp")
	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	@XmlElement(name = "codeEmp")
	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	@XmlElement(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@XmlElement(name = "workphone")
	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	@XmlElement(name = "empsrvtype")
	public String getEmpsrvtype() {
		return empsrvtype;
	}

	public void setEmpsrvtype(String empsrvtype) {
		this.empsrvtype = empsrvtype;
	}

	@XmlElement(name = "spec")
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

}
