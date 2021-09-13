package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TICKETADD_CSDT 
 *
 * @since 2018-07-14 03:40:31
 */
@Table(value="BD_TICKETADD_CSDT")
public class BdTicketaddCsdt extends BaseModule  {

	@PK
	@Field(value="PK_TICKETADDCSDT",id=KeyId.UUID)
    private String pkTicketaddcsdt;

	@Field(value="PK_TICKETADDCS")
    private String pkTicketaddcs;

	@Field(value="BEGINNO")
    private Integer beginno;

	@Field(value="ENDNO")
    private Integer endno;

	@Field(value="PK_ITEM")
    private String pkItem;

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
	
    private String price;
    
    private String status;


    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPkTicketaddcsdt(){
        return this.pkTicketaddcsdt;
    }
    public void setPkTicketaddcsdt(String pkTicketaddcsdt){
        this.pkTicketaddcsdt = pkTicketaddcsdt;
    }

    public String getPkTicketaddcs(){
        return this.pkTicketaddcs;
    }
    public void setPkTicketaddcs(String pkTicketaddcs){
        this.pkTicketaddcs = pkTicketaddcs;
    }

    public Integer getBeginno(){
        return this.beginno;
    }
    public void setBeginno(Integer beginno){
        this.beginno = beginno;
    }

    public Integer getEndno(){
        return this.endno;
    }
    public void setEndno(Integer endno){
        this.endno = endno;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
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
}