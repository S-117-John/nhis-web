package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_STOCK - pd_stock 
 *
 * @since 2016-11-04 09:27:38
 */
@Table(value="PD_STOCK")
public class PdStock extends BaseModule  {

	@PK
	@Field(value="PK_PDSTOCK",id=KeyId.UUID)
    private String pkPdstock;

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

    /** PRICE_COST - 该如何计算? */
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

	@Field(value="QUAN_PREP")
    private Double quanPrep;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="FLAG_STOP_OP")
    private String flagStopOp;
	
	@Field(value="FLAG_STOP_ER")
    private String flagStopEr;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdstock(){
        return this.pkPdstock;
    }
    public void setPkPdstock(String pkPdstock){
        this.pkPdstock = pkPdstock;
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

    public Double getQuanPrep(){
        return this.quanPrep;
    }
    public void setQuanPrep(Double quanPrep){
        this.quanPrep = quanPrep;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getFlagStopOp() {
		return flagStopOp;
	}
	public void setFlagStopOp(String flagStopOp) {
		this.flagStopOp = flagStopOp;
	}
	public String getFlagStopEr() {
		return flagStopEr;
	}
	public void setFlagStopEr(String flagStopEr) {
		this.flagStopEr = flagStopEr;
	}
    
}