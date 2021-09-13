package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_SUPPLYER - bd_pd_supplyer 
 *
 * @since 2016-11-03 06:21:33
 */
@Table(value="BD_PD_SUPPLYER")
public class BdPdSupplyer extends BaseModule  {

	@PK
	@Field(value="PK_PDSUPPLYER",id=KeyId.UUID)
    private String pkPdsupplyer;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_SUPPLYER")
    private String pkSupplyer;

	@Field(value="CODE_AGREE")
    private String codeAgree;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PRICE")
    private Double price;


    public String getPkPdsupplyer(){
        return this.pkPdsupplyer;
    }
    public void setPkPdsupplyer(String pkPdsupplyer){
        this.pkPdsupplyer = pkPdsupplyer;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
    }

    public String getCodeAgree(){
        return this.codeAgree;
    }
    public void setCodeAgree(String codeAgree){
        this.codeAgree = codeAgree;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }
}