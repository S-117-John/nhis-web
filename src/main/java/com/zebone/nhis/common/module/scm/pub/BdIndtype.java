package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_INDTYPE 
 *
 * @since 2018-12-20 10:39:50
 */
@Table(value="BD_INDTYPE")
public class BdIndtype extends BaseModule  {

	@PK
	@Field(value="PK_INDTYPE",id=KeyId.UUID)
    private String pkIndtype;

	@Field(value="CODE_TYPE")
    private String codeType;

	@Field(value="NAME_TYPE")
    private String nameType;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkIndtype(){
        return this.pkIndtype;
    }
    public void setPkIndtype(String pkIndtype){
        this.pkIndtype = pkIndtype;
    }

    public String getCodeType(){
        return this.codeType;
    }
    public void setCodeType(String codeType){
        this.codeType = codeType;
    }

    public String getNameType(){
        return this.nameType;
    }
    public void setNameType(String nameType){
        this.nameType = nameType;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
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