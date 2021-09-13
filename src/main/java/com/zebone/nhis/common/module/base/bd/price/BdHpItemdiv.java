package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_ITEMDIV  - bd_hp_itemdiv 
 *
 * @since 2016-09-27 02:49:45
 */
@Table(value="BD_HP_ITEMDIV")
public class BdHpItemdiv extends BaseModule  {

	@PK
	@Field(value="PK_HPITEM",id=KeyId.UUID)
    private String pkHpitem;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="FLAG_PD")
    private String flagPd;

    /** PK_ITEM - 如果是药品的话，填写pk_pd */
	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="CODE")
    private String code;

    /** DT_HPTYPE - 医保目录 0 甲类; 1 乙类; 2 丙类 */
	@Field(value="DT_HPTYPE")
    private String dtHptype;

    /** EU_DIVIDE - 0按比例 1按金额 */
	@Field(value="EU_DIVIDE")
    private String euDivide;

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


    public String getPkHpitem(){
        return this.pkHpitem;
    }
    public void setPkHpitem(String pkHpitem){
        this.pkHpitem = pkHpitem;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getDtHptype(){
        return this.dtHptype;
    }
    public void setDtHptype(String dtHptype){
        this.dtHptype = dtHptype;
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