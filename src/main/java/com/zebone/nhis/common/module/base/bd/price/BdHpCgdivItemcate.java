package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_CGDIV_ITEMCATE 
 *
 * @since 2018-07-15 12:01:28
 */
@Table(value="BD_HP_CGDIV_ITEMCATE")
public class BdHpCgdivItemcate extends BaseModule  {

	@PK
	@Field(value="PK_HPCGDIVITEMCATE",id=KeyId.UUID)
    private String pkHpcgdivitemcate;

	@Field(value="PK_HPCGDIV")
    private String pkHpcgdiv;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

	@Field(value="EU_DIVIDE")
    private String euDivide;

	@Field(value="RATE")
    private Double rate;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value = "PK_PAYER")
    private String pkPayer;

	private String code;
	
	private String name;
	
	//判断是否编辑
	private String isEdit;
	
    public String getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPkHpcgdivitemcate(){
        return this.pkHpcgdivitemcate;
    }
    public void setPkHpcgdivitemcate(String pkHpcgdivitemcate){
        this.pkHpcgdivitemcate = pkHpcgdivitemcate;
    }

    public String getPkHpcgdiv(){
        return this.pkHpcgdiv;
    }
    public void setPkHpcgdiv(String pkHpcgdiv){
        this.pkHpcgdiv = pkHpcgdiv;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }

    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getPkPayer() {
        return pkPayer;
    }

    public void setPkPayer(String pkPayer) {
        this.pkPayer = pkPayer;
    }
}