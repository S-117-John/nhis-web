package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ITEM_PRICE  - bd_item_price 
 *
 * @since 2016-09-09 10:33:49
 */
@Table(value="BD_ITEM_PRICE")
public class BdItemPrice extends BaseModule  {

	@PK
	@Field(value="PK_ITEMPRICE",id=KeyId.UUID)
    private String pkItemprice;

	@Field(value="PK_ITEM")
    private String pkItem;

    /** EU_PRICETYPE - 1 省级 2 市级 3 县级 9 其他 */
	@Field(value="EU_PRICETYPE")
    private String euPricetype;

	@Field(value="PRICE")
    private double price;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="MODIFIER")
	private String modifier;
	
	//数据行修改标识
	private String flagModify;
	
	public String getFlagModify() {
		return flagModify;
	}
	public void setFlagModify(String flagModify) {
		this.flagModify = flagModify;
	}
	public String getPkItemprice(){
        return this.pkItemprice;
    }
    public void setPkItemprice(String pkItemprice){
        this.pkItemprice = pkItemprice;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getEuPricetype(){
        return this.euPricetype;
    }
    public void setEuPricetype(String euPricetype){
        this.euPricetype = euPricetype;
    }

    public double getPrice(){
        return this.price;
    }
    public void setPrice(double price){
        this.price = price;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}