package com.zebone.nhis.ma.pub.lb.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class RespHighValueDataVo {
	@JSONField(name = "HRPBARCODE")
    private String hrpBarCode;
	
	@JSONField(name = "QTY")
    private String qty;
	@JSONField(name = "PERIODOFVALIDITY")
    private String PERIODOFVALIDITY;
	@JSONField(name = "MATERIALNUMBER")
    private String MATERIALNUMBER;
	@JSONField(name = "MATERIALNAME")
    private String MATERIALNAME;
    @JSONField(name = "MODEL")
    private String MODEL;
    @JSONField(name = "IMPLANTED")
    private String IMPLANTED;
		
    @JSONField(name = "HISBARCODE")
    private String HISBARCODE;
    @JSONField(name = "CHARGEAMOUNT")
    private String CHARGEAMOUNT;
    @JSONField(name = "CHARGEDATE")
    private String CHARGEDATE;
    @JSONField(name = "FHISMATNUMBER")
    private String FHISMATNUMBER;
    @JSONField(name = "FHISMATNAME")
    private String FHISMATNAME;
    @JSONField(name = "FHISSUPNUMBER")
    private String FHISSUPNUMBER;
    @JSONField(name = "FHISSUPNAME")
    private String FHISSUPNAME;
    @JSONField(name = "HISDEPNUMBER")
    private String HISDEPNUMBER;
    @JSONField(name = "HISDEPNAME")
    private String HISDEPNAME;
    @JSONField(name = "HISAPPLYDEPTNUMBER")
    private String HISAPPLYDEPTNUMBER;
    @JSONField(name = "HISAPPLYDEPTNAME")
    private String HISAPPLYDEPTNAME;
    @JSONField(name = "PATIENTNUMBER")
    private String PATIENTNUMBER;
    @JSONField(name = "PATIENTNAME")
    private String PATIENTNAME;
    @JSONField(name = "OUTPATIENTNUMBER")
    private String OUTPATIENTNUMBER;
    @JSONField(name = "REGISTRATIONNO")
    private String REGISTRATIONNO;
    @JSONField(name = "MANUFACTURER")
    private String MANUFACTURER;
	public String getHrpBarCode() {
		return hrpBarCode;
	}
	public void setHrpBarCode(String hrpBarCode) {
		this.hrpBarCode = hrpBarCode;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getPERIODOFVALIDITY() {
		return PERIODOFVALIDITY;
	}
	public void setPERIODOFVALIDITY(String pERIODOFVALIDITY) {
		PERIODOFVALIDITY = pERIODOFVALIDITY;
	}
	public String getMATERIALNUMBER() {
		return MATERIALNUMBER;
	}
	public void setMATERIALNUMBER(String mATERIALNUMBER) {
		MATERIALNUMBER = mATERIALNUMBER;
	}
	public String getMATERIALNAME() {
		return MATERIALNAME;
	}
	public void setMATERIALNAME(String mATERIALNAME) {
		MATERIALNAME = mATERIALNAME;
	}
	public String getMODEL() {
		return MODEL;
	}
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}
	public String getIMPLANTED() {
		return IMPLANTED;
	}
	public void setIMPLANTED(String iMPLANTED) {
		IMPLANTED = iMPLANTED;
	}
	public String getHISBARCODE() {
		return HISBARCODE;
	}
	public void setHISBARCODE(String hISBARCODE) {
		HISBARCODE = hISBARCODE;
	}
	public String getCHARGEAMOUNT() {
		return CHARGEAMOUNT;
	}
	public void setCHARGEAMOUNT(String cHARGEAMOUNT) {
		CHARGEAMOUNT = cHARGEAMOUNT;
	}
	public String getCHARGEDATE() {
		return CHARGEDATE;
	}
	public void setCHARGEDATE(String cHARGEDATE) {
		CHARGEDATE = cHARGEDATE;
	}
	public String getFHISMATNUMBER() {
		return FHISMATNUMBER;
	}
	public void setFHISMATNUMBER(String fHISMATNUMBER) {
		FHISMATNUMBER = fHISMATNUMBER;
	}
	public String getFHISMATNAME() {
		return FHISMATNAME;
	}
	public void setFHISMATNAME(String fHISMATNAME) {
		FHISMATNAME = fHISMATNAME;
	}
	public String getFHISSUPNUMBER() {
		return FHISSUPNUMBER;
	}
	public void setFHISSUPNUMBER(String fHISSUPNUMBER) {
		FHISSUPNUMBER = fHISSUPNUMBER;
	}
	public String getFHISSUPNAME() {
		return FHISSUPNAME;
	}
	public void setFHISSUPNAME(String fHISSUPNAME) {
		FHISSUPNAME = fHISSUPNAME;
	}
	public String getHISDEPNUMBER() {
		return HISDEPNUMBER;
	}
	public void setHISDEPNUMBER(String hISDEPNUMBER) {
		HISDEPNUMBER = hISDEPNUMBER;
	}
	public String getHISDEPNAME() {
		return HISDEPNAME;
	}
	public void setHISDEPNAME(String hISDEPNAME) {
		HISDEPNAME = hISDEPNAME;
	}
	public String getHISAPPLYDEPTNUMBER() {
		return HISAPPLYDEPTNUMBER;
	}
	public void setHISAPPLYDEPTNUMBER(String hISAPPLYDEPTNUMBER) {
		HISAPPLYDEPTNUMBER = hISAPPLYDEPTNUMBER;
	}
	public String getHISAPPLYDEPTNAME() {
		return HISAPPLYDEPTNAME;
	}
	public void setHISAPPLYDEPTNAME(String hISAPPLYDEPTNAME) {
		HISAPPLYDEPTNAME = hISAPPLYDEPTNAME;
	}
	public String getPATIENTNUMBER() {
		return PATIENTNUMBER;
	}
	public void setPATIENTNUMBER(String pATIENTNUMBER) {
		PATIENTNUMBER = pATIENTNUMBER;
	}
	public String getPATIENTNAME() {
		return PATIENTNAME;
	}
	public void setPATIENTNAME(String pATIENTNAME) {
		PATIENTNAME = pATIENTNAME;
	}
	public String getOUTPATIENTNUMBER() {
		return OUTPATIENTNUMBER;
	}
	public void setOUTPATIENTNUMBER(String oUTPATIENTNUMBER) {
		OUTPATIENTNUMBER = oUTPATIENTNUMBER;
	}
	public String getREGISTRATIONNO() {
		return REGISTRATIONNO;
	}
	public void setREGISTRATIONNO(String rEGISTRATIONNO) {
		REGISTRATIONNO = rEGISTRATIONNO;
	}
	public String getMANUFACTURER() {
		return MANUFACTURER;
	}
	public void setMANUFACTURER(String mANUFACTURER) {
		MANUFACTURER = mANUFACTURER;
	}
    
    
}
