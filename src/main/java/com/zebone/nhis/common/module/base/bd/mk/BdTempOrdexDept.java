package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TEMP_ORDEX_DEPT 
 *
 * @since 2017-8-29 10:04:51
 */
@Table(value="BD_TEMP_ORDEX_DEPT")
public class BdTempOrdexDept extends BaseModule  {

	@PK
	@Field(value="PK_TEMPDEPT",id=KeyId.UUID)
    private String pkTempdept;

	@Field(value="PK_TEMPORDEX")
    private String pkTempordex;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTempdept(){
        return this.pkTempdept;
    }
    public void setPkTempdept(String pkTempdept){
        this.pkTempdept = pkTempdept;
    }

    public String getPkTempordex(){
        return this.pkTempordex;
    }
    public void setPkTempordex(String pkTempordex){
        this.pkTempordex = pkTempordex;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}