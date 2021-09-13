package com.zebone.nhis.common.module.pi;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_CATE - pi_cate 
 *
 * @since 2016-10-13 10:33:57
 */
@Table(value="PI_CATE")
public class PiCate extends BaseModule  {

	@PK
	@Field(value="PK_PICATE",id=KeyId.UUID)
    private String pkPicate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_HP")
    private String pkHp;
	
	@Field(value="FLAG_DEF")
	public String flagDef = "0";  // 0：  1：默认值

	@Field(value="FLAG_SPEC")
	public String flagSpec = "0";  //0默认值

    @Field(value="PK_PAYER")
    public String pkPayer;

    @Field(value="AMOUNT_DISC")
    public Double amountDisc;


    public String getFlagSpec() {
		return flagSpec;
	}
	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}
	public String getFlagDef() {
		return flagDef;
	}
	public String getPkPicate(){
        return this.pkPicate;
    }
    public void setPkPicate(String pkPicate){
        this.pkPicate = pkPicate;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }
    public void setFlagDef(String flagDef){
    	this.flagDef=flagDef;
    }

    public String getPkPayer() {
        return pkPayer;
    }

    public void setPkPayer(String pkPayer) {
        this.pkPayer = pkPayer;
    }

    public Double getAmountDisc() {
        return amountDisc;
    }

    public void setAmountDisc(Double amountDisc) {
        this.amountDisc = amountDisc;
    }
}