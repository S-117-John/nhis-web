package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_ORG 
 *
 * @since 2018-08-03 11:02:35
 */
@Table(value="BD_HP_ORG")
public class BdHpOrg extends BaseModule  {

	@PK
	@Field(value="PK_HPORG",id=KeyId.UUID)
    private String pkHporg;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkHporg(){
        return this.pkHporg;
    }
    public void setPkHporg(String pkHporg){
        this.pkHporg = pkHporg;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}