package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_FILTER_ITEM")
public class BdFilterItem extends BaseModule  {

	@PK
	@Field(value="PK_FILTERITEM",id=KeyId.UUID)
    private String pkFilteritem;

	@Field(value="CODE_ITEM")
    private String codeItem;

	@Field(value="NAME_ITEM")
    private String nameItem;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="DT_FILTERTYPE")
    private String dtFiltertype;

	@Field(value="DESC_FILTER")
    private String descFilter;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkFilteritem(){
        return this.pkFilteritem;
    }
    public void setPkFilteritem(String pkFilteritem){
        this.pkFilteritem = pkFilteritem;
    }

    public String getCodeItem(){
        return this.codeItem;
    }
    public void setCodeItem(String codeItem){
        this.codeItem = codeItem;
    }

    public String getNameItem(){
        return this.nameItem;
    }
    public void setNameItem(String nameItem){
        this.nameItem = nameItem;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getDtFiltertype(){
        return this.dtFiltertype;
    }
    public void setDtFiltertype(String dtFiltertype){
        this.dtFiltertype = dtFiltertype;
    }

    public String getDescFilter(){
        return this.descFilter;
    }
    public void setDescFilter(String descFilter){
        this.descFilter = descFilter;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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
