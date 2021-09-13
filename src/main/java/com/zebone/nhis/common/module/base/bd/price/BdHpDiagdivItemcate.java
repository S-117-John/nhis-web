package com.zebone.nhis.common.module.base.bd.price;


import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_DIAGDIV_ITEMCATE 
 *
 * @since 2018-04-08 11:27:31
 */
@Table(value="BD_HP_DIAGDIV_ITEMCATE")
public class BdHpDiagdivItemcate extends BaseModule  {

	@PK
	@Field(value="PK_DIAGITEMCATE",id=KeyId.UUID)
    private String pkDiagitemcate;

	@Field(value="PK_TOTALDIV")
    private String pkTotaldiv;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

	@Field(value="RATE")
    private BigDecimal rate;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkDiagitemcate(){
        return this.pkDiagitemcate;
    }
    public void setPkDiagitemcate(String pkDiagitemcate){
        this.pkDiagitemcate = pkDiagitemcate;
    }

    public String getPkTotaldiv(){
        return this.pkTotaldiv;
    }
    public void setPkTotaldiv(String pkTotaldiv){
        this.pkTotaldiv = pkTotaldiv;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    public BigDecimal getRate(){
        return this.rate;
    }
    public void setRate(BigDecimal rate){
        this.rate = rate;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}