package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_REPRICE_DETAIL - pd_reprice_detail 
 *
 * @since 2016-11-10 01:36:52
 */
@Table(value="PD_REPRICE_DETAIL")
public class PdRepriceDetail extends BaseModule  {

	@PK
	@Field(value="PK_PDREPDT",id=KeyId.UUID)
    private String pkPdrepdt;

	@Field(value="PK_PDREP")
    private String pkPdrep;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="PRICE_ORG")
    private Double priceOrg;

	@Field(value="PRICE")
    private Double price;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdrepdt(){
        return this.pkPdrepdt;
    }
    public void setPkPdrepdt(String pkPdrepdt){
        this.pkPdrepdt = pkPdrepdt;
    }

    public String getPkPdrep(){
        return this.pkPdrep;
    }
    public void setPkPdrep(String pkPdrep){
        this.pkPdrep = pkPdrep;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnitPack(){
        return this.pkUnitPack;
    }
    public void setPkUnitPack(String pkUnitPack){
        this.pkUnitPack = pkUnitPack;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public Double getPriceOrg(){
        return this.priceOrg;
    }
    public void setPriceOrg(Double priceOrg){
        this.priceOrg = priceOrg;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}