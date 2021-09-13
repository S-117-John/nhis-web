package com.zebone.nhis.ma.pub.platform.syx.vo.scmhr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="subject")
public class ReqSubject {
	@XmlElement(name="action")
	private String action;
	@XmlElement(name="data_prescription")
    private List<DataPrescription> dataPrescription;
	@XmlElement(name="DC_DICT_DRUG")
	private DcDictDrug dcDictDrug;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<DataPrescription>  getDataPrescription() {
		if(dataPrescription==null)dataPrescription=new ArrayList<DataPrescription>();
		return dataPrescription;
	}
	public void setDataPrescription(List<DataPrescription>  dataPrescription) {
		this.dataPrescription = dataPrescription;
	}
	public DcDictDrug getDcDictDrug() {
		return dcDictDrug;
	}
	public void setDcDictDrug(DcDictDrug dcDictDrug) {
		this.dcDictDrug = dcDictDrug;
	}
	
	
}
