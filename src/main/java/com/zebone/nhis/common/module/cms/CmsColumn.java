package com.zebone.nhis.common.module.cms;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CMS_COLUMN - cms_column 
 *
 * @since 2016-11-15 02:34:48
 */
@Table(value="CMS_COLUMN")
public class CmsColumn extends BaseModule  {

	@PK
	@Field(value="PK_CMSCOL",id=KeyId.UUID)
    private String pkCmscol;

	@Field(value="CODE_COL")
    private String codeCol;

	@Field(value="NAME_COL")
    private String nameCol;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCmscol(){
        return this.pkCmscol;
    }
    public void setPkCmscol(String pkCmscol){
        this.pkCmscol = pkCmscol;
    }

    public String getCodeCol(){
        return this.codeCol;
    }
    public void setCodeCol(String codeCol){
        this.codeCol = codeCol;
    }

    public String getNameCol(){
        return this.nameCol;
    }
    public void setNameCol(String nameCol){
        this.nameCol = nameCol;
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

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
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