package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_GU_OPER - bd_ou_gu_oper 
 *
 * @since 2016-11-17 03:02:10
 */
@Table(value="BD_OU_GU_OPER")
public class BdOuGuOper extends BaseModule  {

	@PK
	@Field(value="PK_GUOPER",id=KeyId.UUID)
    private String pkGuoper;

	@Field(value="PK_GROUPUSER")
    private String pkGroupuser;

	@Field(value="PK_OPER")
    private String pkOper;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_MENU")
    private String pkMenu;
	
    public String getPkGuoper(){
        return this.pkGuoper;
    }
    public void setPkGuoper(String pkGuoper){
        this.pkGuoper = pkGuoper;
    }

    public String getPkGroupuser(){
        return this.pkGroupuser;
    }
    public void setPkGroupuser(String pkGroupuser){
        this.pkGroupuser = pkGroupuser;
    }

    public String getPkOper(){
        return this.pkOper;
    }
    public void setPkOper(String pkOper){
        this.pkOper = pkOper;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getPkMenu() {
		return pkMenu;
	}
	public void setPkMenu(String pkMenu) {
		this.pkMenu = pkMenu;
	}
    
}