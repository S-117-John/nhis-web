package com.zebone.nhis.webservice.syx.vo.scmhr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class ResSubject {
	/**S112 药房货位数据*/
	@XmlElement(name="DISPENSARY")
	private String dispensary;
	@XmlElement(name="DRUG_CODE")
    private String drugCode;
	@XmlElement(name="LOCATIONINFO")
    private String locationinfo;
	@XmlElement(name="BATCHID")
    private String batchid;
	@XmlElement(name="BATCHNO")
    private String batchno;
	@XmlElement(name="PRODUCEDATE")
    private String producedate;
	@XmlElement(name="DISABLEDDATE")
    private String disableddate;
	@XmlElement(name="QUANTITY")
    private String quantity;
	
	/**S114药房窗口数据*/
	@XmlElement(name="OPWINID_CODE")
	private String opwinidCode;
	@XmlElement(name="OPWINID_NAME")
    private String opwinidName;
	
	/**S161处方查询接口数据*/
	@XmlElement(name="data_prescription")
	private List<DataPrescription> dataPrescriptions;

	/**
	 * 
	 * S163 药品字典查询服务	
	 */
	@XmlElement(name="DC_DICT_DRUG")
    private List<DcDictDrug> dcDictDrug;
	
	
	public List<DcDictDrug> getDcDictDrug() {
		if(this.dcDictDrug == null){
			dcDictDrug = new ArrayList<DcDictDrug>();
		}
		return dcDictDrug;
	}
	public void setDcDictDrug(List<DcDictDrug> dcDictDrug) {
		this.dcDictDrug = dcDictDrug;
	}
	public String getDispensary() {
		return dispensary;
	}
	public void setDispensary(String dispensary) {
		this.dispensary = dispensary;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getLocationinfo() {
		return locationinfo;
	}
	public void setLocationinfo(String locationinfo) {
		this.locationinfo = locationinfo;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getProducedate() {
		return producedate;
	}
	public void setProducedate(String producedate) {
		this.producedate = producedate;
	}
	public String getDisableddate() {
		return disableddate;
	}
	public void setDisableddate(String disableddate) {
		this.disableddate = disableddate;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getOpwinidCode() {
		return opwinidCode;
	}
	public void setOpwinidCode(String opwinidCode) {
		this.opwinidCode = opwinidCode;
	}
	public String getOpwinidName() {
		return opwinidName;
	}
	public void setOpwinidName(String opwinidName) {
		this.opwinidName = opwinidName;
	}
	public List<DataPrescription> getDataPrescriptions() {
		if(dataPrescriptions==null) dataPrescriptions=new ArrayList<DataPrescription>();
		return dataPrescriptions;
	}
	public void setDataPrescriptions(List<DataPrescription> dataPrescriptions) {
		this.dataPrescriptions = dataPrescriptions;
	}
	
	
}
