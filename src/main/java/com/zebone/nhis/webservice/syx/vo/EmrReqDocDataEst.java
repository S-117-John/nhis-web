package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="EST")
public class EmrReqDocDataEst {
	
	//记录日期
	@XmlElement(name="REC_TIME")
    private String recTime;
    private String operTime;
    //体温
	@XmlElement(name="TEMPE")
    private String tempe;
	//脉搏
	@XmlElement(name="PULSE")
    private String pulse;
	//呼吸
	@XmlElement(name="BREATH")
	private String breath;
	//收缩压
	@XmlElement(name="SYSTOLIC")
	private String systolic;
	//舒张压
	@XmlElement(name="DIASTOLIC")
	private String diastolic;
	//HB
	@XmlElement(name="HB")
	private String hb;
	//PLT
	@XmlElement(name="PLT")
	private String plt;
	//PT
	@XmlElement(name="PT")
	private String pt;
	//APTT
	@XmlElement(name="APTT")
	private String aptt;
	//FIG
	@XmlElement(name="FIG")
	private String fig;
	//TRIGGER
	@XmlElement(name="TRIGGER")
	private String trigger;
	
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
	public String getDocTxt() {
		return docTxt;
	}
	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}
	@XmlTransient
	public String getTempe() {
		return tempe;
	}
	public void setTempe(String tempe) {
		this.tempe = tempe;
	}
	@XmlTransient
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	@XmlTransient
	public String getBreath() {
		return breath;
	}
	public void setBreath(String breath) {
		this.breath = breath;
	}
	@XmlTransient
	public String getSystolic() {
		return systolic;
	}
	public void setSystolic(String systolic) {
		this.systolic = systolic;
	}
	@XmlTransient
	public String getDiastolic() {
		return diastolic;
	}
	public void setDiastolic(String diastolic) {
		this.diastolic = diastolic;
	}
	@XmlTransient
	public String getHb() {
		return hb;
	}
	public void setHb(String hb) {
		this.hb = hb;
	}
	@XmlTransient
	public String getPlt() {
		return plt;
	}
	public void setPlt(String plt) {
		this.plt = plt;
	}
	@XmlTransient
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
	@XmlTransient
	public String getAptt() {
		return aptt;
	}
	public void setAptt(String aptt) {
		this.aptt = aptt;
	}
	@XmlTransient
	public String getFig() {
		return fig;
	}
	public void setFig(String fig) {
		this.fig = fig;
	}
	@XmlTransient
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
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


}
