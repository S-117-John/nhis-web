package com.zebone.nhis.common.module.base.ou;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_USER_USRGRP - bd_ou_user_usrgrp 
 *
 * @since 2016-12-16 04:24:09
 */
@Table(value="BD_OU_USER_USRGRP")
public class BdOuUserUsrgrp extends BaseModule  {

	@PK
	@Field(value="PK_USERUSRGRP",id=KeyId.UUID)
    private String pkUserusrgrp;

	@Field(value="PK_USER")
    private String pkUser;

	@Field(value="PK_ORG_USRGRP")
    private String pkOrgUsrgrp;

	@Field(value="PK_USRGRP")
    private String pkUsrgrp;

	@Field(value="NOTE")
    private String note;


    public String getPkUserusrgrp(){
        return this.pkUserusrgrp;
    }
    public void setPkUserusrgrp(String pkUserusrgrp){
        this.pkUserusrgrp = pkUserusrgrp;
    }

    public String getPkUser(){
        return this.pkUser;
    }
    public void setPkUser(String pkUser){
        this.pkUser = pkUser;
    }

    public String getPkOrgUsrgrp(){
        return this.pkOrgUsrgrp;
    }
    public void setPkOrgUsrgrp(String pkOrgUsrgrp){
        this.pkOrgUsrgrp = pkOrgUsrgrp;
    }

    public String getPkUsrgrp(){
        return this.pkUsrgrp;
    }
    public void setPkUsrgrp(String pkUsrgrp){
        this.pkUsrgrp = pkUsrgrp;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}