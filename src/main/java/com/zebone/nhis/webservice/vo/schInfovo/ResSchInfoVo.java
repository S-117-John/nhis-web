package com.zebone.nhis.webservice.vo.schInfovo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 根据科室查询出诊专家 VO
 * @ClassName: ResSchInfoVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月16日 下午4:14:21     
 * @Copyright: 2019
 */
public class ResSchInfoVo {
	// 科室
	private String pkDept ="";
	// 医生唯一标识
	private String pkEmp="";
	// 人员名称
	private String name="";
	// 医生编码
	private String codeEmp="";
	// 专长
	private String spec="";
	// 职称
	private String empsrvtype="";
	
	private String cntUnused;
	@XmlElement(name = "spec")
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	@XmlElement(name = "empsrvtype")
	public String getEmpsrvtype() {
		return empsrvtype;
	}

	public void setEmpsrvtype(String empsrvtype) {
		this.empsrvtype = empsrvtype;
	}

	@XmlElement(name = "pkEmp")
	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	@XmlElement(name = "codeEmp")
	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name = "cntUnused")
	public String getCntUnused() {
		return cntUnused;
	}

	public void setCntUnused(String cntUnused) {
		this.cntUnused = cntUnused;
	}
	
}
