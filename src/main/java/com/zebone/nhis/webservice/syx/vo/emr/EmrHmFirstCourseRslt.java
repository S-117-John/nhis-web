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
//@XmlType(propOrder = { "pkRec", "codeIp", "codePi","name"})  @XmlTransient 不显示
public class EmrHmFirstCourseRslt{
	private String pkPv;

	private String pkRec;
    
    private String codeIp;
    
    private String codePi;
	
    private String name;
    
    private String nameSex;
    
    private String ageTxt;
    
    private String birthDate;
    
    private String placeBirth;
    
    private String marryName;
    
    private String dateBegin;
    
    private String pkDept;
    
    private String codeDept;
    
    private String nameDept;

    private String type;
    
    private String zs;

    private String xbs;

    private String jws;
    
    private String tgjc;
    
    private String fzjc;

    private String zdyj;

    private String cbzd;
    
    private String treatment;
    
    private String pkEmpRefer;
    
    private String referCode;
     
    private String referName;
    
    private String jbzd;
    
    private String recDate;
    
    private String docXml;
    
    private String flagFirst;
    
    private String docTxt;
    
	@XmlElement(name="FirstCourseID")
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

	@XmlTransient
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


	@XmlElement(name="JzTime")
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
	
	@XmlElement(name="Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	@XmlElement(name="Zs")
	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}
	
	@XmlElement(name="Xbs")
	public String getXbs() {
		return xbs;
	}

	public void setXbs(String xbs) {
		this.xbs = xbs;
	}

	@XmlElement(name="Jws")
	public String getJws() {
		return jws;
	}

	public void setJws(String jws) {
		this.jws = jws;
	}

	@XmlElement(name="Tgjc")
	public String getTgjc() {
		return tgjc;
	}

	public void setTgjc(String tgjc) {
		this.tgjc = tgjc;
	}

	@XmlElement(name="Fzjc")
	public String getFzjc() {
		return fzjc;
	}

	public void setFzjc(String fzjc) {
		this.fzjc = fzjc;
	}

	@XmlElement(name="Zdyj")
	public String getZdyj() {
		return zdyj;
	}

	public void setZdyj(String zdyj) {
		this.zdyj = zdyj;
	}

	@XmlElement(name="Cbzd")
	public String getCbzd() {
		return cbzd;
	}

	public void setCbzd(String cbzd) {
		this.cbzd = cbzd;
	}



	@XmlElement(name="Jbzd")
	public String getJbzd() {
		return jbzd;
	}

	public void setJbzd(String jbzd) {
		this.jbzd = jbzd;
	}
	

	@XmlElement(name="Treatment")
	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
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

	@XmlTransient
	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	@XmlTransient
	public String getDocTxt() {
		return docTxt;
	}

	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}

     
}