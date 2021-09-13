package com.zebone.nhis.common.module.scm.st;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_CC_DETAIL  - pd_cc_detail 
 *
 * @since 2016-12-05 03:07:12
 */
@Table(value="PD_CC_DETAIL")
public class PdCcDetail extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_PDCCDT",id=KeyId.UUID)
    private String pkPdccdt;

	@Field(value="PK_PDCC")
    private String pkPdcc;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="PRICE_COST")
    private Double priceCost;

	@Field(value="PRICE")
    private Double price;

	@Field(value="AMOUNT_COST")
    private Double amountCost;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	/** 规格 */
	private String spec;
	
	/** 生产厂家 */
	private String pkFactory;

    public String getPkPdccdt(){
        return this.pkPdccdt;
    }
    public void setPkPdccdt(String pkPdccdt){
        this.pkPdccdt = pkPdccdt;
    }

    public String getPkPdcc(){
        return this.pkPdcc;
    }
    public void setPkPdcc(String pkPdcc){
        this.pkPdcc = pkPdcc;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public Double getQuanMin(){
        return this.quanMin;
    }
    public void setQuanMin(Double quanMin){
        this.quanMin = quanMin;
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

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPkFactory() {
		return pkFactory;
	}
	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}
}