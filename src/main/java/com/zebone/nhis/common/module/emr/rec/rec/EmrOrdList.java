package com.zebone.nhis.common.module.emr.rec.rec;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
public class EmrOrdList{
    /**
     * 
     */
    private String pkCnord;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkOrd;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String code;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private String spec;
    /**
     * 
     */
    private String supplyCode;
    /**
     * 
     */
    private String supplyName;
    /**
     * 
     */
    private String freqCode;
    /**
     * 
     */
    private String freqName;
    /**
     * 
     */
    private BigDecimal ordsn;
    /**
     * 
     */
    private BigDecimal ordsnParent;
    /**
     * 
     */
    private String orderFlag;
    /**
     * 
     */
    private String orderStatus;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private Date beginDate;
    
    private Date endDate;
    
    /**
     * 
     */
    private String pkEmpOrd;
    /**
     * 
     */
    private String nameEmpOrd;
    
    private String codeOrdtype;

    private String pkUnitDos;
    private String dosage; 
    private String dosageUnit;
    
    private String delFlag;
 
    private String flagMedout;
    
    private String freqNameChn;
    private String nameUnitDos;
 
    private String flagDrug;
   
    private String flagFit;
    
    private String descFit;
    
    private String ordtypeName;
    
    private String groupno;
    
    
    
    public String getGroupno() {
		return groupno;
	}

	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}

	public String getOrdtypeName() {
		return ordtypeName;
	}

	public void setOrdtypeName(String ordtypeName) {
		this.ordtypeName = ordtypeName;
	}

	public String getNameUnitDos() {
  		return nameUnitDos;
  	}

    public void setNameUnitDos(String nameUnitDos) {
  		this.nameUnitDos = nameUnitDos;
  	}
    

    
    /**
     * 
     */
    public String getPkCnord(){
        return this.pkCnord;
    }

    /**
     * 
     */
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }    
    /**
     * 
     */

    public String getPkOrg(){
        return this.pkOrg;
    }
    /**
     * 
   */
     
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getPkOrd(){
        return this.pkOrd;
    }

    /**
     * 
     */
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }    
    /**
     * 
     */
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }    
    /**
     * 
     */
    public String getCode(){
        return this.code;
    }

    /**
     * 
     */
    public void setCode(String code){
        this.code = code;
    }    
    /**
     * 
     */
    public String getName(){
        return this.name;
    }

    /**
     * 
     */
    public void setName(String name){
        this.name = name;
    }    
    /**
     * 
     */
    public String getSpec(){
        return this.spec;
    }

    /**
     * 
     */
    public void setSpec(String spec){
        this.spec = spec;
    }    
    /**
     * 
     */
    public String getSupplyCode(){
        return this.supplyCode;
    }

    /**
     * 
     */
    public void setSupplyCode(String supplyCode){
        this.supplyCode = supplyCode;
    }    
    /**
     * 
     */
    public String getSupplyName(){
        return this.supplyName;
    }

    /**
     * 
     */
    public void setSupplyName(String supplyName){
        this.supplyName = supplyName;
    }    
    /**
     * 
     */
    public String getFreqCode(){
        return this.freqCode;
    }

    /**
     * 
     */
    public void setFreqCode(String freqCode){
        this.freqCode = freqCode;
    }    
    /**
     * 
     */
    public String getFreqName(){
        return this.freqName;
    }

    /**
     * 
     */
    public void setFreqName(String freqName){
        this.freqName = freqName;
    }    
    /**
     * 
     */
    public BigDecimal getOrdsn(){
        return this.ordsn;
    }

    /**
     * 
     */
    public void setOrdsn(BigDecimal ordsn){
        this.ordsn = ordsn;
    }    
    /**
     * 
     */
    public BigDecimal getOrdsnParent(){
        return this.ordsnParent;
    }

    /**
     * 
     */
    public void setOrdsnParent(BigDecimal ordsnParent){
        this.ordsnParent = ordsnParent;
    }    
    /**
     * 
     */
    public String getOrderFlag(){
        return this.orderFlag;
    }

    /**
     * 
     */
    public void setOrderFlag(String orderFlag){
        this.orderFlag = orderFlag;
    }    
    /**
     * 
     */
    public String getOrderStatus(){
        return this.orderStatus;
    }

    /**
     * 
     */
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public Date getBeginDate(){
        return this.beginDate;
    }

    /**
     * 
     */
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }    
    /**
     * 
     */
    public String getPkEmpOrd(){
        return this.pkEmpOrd;
    }

    /**
     * 
     */
    public void setPkEmpOrd(String pkEmpOrd){
        this.pkEmpOrd = pkEmpOrd;
    }    
    /**
     * 
     */
    public String getNameEmpOrd(){
        return this.nameEmpOrd;
    }

    /**
     * 
     */
    public void setNameEmpOrd(String nameEmpOrd){
        this.nameEmpOrd = nameEmpOrd;
    }

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getPkUnitDos() {
		return pkUnitDos;
	}

	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDosageUnit() {
		return dosageUnit;
	}

	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFlagMedout() {
		return flagMedout;
	}

	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}

	public String getFreqNameChn() {
		return freqNameChn;
	}

	public void setFreqNameChn(String freqNameChn) {
		this.freqNameChn = freqNameChn;
	}

	public String getFlagDrug() {
		return flagDrug;
	}

	public void setFlagDrug(String flagDrug) {
		this.flagDrug = flagDrug;
	}    
	
	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	} 
}
