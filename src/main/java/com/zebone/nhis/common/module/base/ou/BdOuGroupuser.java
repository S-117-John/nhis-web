package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_GROUPUSER - bd_ou_groupuser 
 *
 * @since 2016-11-17 03:01:46
 */
@Table(value="BD_OU_GROUPUSER")
public class BdOuGroupuser extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_GROUPUSER",id=KeyId.UUID)
    private String pkGroupuser;

	@Field(value="CODE_USER")
    private String codeUser;

	@Field(value="NAME_USER")
    private String nameUser;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PWD")
    private String pwd;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkGroupuser(){
        return this.pkGroupuser;
    }
    public void setPkGroupuser(String pkGroupuser){
        this.pkGroupuser = pkGroupuser;
    }

    public String getCodeUser(){
        return this.codeUser;
    }
    public void setCodeUser(String codeUser){
        this.codeUser = codeUser;
    }

    public String getNameUser(){
        return this.nameUser;
    }
    public void setNameUser(String nameUser){
        this.nameUser = nameUser;
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

    public String getPwd(){
        return this.pwd;
    }
    public void setPwd(String pwd){
        this.pwd = pwd;
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