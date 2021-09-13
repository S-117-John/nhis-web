package com.zebone.nhis.common.module.base.bd.code;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_SYSPARAM  - bd_sysparam 
 *
 * @since 2016-10-08 04:45:28
 */
@Table(value="BD_SYSPARAM")
public class BdSysparam extends BaseModule  {

	@PK
	@Field(value="PK_SYSPARAM",id=KeyId.UUID)
    private String pkSysparam;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="VAL")
    private String val;

	@Field(value="DESC_PARAM")
    private String descParam;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_PARAMTEMP")
	private String pkParamtemp;

    public String getPkSysparam(){
        return this.pkSysparam;
    }
    public void setPkSysparam(String pkSysparam){
        this.pkSysparam = pkSysparam;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getVal(){
        return this.val;
    }
    public void setVal(String val){
        this.val = val;
    }

    public String getDescParam(){
        return this.descParam;
    }
    public void setDescParam(String descParam){
        this.descParam = descParam;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getPkParamtemp() {
		return pkParamtemp;
	}
	public void setPkParamtemp(String pkParamtemp) {
		this.pkParamtemp = pkParamtemp;
	}
}