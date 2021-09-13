package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_CGDIV_ITEM 
 *
 * @since 2018-07-15 12:00:19
 */
@Table(value="BD_HP_CGDIV_ITEM")
public class BdHpCgdivItem extends BaseModule  {

	@PK
	@Field(value="PK_HPCGDIVITEM",id=KeyId.UUID)
    private String pkHpcgdivitem;

	@Field(value="PK_HPCGDIV")
    private String pkHpcgdiv;

	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

	//@Field(value="CODE")
    private String itemcode;

	@Field(value="DT_HPTYPE")
    private String dtHptype;

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

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="CODE")
	private String code;
	
	/** CREATOR-创建人 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME-创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

    @Field(value = "PK_PAYER")
    private String pkPayer;

    @Field(value = "MAX_QUAN")
    private Double maxQuan;

	private String itemname;
	
	private String itemspec;
	
	private String spcode;
	
	//判断是否编辑
	private String isEdit;
	
    public String getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemspec() {
		return itemspec;
	}
	public void setItemspec(String itemspec) {
		this.itemspec = itemspec;
	}
	public String getPkHpcgdivitem(){
        return this.pkHpcgdivitem;
    }
    public void setPkHpcgdivitem(String pkHpcgdivitem){
        this.pkHpcgdivitem = pkHpcgdivitem;
    }

    public String getPkHpcgdiv(){
        return this.pkHpcgdiv;
    }
    public void setPkHpcgdiv(String pkHpcgdiv){
        this.pkHpcgdiv = pkHpcgdiv;
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

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    /*public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }*/

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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    public String getPkPayer() {
        return pkPayer;
    }

    public void setPkPayer(String pkPayer) {
        this.pkPayer = pkPayer;
    }

    public Double getMaxQuan() {
        return maxQuan;
    }

    public void setMaxQuan(Double maxQuan) {
        this.maxQuan = maxQuan;
    }
}