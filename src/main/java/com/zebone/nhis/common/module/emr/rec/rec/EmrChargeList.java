package com.zebone.nhis.common.module.emr.rec.rec;

import java.math.BigDecimal;

/**
 *
 * @author 
 */
public class EmrChargeList{
    /**
     * 
     */
    private String code;
    /**
     * 
     */
    private String name;
    
    private String itemType;
    
    /**
     * 
     */
    private BigDecimal amount;

    private BigDecimal amountPi;
    
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
    public BigDecimal getAmount(){
        return this.amount;
    }

    /**
     * 
     */
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public BigDecimal getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}    
    
    
}