package com.zebone.nhis.webservice.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.platform.modules.dao.build.au.Field;
/*
 * 科室医生排班号源
 */
@XmlRootElement(name = "doctor")
@XmlAccessorType(XmlAccessType.FIELD)
public class BdOuEmpPhoto {
	
	@XmlElement(name = "doctCode")
	private String codeEmp;
	
	/**
	 * 人员名称
	 */
	@XmlElement(name = "doctName")
	private String nameEmp;
	
	
	/**
	 * 照片
	 */
	@XmlElement(name = "photo")
	private String photo;


	public String getCodeEmp() {
		return codeEmp;
	}


	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}


	public String getNameEmp() {
		return nameEmp;
	}


	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
