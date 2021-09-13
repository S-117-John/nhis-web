package com.zebone.nhis.scm.pub.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_SERIALNO 
 *
 * @since 2020-02-12 02:14:32
 */
@Table(value="BD_SERIALNO")
public class BdSerialnoBase extends BaseModule  {

	@PK
	@Field(value="PK_SERIALNO",id=KeyId.UUID)
    private String pkSerialno;

	@Field(value="NAME_TB")
    private String nameTb;

	@Field(value="NAME_FD")
    private String nameFd;

	@Field(value="VAL_INIT")
    private BigDecimal valInit;

	@Field(value="VAL")
    private BigDecimal val;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value = "PREFIX")
	private String prefix;

	@Field(value = "LENGTH")
	private Double length;


    public String getPkSerialno(){
        return this.pkSerialno;
    }
    public void setPkSerialno(String pkSerialno){
        this.pkSerialno = pkSerialno;
    }

    public String getNameTb(){
        return this.nameTb;
    }
    public void setNameTb(String nameTb){
        this.nameTb = nameTb;
    }

    public String getNameFd(){
        return this.nameFd;
    }
    public void setNameFd(String nameFd){
        this.nameFd = nameFd;
    }

    public BigDecimal getValInit(){
        return this.valInit;
    }
    public void setValInit(BigDecimal valInit){
        this.valInit = valInit;
    }

    public BigDecimal getVal(){
        return this.val;
    }
    public void setVal(BigDecimal val){
        this.val = val;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}