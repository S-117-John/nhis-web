package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PDCATE 
 *
 * @since 2018-07-06 11:48:03
 */
@Table(value="BD_PDCATE")
public class BdPdcate extends BaseModule  {

	@PK
	@Field(value="PK_PDCATE",id=KeyId.UUID)
    private String pkPdcate;

	@Field(value="CODE_PDCATE")
    private String codePdcate;

	@Field(value="NAME_PDCATE")
    private String namePdcate;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_FATHER")
    private String pkFather;

	@Field(value="DT_PDTYPE")
    private String dtPdtype;

	@Field(value="NOTE")
    private String note;

    public String getPkPdcate(){
        return this.pkPdcate;
    }
    public void setPkPdcate(String pkPdcate){
        this.pkPdcate = pkPdcate;
    }

    public String getCodePdcate(){
        return this.codePdcate;
    }
    public void setCodePdcate(String codePdcate){
        this.codePdcate = codePdcate;
    }

    public String getNamePdcate(){
        return this.namePdcate;
    }
    public void setNamePdcate(String namePdcate){
        this.namePdcate = namePdcate;
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

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getDtPdtype(){
        return this.dtPdtype;
    }
    public void setDtPdtype(String dtPdtype){
        this.dtPdtype = dtPdtype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}