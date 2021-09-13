package com.zebone.nhis.common.module.nd.temp;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_TEMPLATE 
 *
 * @since 2017-06-07 10:49:18
 */
@Table(value="ND_TEMPLATE")
public class NdTemplate extends BaseModule  {

	@PK
	@Field(value="PK_TEMPLATE",id=KeyId.UUID)
    private String pkTemplate;

	@Field(value="CODE_TEMP")
    private String codeTemp;

	@Field(value="NAME_TEMP")
    private String nameTemp;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="SORTNO")
    private BigDecimal sortno;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="PK_TEMPLATECATE")
    private String pkTemplatecate;

	@Field(value="CONTENT")
    private String content;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTemplate(){
        return this.pkTemplate;
    }
    public void setPkTemplate(String pkTemplate){
        this.pkTemplate = pkTemplate;
    }

    public String getCodeTemp(){
        return this.codeTemp;
    }
    public void setCodeTemp(String codeTemp){
        this.codeTemp = codeTemp;
    }

    public String getNameTemp(){
        return this.nameTemp;
    }
    public void setNameTemp(String nameTemp){
        this.nameTemp = nameTemp;
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

    public BigDecimal getSortno(){
        return this.sortno;
    }
    public void setSortno(BigDecimal sortno){
        this.sortno = sortno;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getPkTemplatecate(){
        return this.pkTemplatecate;
    }
    public void setPkTemplatecate(String pkTemplatecate){
        this.pkTemplatecate = pkTemplatecate;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
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