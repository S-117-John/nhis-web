package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_INV_INIT 
 *
 * @since 2018-09-18 06:22:27
 */
@Table(value="PD_INV_INIT")
public class PdInvInit extends BaseModule  {

	@PK
	@Field(value="PK_PDINVINIT",id=KeyId.UUID)
    private String pkPdinvinit;

	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="BATCH_NO")
    private String batchNo;

	@Field(value="DATE_EXPIRE")
    private Date dateExpire;

	@Field(value="PRICE_COST")
    private Double priceCost;

	@Field(value="PRICE")
    private Double price;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="AMOUNT_COST")
    private Double amountCost;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="DATE_INPUT")
    private Date dateInput;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="FLAG_CC")
    private String flagCc;

	@Field(value="PK_PDCC")
    private String pkPdcc;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdinvinit(){
        return this.pkPdinvinit;
    }
    public void setPkPdinvinit(String pkPdinvinit){
        this.pkPdinvinit = pkPdinvinit;
    }
    
    public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getBatchNo(){
        return this.batchNo;
    }
    public void setBatchNo(String batchNo){
        this.batchNo = batchNo;
    }

    public Date getDateExpire(){
        return this.dateExpire;
    }
    public void setDateExpire(Date dateExpire){
        this.dateExpire = dateExpire;
    }

    public Double getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Double priceCost){
        this.priceCost = priceCost;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getQuanMin(){
        return this.quanMin;
    }
    public void setQuanMin(Double quanMin){
        this.quanMin = quanMin;
    }

    public Double getAmountCost(){
        return this.amountCost;
    }
    public void setAmountCost(Double amountCost){
        this.amountCost = amountCost;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Date getDateInput(){
        return this.dateInput;
    }
    public void setDateInput(Date dateInput){
        this.dateInput = dateInput;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public String getFlagCc(){
        return this.flagCc;
    }
    public void setFlagCc(String flagCc){
        this.flagCc = flagCc;
    }

    public String getPkPdcc(){
        return this.pkPdcc;
    }
    public void setPkPdcc(String pkPdcc){
        this.pkPdcc = pkPdcc;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}