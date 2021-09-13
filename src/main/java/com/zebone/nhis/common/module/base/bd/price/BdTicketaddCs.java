package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TICKETADD_CS 
 *
 * @since 2018-07-14 03:40:14
 */
@Table(value="BD_TICKETADD_CS")
public class BdTicketaddCs extends BaseModule  {

	@PK
	@Field(value="PK_TICKETADDCS",id=KeyId.UUID)
    private String pkTicketaddcs;

	@Field(value="CODE_CS")
    private String codeCs;

	@Field(value="NAME_CS")
    private String nameCs;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    public String getPkTicketaddcs(){
        return this.pkTicketaddcs;
    }
    public void setPkTicketaddcs(String pkTicketaddcs){
        this.pkTicketaddcs = pkTicketaddcs;
    }

    public String getCodeCs(){
        return this.codeCs;
    }
    public void setCodeCs(String codeCs){
        this.codeCs = codeCs;
    }

    public String getNameCs(){
        return this.nameCs;
    }
    public void setNameCs(String nameCs){
        this.nameCs = nameCs;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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