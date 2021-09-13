package com.zebone.nhis.common.module.base.bd.res;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEFDEPT_MAP 
 *
 * @since 2018-12-11 10:37:15
 */
@Table(value="BD_DEFDEPT_MAP")
public class BdDefdeptMap extends BaseModule  {

	@PK
	@Field(value="PK_DEFDEPTMAP",id=KeyId.UUID)
    private String pkDefdeptmap;

	@Field(value="PK_DEFDEPT")
    private String pkDefdept;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkDefdeptmap(){
        return this.pkDefdeptmap;
    }
    public void setPkDefdeptmap(String pkDefdeptmap){
        this.pkDefdeptmap = pkDefdeptmap;
    }

    public String getPkDefdept(){
        return this.pkDefdept;
    }
    public void setPkDefdept(String pkDefdept){
        this.pkDefdept = pkDefdept;
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

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}