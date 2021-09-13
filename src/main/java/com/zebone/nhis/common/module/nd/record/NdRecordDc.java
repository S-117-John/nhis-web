package com.zebone.nhis.common.module.nd.record;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD_DC 
 *
 * @since 2017-08-02 05:40:14
 */
@Table(value="ND_RECORD_DC")
public class NdRecordDc extends BaseModule  {

	@PK
	@Field(value="PK_RECORDDC",id=KeyId.UUID)
    private String pkRecorddc;

	@Field(value="PK_RECORD")
    private String pkRecord;

	@Field(value="COLNO")
    private String colno;

	@Field(value="COLNAME")
    private String colname;

	@Field(value="CODE_DE")
    private String codeDe;

	@Field(value="EU_CTRL_TYPE")
    private String euCtrlType;

	@Field(value="CODE_RANGE")
    private String codeRange;

	@Field(value="FLAG_HIDE")
    private String flagHide;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecorddc(){
        return this.pkRecorddc;
    }
    public void setPkRecorddc(String pkRecorddc){
        this.pkRecorddc = pkRecorddc;
    }

    public String getPkRecord(){
        return this.pkRecord;
    }
    public void setPkRecord(String pkRecord){
        this.pkRecord = pkRecord;
    }

    public String getColno(){
        return this.colno;
    }
    public void setColno(String colno){
        this.colno = colno;
    }

    public String getColname(){
        return this.colname;
    }
    public void setColname(String colname){
        this.colname = colname;
    }

    public String getCodeDe(){
        return this.codeDe;
    }
    public void setCodeDe(String codeDe){
        this.codeDe = codeDe;
    }

    public String getEuCtrlType(){
        return this.euCtrlType;
    }
    public void setEuCtrlType(String euCtrlType){
        this.euCtrlType = euCtrlType;
    }

    public String getCodeRange(){
        return this.codeRange;
    }
    public void setCodeRange(String codeRange){
        this.codeRange = codeRange;
    }

    public String getFlagHide(){
        return this.flagHide;
    }
    public void setFlagHide(String flagHide){
        this.flagHide = flagHide;
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