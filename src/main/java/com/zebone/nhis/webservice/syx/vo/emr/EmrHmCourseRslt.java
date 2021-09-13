package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;



/**
 * 首次病程记录
 * @author chengjia
 *
 */
@XmlRootElement(name = "Record")
public class EmrHmCourseRslt{
	private String pkPv;

	private String pkRec;
    
    private String codeIp;
    
    private String codePi;
	
    private String name;
    
    private String nameSex;
    
    private String ageTxt;
    
    private String birthDate;
    
    private String marryName;
    
    private String placeBirth;
    
    private String dateBegin;
    
    private String pkDept;
    
    private String codeDept;
    
    private String nameDept;

    private String zs;

    private String ryqk;

    private String ryzd;
    
    private String zljg;
    
    private String mqqk;

    private String mqzd;

    private String zljh;

    private String pkEmpRefer;
    
    private String referCode;
     
    private String referName;

    private String recDate;
    
    private String docXml;

    private String docTxt;
    
	@XmlElement(name="CourseID")
	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}
	
	@XmlElement(name="Mrn")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name="PID")
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	@XmlElement(name="PatientName")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="Sex")
	public String getNameSex() {
		return nameSex;
	}

	public void setNameSex(String nameSex) {
		this.nameSex = nameSex;
	}

	@XmlElement(name="Age")
	public String getAgeTxt() {
		return ageTxt;
	}

	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}
	
	@XmlElement(name="Birthday")
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	@XmlElement(name="MaritalStatus")
	public String getMarryName() {
		return marryName;
	}

	public void setMarryName(String marryName) {
		this.marryName = marryName;
	}

	@XmlElement(name="Address")
	public String getPlaceBirth() {
		return placeBirth;
	}

	public void setPlaceBirth(String placeBirth) {
		this.placeBirth = placeBirth;
	}


	@XmlElement(name="InHospTime")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	@XmlElement(name="DeptID")
	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}


	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name="DeptName")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name="Zs")
	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}
	
	@XmlElement(name="Ryqk")
	public String getRyqk() {
		return ryqk;
	}

	public void setRyqk(String ryqk) {
		this.ryqk = ryqk;
	}
	
	@XmlElement(name="Ryzd")
	public String getRyzd() {
		return ryzd;
	}

	public void setRyzd(String ryzd) {
		this.ryzd = ryzd;
	}

	@XmlElement(name="Zljg")
	public String getZljg() {
		return zljg;
	}

	public void setZljg(String zljg) {
		this.zljg = zljg;
	}

	@XmlElement(name="Mqqk")
	public String getMqqk() {
		return mqqk;
	}

	public void setMqqk(String mqqk) {
		this.mqqk = mqqk;
	}	
	
	@XmlElement(name="Mqzd")
	public String getMqzd() {
		return mqzd;
	}

	public void setMqzd(String mqzd) {
		this.mqzd = mqzd;
	}

	@XmlElement(name="NextZljh")
	public String getZljh() {
		return zljh;
	}

	public void setZljh(String zljh) {
		this.zljh = zljh;
	}

	@XmlElement(name="DoctorID")
	public String getReferCode() {
		return referCode;
	}
	public void setReferCode(String referCode) {
		this.referCode = referCode;
	}

	@XmlElement(name="DoctorName")
	public String getReferName() {
		return referName;
	}

	public void setReferName(String referName) {
		this.referName = referName;
	}

	@XmlTransient
	public String getPkEmpRefer() {
		return pkEmpRefer;
	}

	public void setPkEmpRefer(String pkEmpRefer) {
		this.pkEmpRefer = pkEmpRefer;
	}

	@XmlElement(name="ReordTime")
	public String getRecDate() {
		return recDate;
	}

	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}

	@XmlTransient
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
	@XmlTransient
	public String getDocXml() {
		return docXml;
	}

	public void setDocXml(String docXml) {
		this.docXml = docXml;
	}

	@XmlElement(name="DocTxt")
	public String getDocTxt() {
		return docTxt;
	}

	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}
     
}