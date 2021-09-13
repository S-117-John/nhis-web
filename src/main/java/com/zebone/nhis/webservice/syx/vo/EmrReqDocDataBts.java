package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="BTS")
public class EmrReqDocDataBts {
	
	//记录日期
	@XmlElement(name="REC_TIME")
    private String recTime;
    //输血日期
	@XmlElement(name="OPER_TIME")
    private String operTime;
    //ABO血型编码
	@XmlElement(name="ABO_CODE")
    private String aboCode;
	//ABO血型名称
	@XmlElement(name="ABO_NAME")
    private String aboName;
	//用血品种
	@XmlElement(name="TYPE_CODE")
	private String typeCode;
	//用血品种名称
	@XmlElement(name="TYPE_NAME")
	private String typeName;
	//用血量
	@XmlElement(name="QTY")
	private String qty;
	//用血单位
	@XmlElement(name="UNIT")
	private String unit;
	
	//不良反应
	@XmlElement(name="REACT")
	private String react;
	
	@XmlElement(name="DOC_TXT")
	private String docTxt;
	
	@XmlTransient
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	@XmlTransient
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@XmlTransient
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	@XmlTransient
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@XmlTransient
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	@XmlTransient
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	@XmlTransient
	public String getAboCode() {
		return aboCode;
	}
	public void setAboCode(String aboCode) {
		this.aboCode = aboCode;
	}
	@XmlTransient
	public String getAboName() {
		return aboName;
	}
	public void setAboName(String aboName) {
		this.aboName = aboName;
	}
	@XmlTransient
	public String getReact() {
		return react;
	}
	public void setReact(String react) {
		this.react = react;
	}
	
	@XmlTransient
	public String getDocTxt() {
		return docTxt;
	}
	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}

}
