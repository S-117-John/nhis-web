package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_DEPTUSING 
 *
 * @since 2018-08-20 06:51:47
 */
@Table(value="PD_DEPTUSING")
public class PdDeptusing extends BaseModule  {

	@PK
	@Field(value="PK_DEPTUSING",id=KeyId.UUID)
    private String pkDeptusing;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="PK_DEPT_USE")
    private String pkDeptUse;

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

	@Field(value="QUAN")
    private Double quan;

	@Field(value="AMOUNT_COST")
    private Double amountCost;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="DATE_BEIGN")
    private Date dateBeign;

	@Field(value="PK_PDSTDT_OUT")
    private String pkPdstdtOut;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;


    public String getPkDeptusing(){
        return this.pkDeptusing;
    }
    public void setPkDeptusing(String pkDeptusing){
        this.pkDeptusing = pkDeptusing;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
    }

    public String getPkDeptUse(){
        return this.pkDeptUse;
    }
    public void setPkDeptUse(String pkDeptUse){
        this.pkDeptUse = pkDeptUse;
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

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
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

    public Date getDateBeign(){
        return this.dateBeign;
    }
    public void setDateBeign(Date dateBeign){
        this.dateBeign = dateBeign;
    }

    public String getPkPdstdtOut(){
        return this.pkPdstdtOut;
    }
    public void setPkPdstdtOut(String pkPdstdtOut){
        this.pkPdstdtOut = pkPdstdtOut;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
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
}