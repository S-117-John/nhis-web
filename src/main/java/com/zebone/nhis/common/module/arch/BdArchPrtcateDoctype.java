package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ARCH_PRTCATE_DOCTYPE 
 *
 * @since 2017-04-25 03:33:03
 */
@Table(value="BD_ARCH_PRTCATE_DOCTYPE")
public class BdArchPrtcateDoctype   {

	@PK
	@Field(value="PK_PRTDOC",id=KeyId.UUID)
    private String pkPrtdoc;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PRTCATE")
    private String pkPrtcate;

	@Field(value="PK_DOCTYPE")
    private String pkDoctype;

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


    public String getPkPrtdoc(){
        return this.pkPrtdoc;
    }
    public void setPkPrtdoc(String pkPrtdoc){
        this.pkPrtdoc = pkPrtdoc;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPrtcate(){
        return this.pkPrtcate;
    }
    public void setPkPrtcate(String pkPrtcate){
        this.pkPrtcate = pkPrtcate;
    }

    public String getPkDoctype(){
        return this.pkDoctype;
    }
    public void setPkDoctype(String pkDoctype){
        this.pkDoctype = pkDoctype;
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