package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CHAP 
 *
 * @since 2018-07-14 03:15:49
 */
@Table(value="BD_CHAP")
public class BdChap extends BaseModule  {

	@PK
	@Field(value="PK_CHAP",id=KeyId.UUID)
    private String pkChap;

	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="DT_CHAPTYPE")
    private String dtChaptype;

	@Field(value="VAL_BEGIN")
    private String valBegin;

	@Field(value="VAL_END")
    private String valEnd;

	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="EU_CALCMODE")
    private String euCalcmode;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="RATE")
    private Double rate;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


	private String code;
	
	private String name;
	
	private String spec;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPkChap(){
        return this.pkChap;
    }
    public void setPkChap(String pkChap){
        this.pkChap = pkChap;
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

    public String getDtChaptype(){
        return this.dtChaptype;
    }
    public void setDtChaptype(String dtChaptype){
        this.dtChaptype = dtChaptype;
    }

    public String getValBegin(){
        return this.valBegin;
    }
    public void setValBegin(String valBegin){
        this.valBegin = valBegin;
    }

    public String getValEnd(){
        return this.valEnd;
    }
    public void setValEnd(String valEnd){
        this.valEnd = valEnd;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public String getEuCalcmode(){
        return this.euCalcmode;
    }
    public void setEuCalcmode(String euCalcmode){
        this.euCalcmode = euCalcmode;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getRate(){
        return this.rate;
    }
    public void setRate(Double rate){
        this.rate = rate;
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