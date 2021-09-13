package com.zebone.nhis.common.module.ma.tpi.rhip;

import java.util.Date;

public class LisResultInfo {
	private String rptNo;
	
    private String inpatientNo;

    private String name;

    private String sex;

    private String unitName;

    private String doctor;

    private String diag;

    private String jyName;

    private String itemCode;

    private String itemName1;

    private String result;

    private String unit;

    private String sampleType;

    private Date jsDate;

    private String shDate;

    private String jyOpera;

    private String shName;

    private String refer1Min;

    private String refer1Max;

    private String refer2Min;

    private String refer2Max;

    private String itemName;

    private Short itmId;

    private Short itmOrder;

    
    public String getRptNo() {
		return rptNo;
	}

	public void setRptNo(String rptNo) {
		this.rptNo = rptNo;
	}

	public String getInpatientNo() {
        return inpatientNo;
    }

    public void setInpatientNo(String inpatientNo) {
        this.inpatientNo = inpatientNo == null ? null : inpatientNo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor == null ? null : doctor.trim();
    }

    public String getDiag() {
        return diag;
    }

    public void setDiag(String diag) {
        this.diag = diag == null ? null : diag.trim();
    }

    public String getJyName() {
        return jyName;
    }

    public void setJyName(String jyName) {
        this.jyName = jyName == null ? null : jyName.trim();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItemName1() {
        return itemName1;
    }

    public void setItemName1(String itemName1) {
        this.itemName1 = itemName1 == null ? null : itemName1.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType == null ? null : sampleType.trim();
    }

    public Date getJsDate() {
        return jsDate;
    }

    public void setJsDate(Date jsDate) {
        this.jsDate = jsDate;
    }

    public String getShDate() {
        return shDate;
    }

    public void setShDate(String shDate) {
        this.shDate = shDate == null ? null : shDate.trim();
    }

    public String getJyOpera() {
        return jyOpera;
    }

    public void setJyOpera(String jyOpera) {
        this.jyOpera = jyOpera == null ? null : jyOpera.trim();
    }

    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName == null ? null : shName.trim();
    }

    public String getRefer1Min() {
        return refer1Min;
    }

    public void setRefer1Min(String refer1Min) {
        this.refer1Min = refer1Min == null ? null : refer1Min.trim();
    }

    public String getRefer1Max() {
        return refer1Max;
    }

    public void setRefer1Max(String refer1Max) {
        this.refer1Max = refer1Max == null ? null : refer1Max.trim();
    }

    public String getRefer2Min() {
        return refer2Min;
    }

    public void setRefer2Min(String refer2Min) {
        this.refer2Min = refer2Min == null ? null : refer2Min.trim();
    }

    public String getRefer2Max() {
        return refer2Max;
    }

    public void setRefer2Max(String refer2Max) {
        this.refer2Max = refer2Max == null ? null : refer2Max.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public Short getItmId() {
        return itmId;
    }

    public void setItmId(Short itmId) {
        this.itmId = itmId;
    }

    public Short getItmOrder() {
        return itmOrder;
    }

    public void setItmOrder(Short itmOrder) {
        this.itmOrder = itmOrder;
    }
}