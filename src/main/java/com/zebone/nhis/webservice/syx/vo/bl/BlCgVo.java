package com.zebone.nhis.webservice.syx.vo.bl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlCgVo {
	@XmlElement(name = "code_pi")
	private String code_pi;
	@XmlElement(name = "code_pv")
	private String code_pv;
	@XmlElement(name = "code_item")
	private String code_item;
	@XmlElement(name = "name_cg")
	private String name_cg;
	@XmlElement(name = "quan")
	private Double quan;
	@XmlElement(name = "code_dept")
	private String code_dept;
	@XmlElement(name = "code_dept_ns")
	private String code_dept_ns;
	@XmlElement(name = "code_emp_phy")
	private String code_emp_phy;
	@XmlElement(name = "code_dept_ex")
	private String code_dept_ex;
	@XmlElement(name = "pk_cnord")
	private String pk_cnord;
	@XmlElement(name = "date_hap")
	private String date_hap;
	@XmlElement(name = "date_cg")
	private String date_cg;
	@XmlElement(name = "code_emp_cg")
	private String code_emp_cg;

	public String getCode_pi() {
		return code_pi;
	}

	public void setCode_pi(String code_pi) {
		this.code_pi = code_pi;
	}

	public String getCode_pv() {
		return code_pv;
	}

	public void setCode_pv(String code_pv) {
		this.code_pv = code_pv;
	}

	public String getCode_item() {
		return code_item;
	}

	public void setCode_item(String code_item) {
		this.code_item = code_item;
	}

	public String getName_cg() {
		return name_cg;
	}

	public void setName_cg(String name_cg) {
		this.name_cg = name_cg;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getCode_dept() {
		return code_dept;
	}

	public void setCode_dept(String code_dept) {
		this.code_dept = code_dept;
	}

	public String getCode_dept_ns() {
		return code_dept_ns;
	}

	public void setCode_dept_ns(String code_dept_ns) {
		this.code_dept_ns = code_dept_ns;
	}

	public String getCode_emp_phy() {
		return code_emp_phy;
	}

	public void setCode_emp_phy(String code_emp_phy) {
		this.code_emp_phy = code_emp_phy;
	}

	public String getCode_dept_ex() {
		return code_dept_ex;
	}

	public void setCode_dept_ex(String code_dept_ex) {
		this.code_dept_ex = code_dept_ex;
	}

	public String getPk_cnord() {
		return pk_cnord;
	}

	public void setPk_cnord(String pk_cnord) {
		this.pk_cnord = pk_cnord;
	}

	public String getDate_hap() {
		return date_hap;
	}

	public void setDate_hap(String date_hap) {
		this.date_hap = date_hap;
	}

	public String getDate_cg() {
		return date_cg;
	}

	public void setDate_cg(String date_cg) {
		this.date_cg = date_cg;
	}

	public String getCode_emp_cg() {
		return code_emp_cg;
	}

	public void setCode_emp_cg(String code_emp_cg) {
		this.code_emp_cg = code_emp_cg;
	}
	
	
}
