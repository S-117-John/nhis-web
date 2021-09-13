package com.zebone.nhis.common.module.ex.nis.pd;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_BASE - bd_pd_base 
 *
 * @since 2016-10-21 09:19:09
 */
@Table(value="BD_PD_BASE")
public class BdPdBase extends BaseModule  {

	@PK
	@Field(value="PK_PDBASE",id=KeyId.UUID)
    private String pkPdbase;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

	@Field(value="PACK_SIZE")
    private Double packSize;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PRICE")
    private Double price;

	@Field(value="QUAN_BASE")
    private Double quanBase;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdbase(){
        return this.pkPdbase;
    }
    public void setPkPdbase(String pkPdbase){
        this.pkPdbase = pkPdbase;
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
    public void setPkUnitPark(String pkUnitPack){
        this.pkUnitPack = pkUnitPack;
    }

    public Double getPackSize() {
		return packSize;
	}
	public void setPackSize(Double packSize) {
		this.packSize = packSize;
	}
	public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getQuanBase(){
        return this.quanBase;
    }
    public void setQuanBase(Double quanBase){
        this.quanBase = quanBase;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}