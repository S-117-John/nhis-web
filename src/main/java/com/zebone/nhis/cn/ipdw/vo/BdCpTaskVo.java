package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_TASK 
 *
 * @since 2019-05-13 10:22:43
 */
@Table(value="BD_CP_TASK")
public class BdCpTaskVo extends BaseModule  {

	@PK
	@Field(value="PK_CPTASK",id=KeyId.UUID)
    private String pkCptask;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NAME_TASK")
    private String nameTask;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="EU_REPTYPE")
    private String euReptype;

	@Field(value="DT_PHARM")
    private String dtPharm;

	@Field(value="PK_MENU")
    private String pkMenu;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCptask(){
        return this.pkCptask;
    }
    public void setPkCptask(String pkCptask){
        this.pkCptask = pkCptask;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getNameTask(){
        return this.nameTask;
    }
    public void setNameTask(String nameTask){
        this.nameTask = nameTask;
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

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
    }

    public String getEuReptype(){
        return this.euReptype;
    }
    public void setEuReptype(String euReptype){
        this.euReptype = euReptype;
    }

    public String getDtPharm(){
        return this.dtPharm;
    }
    public void setDtPharm(String dtPharm){
        this.dtPharm = dtPharm;
    }

    public String getPkMenu(){
        return this.pkMenu;
    }
    public void setPkMenu(String pkMenu){
        this.pkMenu = pkMenu;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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