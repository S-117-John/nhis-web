package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_DEPT 
 *
 * @since 2021-04-09 11:23:18
 */
@Table(value="BD_HP_DEPT")
public class BdHpDept extends BaseModule  {

	@PK
	@Field(value="PK_HPDEPT",id=KeyId.UUID)
    private String pkHpdept;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_DEPT_USE")
    private String pkDeptUse;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkHpdept(){
        return this.pkHpdept;
    }
    public void setPkHpdept(String pkHpdept){
        this.pkHpdept = pkHpdept;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkDeptUse(){
        return this.pkDeptUse;
    }
    public void setPkDeptUse(String pkDeptUse){
        this.pkDeptUse = pkDeptUse;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}