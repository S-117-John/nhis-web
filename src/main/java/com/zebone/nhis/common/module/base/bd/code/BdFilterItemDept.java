package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_FILTER_ITEM_DEPT")
public class BdFilterItemDept extends BaseModule  {

	@PK
	@Field(value="PK_ITEMDEPT",id=KeyId.UUID)
    private String pkItemdept;

	@Field(value="PK_FILTERITEM")
    private String pkFilteritem;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkItemdept(){
        return this.pkItemdept;
    }
    public void setPkItemdept(String pkItemdept){
        this.pkItemdept = pkItemdept;
    }

    public String getPkFilteritem(){
        return this.pkFilteritem;
    }
    public void setPkFilteritem(String pkFilteritem){
        this.pkFilteritem = pkFilteritem;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
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
