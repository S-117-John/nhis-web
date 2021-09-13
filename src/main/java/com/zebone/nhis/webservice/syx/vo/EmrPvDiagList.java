package com.zebone.nhis.webservice.syx.vo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.arch.vo.Fileinfo;



/**
 * 诊断列表
 * @author chengjia
 *
 */
@XmlRootElement(name = "DIAGNOSE")
public class EmrPvDiagList{

	private String pkPv;
	
	private String mode;
	
	private String type;
	
	private String dtDiagType;
    
    private String codeIcd;
    
    private String nameDiag;
    
    private String descDiag;
    
    private Date dateDiag;

    private String nameEmpDiag;

    @XmlElement(name="MODE")
    public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@XmlElement(name="TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDtDiagType() {
		return dtDiagType;
	}


	public void setDtDiagType(String dtDiagType) {
		this.dtDiagType = dtDiagType;
	}
	
	@XmlElement(name="DISEASE_CODE")
	public String getCodeIcd() {
		return codeIcd;
	}

	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}

	@XmlElement(name="DISEASE_NAME")
	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	@XmlElement(name="DESCRIPTION")
	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	@XmlElement(name="DIAGNOSE_TIME")
	public Date getDateDiag() {
		return dateDiag;
	}

	public void setDateDiag(Date dateDiag) {
		this.dateDiag = dateDiag;
	}

	@XmlElement(name="ATTENDING_NAME")
	public String getNameEmpDiag() {
		return nameEmpDiag;
	}

	public void setNameEmpDiag(String nameEmpDiag) {
		this.nameEmpDiag = nameEmpDiag;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

    
	

     
}