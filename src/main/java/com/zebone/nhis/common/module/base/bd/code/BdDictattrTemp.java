package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DICTATTR_TEMP 
 *
 * @since 2018-08-01 05:50:04
 */
@Table(value="BD_DICTATTR_TEMP")
public class BdDictattrTemp extends BaseModule  {

	@PK
	@Field(value="PK_DICTATTRTEMP",id=KeyId.UUID)
    private String pkDictattrtemp;

	@Field(value="DT_DICTTYPE")
    private String dtDicttype;

	@Field(value="CODE_ATTR")
    private String codeAttr;

	@Field(value="NAME_ATTR")
    private String nameAttr;

	@Field(value="VAL_ATTR")
    private String valAttr;

	@Field(value="DESC_ATTR")
    private String descAttr;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	public String getPkDictattrtemp(){
        return this.pkDictattrtemp;
    }
    public void setPkDictattrtemp(String pkDictattrtemp){
        this.pkDictattrtemp = pkDictattrtemp;
    }

    public String getDtDicttype(){
        return this.dtDicttype;
    }
    public void setDtDicttype(String dtDicttype){
        this.dtDicttype = dtDicttype;
    }

    public String getCodeAttr(){
        return this.codeAttr;
    }
    public void setCodeAttr(String codeAttr){
        this.codeAttr = codeAttr;
    }

    public String getNameAttr(){
        return this.nameAttr;
    }
    public void setNameAttr(String nameAttr){
        this.nameAttr = nameAttr;
    }

    public String getValAttr(){
        return this.valAttr;
    }
    public void setValAttr(String valAttr){
        this.valAttr = valAttr;
    }

    public String getDescAttr(){
        return this.descAttr;
    }
    public void setDescAttr(String descAttr){
        this.descAttr = descAttr;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
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