package com.zebone.nhis.common.module.ma.self;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_SSS_OPER - 自助服务系统功能操作 
 *
 * @since 2016-10-13 02:38:15
 */
@Table(value="BD_OU_SSS_OPER")
public class BdOuSssOper extends BaseModule  {

	@PK
	@Field(value="PK_OPER",id=KeyId.UUID)
    private String pkOper;

	@Field(value="CODE_OPER")
    private String codeOper;

	@Field(value="NAME_OPER")
    private String nameOper;

	@Field(value="PK_SUBSYS")
    private String pkSubsys;

	@Field(value="PK_FATHER")
    private String pkFather;

    /** EU_BUSTYPE - 00：业务类，01：管理类 */
	@Field(value="EU_BUSTYPE")
    private String euBustype;

    /** EU_OPERTYPE - 1:B/S 
2:C/S */
	@Field(value="EU_OPERTYPE")
    private String euOpertype;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    /** EU_LEVEL - 1:一级
2:二级
3:三级
4:四级 */
	@Field(value="EU_LEVEL")
    private String euLevel;

	@Field(value="DESCR")
    private String descr;

	@Field(value="OPER_DLL")
    private String operDll;

	@Field(value="PARAM")
    private String param;

	@Field(value="FLAG_SHORTCUT")
    private String flagShortcut;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOper(){
        return this.pkOper;
    }
    public void setPkOper(String pkOper){
        this.pkOper = pkOper;
    }

    public String getCodeOper(){
        return this.codeOper;
    }
    public void setCodeOper(String codeOper){
        this.codeOper = codeOper;
    }

    public String getNameOper(){
        return this.nameOper;
    }
    public void setNameOper(String nameOper){
        this.nameOper = nameOper;
    }

    public String getPkSubsys(){
        return this.pkSubsys;
    }
    public void setPkSubsys(String pkSubsys){
        this.pkSubsys = pkSubsys;
    }

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getEuBustype(){
        return this.euBustype;
    }
    public void setEuBustype(String euBustype){
        this.euBustype = euBustype;
    }

    public String getEuOpertype(){
        return this.euOpertype;
    }
    public void setEuOpertype(String euOpertype){
        this.euOpertype = euOpertype;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getEuLevel(){
        return this.euLevel;
    }
    public void setEuLevel(String euLevel){
        this.euLevel = euLevel;
    }

    public String getDescr(){
        return this.descr;
    }
    public void setDescr(String descr){
        this.descr = descr;
    }

    public String getOperDll(){
        return this.operDll;
    }
    public void setOperDll(String operDll){
        this.operDll = operDll;
    }

    public String getParam(){
        return this.param;
    }
    public void setParam(String param){
        this.param = param;
    }

    public String getFlagShortcut(){
        return this.flagShortcut;
    }
    public void setFlagShortcut(String flagShortcut){
        this.flagShortcut = flagShortcut;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
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