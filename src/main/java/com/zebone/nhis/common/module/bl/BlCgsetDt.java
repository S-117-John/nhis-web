package com.zebone.nhis.common.module.bl;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_CGSET_DT - BL_CGSET_DT 
 *
 * @since 2017-06-29 11:24:42
 */
@Table(value="BL_CGSET_DT")
public class BlCgsetDt extends BaseModule  {

	@PK
	@Field(value="PK_CGSETDT",id=KeyId.UUID)
    private String pkCgsetdt;

	@Field(value="PK_CGSET")
    private String pkCgset;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="QUAN")
    private BigDecimal quan;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	@Field(value="sortno")
	private String sortno;
	
	@Field(value="EU_ITEMTYPE")
	private String euItemtype;


    @Field(value="dosage")
    private Double dosage;

    @Field(value="unit_dos")
    private String unitDos;

    @Field(value="name_supply")
    private String nameSupply;

    @Field(value="name_freq")
    private String nameFreq;


    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public String getUnitDos() {
        return unitDos;
    }

    public void setUnitDos(String unitDos) {
        this.unitDos = unitDos;
    }

    public String getNameSupply() {
        return nameSupply;
    }

    public void setNameSupply(String nameSupply) {
        this.nameSupply = nameSupply;
    }

    public String getNameFreq() {
        return nameFreq;
    }

    public void setNameFreq(String nameFreq) {
        this.nameFreq = nameFreq;
    }


    public String getEuItemtype() {
		return euItemtype;
	}
	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}
	public String getPkCgsetdt(){
        return this.pkCgsetdt;
    }
    public void setPkCgsetdt(String pkCgsetdt){
        this.pkCgsetdt = pkCgsetdt;
    }

    public String getPkCgset(){
        return this.pkCgset;
    }
    public void setPkCgset(String pkCgset){
        this.pkCgset = pkCgset;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public BigDecimal getQuan(){
        return this.quan;
    }
    public void setQuan(BigDecimal quan){
        this.quan = quan;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getSortno() {
		return sortno;
	}
	public void setSortno(String sortno) {
		this.sortno = sortno;
	}
	
    
}