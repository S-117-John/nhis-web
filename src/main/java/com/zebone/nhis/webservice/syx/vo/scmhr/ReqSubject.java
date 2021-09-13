package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Subject")
public class ReqSubject {
	/**S112 药房货位数据*/
	@XmlElement(name="dispensary")
	private String dispensary;
	@XmlElement(name="drug_code")
	private String drugCode;
	@XmlElement(name="locationinfo")
	private String locationinfo;
	
	/**S114 药房窗口数据*/
	@XmlElement(name="Opwinid_code")
	private String opwinidCode;
	
	/**S161处方查询参数*/
	@XmlElement(name="prescribe_time")
	private PrescribeTime prescribeTime;
	@XmlElement(name="id")
    private String id;
	@XmlElement(name="register_id")
    private String registerId;
	@XmlElement(name="code_pati")
    private String codePati;
	@XmlElement(name="code_pvtype")
    private String codePvtype;
	

	/**
	 * S163  药品字典查询服务
	 */
	@XmlElement(name="PK_DICT_DRUG")
	private String pkDictDrug;
	@XmlElement(name="CODE_DRUG")
	private String codeDrug;
	@XmlElement(name="NAME_DRUG")
	private String nameDrug;
	
	
	public String getPkDictDrug() {
		return pkDictDrug;
	}

	public void setPkDictDrug(String pkDictDrug) {
		this.pkDictDrug = pkDictDrug;
	}

	public String getCodeDrug() {
		return codeDrug;
	}

	public void setCodeDrug(String codeDrug) {
		this.codeDrug = codeDrug;
	}

	public String getNameDrug() {
		return nameDrug;
	}

	public void setNameDrug(String nameDrug) {
		this.nameDrug = nameDrug;
	}

	public void setDispensary(String dispensary) {
		this.dispensary = dispensary;
	}

	public String getDispensary() {
		return dispensary;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugCode() {
		return drugCode;
	}

	public void setLocationinfo(String locationinfo) {
		this.locationinfo = locationinfo;
	}

	public String getLocationinfo() {
		return locationinfo;
	}

	public String getOpwinidCode() {
		return opwinidCode;
	}

	public void setOpwinidCode(String opwinidCode) {
		this.opwinidCode = opwinidCode;
	}

	public PrescribeTime getPrescribeTime() {
		return prescribeTime;
	}

	public void setPrescribeTime(PrescribeTime prescribeTime) {
		this.prescribeTime = prescribeTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getCodePati() {
		return codePati;
	}

	public void setCodePati(String codePati) {
		this.codePati = codePati;
	}

	public String getCodePvtype() {
		return codePvtype;
	}

	public void setCodePvtype(String codePvtype) {
		this.codePvtype = codePvtype;
	}
	
}
