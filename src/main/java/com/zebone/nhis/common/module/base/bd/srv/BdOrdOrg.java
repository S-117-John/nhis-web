package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_ORG  - bd_ord_org 
 *
 * @since 2016-09-08 01:59:43
 */
@Table(value="BD_ORD_ORG")
public class BdOrdOrg extends BaseModule  {

	@PK
	@Field(value="PK_ORDORG",id=KeyId.UUID)
    private String pkOrdorg;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="NAME_PRT")
    private String namePrt;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="QUAN_DEF")
    private double quanDef;

	@Field(value="PRICE")
    private double price;
	
	@Field(value="ADDR")
    private String addr;

    @Field(value="price_vip")
    private double priceVip;

    @Field(value="price_chd")
    private double priceChd;

    public double getPriceChd() {
        return priceChd;
    }

    public void setPriceChd(double priceChd) {
        this.priceChd = priceChd;
    }

    public double getPriceVip() {
        return priceVip;
    }

    public void setPriceVip(double priceVip) {
        this.priceVip = priceVip;
    }

    public String getPkOrdorg(){
        return this.pkOrdorg;
    }
    public void setPkOrdorg(String pkOrdorg){
        this.pkOrdorg = pkOrdorg;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getNamePrt(){
        return this.namePrt;
    }
    public void setNamePrt(String namePrt){
        this.namePrt = namePrt;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public double getQuanDef(){
        return this.quanDef;
    }
    public void setQuanDef(double quanDef){
        this.quanDef = quanDef;
    }

    public double getPrice(){
        return this.price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}

}