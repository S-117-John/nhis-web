package com.zebone.nhis.webservice.syx.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * 住院患者入院记录结果记录
 * @author chengjia
 *
 */
@XmlRootElement(name = "RECORD")
public class EmrAdmitRecResult{

	private String pkPv;
	
    private String name;
    
    private String nameSex;
    
    private String nationName;
    
    private String ageTxt;
    
    private String marryName;
    
    private String nameDept;

    private String bedNo;
    
    private String codeIp;
    
    private String occuName;

    private String addrBirth;
    
    private String dateBegin;

    private String relator;
    
    private String complaint;
    
    private String hpi;
    
    private String docUrl;
    
    private List<EmrPvDiagList> diagList;
    
    
	@XmlElement(name="PATIENT_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="PATIENT_SEX")
	public String getNameSex() {
		return nameSex;
	}

	public void setNameSex(String nameSex) {
		this.nameSex = nameSex;
	}
	
	@XmlElement(name="NATION")
	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	@XmlElement(name="AGE")
	public String getAgeTxt() {
		return ageTxt;
	}

	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}
	
	@XmlElement(name="ISMARRIAGE")
	public String getMarryName() {
		return marryName;
	}

	public void setMarryName(String marryName) {
		this.marryName = marryName;
	}
	
	@XmlElement(name="DEPARTMENT")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	
	@XmlElement(name="BEDNO")
	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	
	@XmlElement(name="IPSEQNOTEXT")
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@XmlElement(name="PROFESSION")
	public String getOccuName() {
		return occuName;
	}

	public void setOccuName(String occuName) {
		this.occuName = occuName;
	}
	
	@XmlElement(name="BIRTHPLACE")
	public String getAddrBirth() {
		return addrBirth;
	}

	public void setAddrBirth(String addrBirth) {
		this.addrBirth = addrBirth;
	}

	@XmlElement(name="BEHOSPITAL_TIME")
	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	@XmlElement(name="ILLNESSER")
	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	@XmlElement(name="COMPLAIN")
	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	@XmlElement(name="NOWILLNESS")
	public String getHpi() {
		return hpi;
	}

	public void setHpi(String hpi) {
		this.hpi = hpi;
	}

	@XmlElement(name="REPORT_URL")
	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	@XmlElement(name="DIAGNOSELIST")
	public List<EmrPvDiagList> getDiagList() {
		return diagList;
	}

	public void setDiagList(List<EmrPvDiagList> diagList) {
		this.diagList = diagList;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

     
}