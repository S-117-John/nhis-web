package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ARCH_PRTCATE 
 *
 * @since 2017-04-25 03:32:54
 */
@Table(value="BD_ARCH_PRTCATE")
public class BdArchPrtcate   {

	@PK
	@Field(value="PK_PRTCATE",id=KeyId.UUID)
    private String pkPrtcate;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="CODE_PRTCATE")
    private String codePrtcate;

	@Field(value="NAME_PRTCATE")
    private String namePrtcate;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="FLAG_DEL")
    private String flagDel;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPrtcate(){
        return this.pkPrtcate;
    }
    public void setPkPrtcate(String pkPrtcate){
        this.pkPrtcate = pkPrtcate;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getCodePrtcate(){
        return this.codePrtcate;
    }
    public void setCodePrtcate(String codePrtcate){
        this.codePrtcate = codePrtcate;
    }

    public String getNamePrtcate(){
        return this.namePrtcate;
    }
    public void setNamePrtcate(String namePrtcate){
        this.namePrtcate = namePrtcate;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getFlagDel(){
        return this.flagDel;
    }
    public void setFlagDel(String flagDel){
        this.flagDel = flagDel;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}