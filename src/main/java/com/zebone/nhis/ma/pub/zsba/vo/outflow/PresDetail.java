package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 处方明细
 */
@JSONType(ignores = {"presNo"})
public class PresDetail {
    private String doseUnit;
    private Integer drugDays;
    private Double drugDose;
    private String drugPathways;
    private String drugRate;
    private String generalName;
    private Integer packQtyOutter;
    private Integer packUnitOutter;
    private  String packUnitOutterTxt;
    private String specDesc;
    private String specId;
    private String drugId;
    private String drugName;
    private String remark;
    private String drugRateTxt;
    private  String doseUnitTxt;
    private  String drugPathwaysTxt;

    private String presNo;

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public Integer getDrugDays() {
        return drugDays;
    }

    public void setDrugDays(Integer drugDays) {
        this.drugDays = drugDays;
    }

    public Double getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(Double drugDose) {
        this.drugDose = drugDose;
    }

    public String getDrugPathways() {
        return drugPathways;
    }

    public void setDrugPathways(String drugPathways) {
        this.drugPathways = drugPathways;
    }

    public String getDrugRate() {
        return drugRate;
    }

    public void setDrugRate(String drugRate) {
        this.drugRate = drugRate;
    }

    public String getGeneralName() {
        return generalName;
    }

    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }

    public Integer getPackQtyOutter() {
        return packQtyOutter;
    }

    public void setPackQtyOutter(Integer packQtyOutter) {
        this.packQtyOutter = packQtyOutter;
    }

    public Integer getPackUnitOutter() {
        return packUnitOutter;
    }

    public void setPackUnitOutter(Integer packUnitOutter) {
        this.packUnitOutter = packUnitOutter;
    }

    public String getSpecDesc() {
        return specDesc;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getDrugRateTxt() {
        return drugRateTxt;
    }

    public void setDrugRateTxt(String drugRateTxt) {
        this.drugRateTxt = drugRateTxt;
    }

    public String getDoseUnitTxt() {
        return doseUnitTxt;
    }

    public void setDoseUnitTxt(String doseUnitTxt) {
        this.doseUnitTxt = doseUnitTxt;
    }

    public String getDrugPathwaysTxt() {
        return drugPathwaysTxt;
    }

    public void setDrugPathwaysTxt(String drugPathwaysTxt) {
        this.drugPathwaysTxt = drugPathwaysTxt;
    }

    public String getPackUnitOutterTxt() {
        return packUnitOutterTxt;
    }

    public void setPackUnitOutterTxt(String packUnitOutterTxt) {
        this.packUnitOutterTxt = packUnitOutterTxt;
    }
}