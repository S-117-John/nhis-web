package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_ITEMCATEDIV  - bd_hp_itemcatediv 
 *
 * @since 2016-09-27 02:49:35
 */
@Table(value="BD_HP_ITEMCATEDIV")
public class BdHpItemcatediv extends BaseModule  {

	@PK
	@Field(value="PK_HPSRV",id=KeyId.UUID)
    private String pkHpsrv;

	@Field(value="PK_HP")
    private String pkHp;

    /** PK_ITEMCATE - 如果是药品的话，填写pk_pd */
	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

    /** EU_DIV - 0按比例 1按金额 */
	@Field(value="EU_DIV")
    private String euDiv;

	@Field(value="RATE")
    private Double rate;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;


    public String getPkHpsrv(){
        return this.pkHpsrv;
    }
    public void setPkHpsrv(String pkHpsrv){
        this.pkHpsrv = pkHpsrv;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    public String getEuDiv(){
        return this.euDiv;
    }
    public void setEuDiv(String euDiv){
        this.euDiv = euDiv;
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

}