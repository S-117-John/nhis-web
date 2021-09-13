package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_SECDIV  - bd_hp_secdiv 
 *
 * @since 2016-09-27 02:49:51
 */
@Table(value="BD_HP_SECDIV")
public class BdHpSecdiv extends BaseModule  {

	@PK
	@Field(value="PK_SECDIV",id=KeyId.UUID)
    private String pkSecdiv;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="AMT_MIN")
    private Double amtMin;

	@Field(value="AMT_MAX")
    private Double amtMax;

    /** EU_DIVIDE - 0按比例 1按金额 */
	@Field(value="EU_DIVIDE")
    private String euDivide;

	@Field(value="RATE")
    private Double rate;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="NOTE")
    private String note;


    public String getPkSecdiv(){
        return this.pkSecdiv;
    }
    public void setPkSecdiv(String pkSecdiv){
        this.pkSecdiv = pkSecdiv;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public Double getAmtMin(){
        return this.amtMin;
    }
    public void setAmtMin(Double amtMin){
        this.amtMin = amtMin;
    }

    public Double getAmtMax(){
        return this.amtMax;
    }
    public void setAmtMax(Double amtMax){
        this.amtMax = amtMax;
    }

    public String getEuDivide(){
        return this.euDivide;
    }
    public void setEuDivide(String euDivide){
        this.euDivide = euDivide;
    }

    public Double getRate(){
        return this.rate;
    }
    public void setRate(Double rate){
        this.rate = rate;
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

}