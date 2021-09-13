package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_DIVCONFIG 
 *
 * @since 2018-08-11 09:03:21
 */
@Table(value="BD_HP_DIVCONFIG")
public class BdHpDivconfig extends BaseModule  {

	@PK
	@Field(value="PK_HPDIVCONFIG",id=KeyId.UUID)
    private String pkHpdivconfig;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_HPCGDIV")
    private String pkHpcgdiv;

	@Field(value="PK_HPSTDIV")
    private String pkHpstdiv;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkHpdivconfig(){
        return this.pkHpdivconfig;
    }
    public void setPkHpdivconfig(String pkHpdivconfig){
        this.pkHpdivconfig = pkHpdivconfig;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkHpcgdiv(){
        return this.pkHpcgdiv;
    }
    public void setPkHpcgdiv(String pkHpcgdiv){
        this.pkHpcgdiv = pkHpcgdiv;
    }

    public String getPkHpstdiv(){
        return this.pkHpstdiv;
    }
    public void setPkHpstdiv(String pkHpstdiv){
        this.pkHpstdiv = pkHpstdiv;
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