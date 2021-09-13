package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_TOTALDIV  - bd_hp_totaldiv 
 *
 * @since 2016-09-27 02:49:58
 */
@Table(value="BD_HP_TOTALDIV")
public class BdHpTotaldiv extends BaseModule  {

	@PK
	@Field(value="PK_TOTALDIV",id=KeyId.UUID)
    private String pkTotaldiv;

	@Field(value="PK_HP")
    private String pkHp;

    /** EU_DIVIDE - 0按比例 1按金额 */
	@Field(value="EU_DIVIDE")
    private String euDivide;

	@Field(value="RATE")
    private Double rate;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="NOTE")
    private String note;


    public String getPkTotaldiv(){
        return this.pkTotaldiv;
    }
    public void setPkTotaldiv(String pkTotaldiv){
        this.pkTotaldiv = pkTotaldiv;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
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

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

}