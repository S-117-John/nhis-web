package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DEPT_BU  - bd_dept_bu 
 *
 * @since 2016-09-23 02:17:51
 */
@Table(value="BD_DEPT_BU")
public class BdDeptBu extends BaseModule  {

	@PK
	@Field(value="PK_DEPTBU",id=KeyId.UUID)
    private String pkDeptbu;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="DT_BUTYPE")
    private String dtButype;

	@Field(value="NOTE")
    private String note;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLAG_SYS")
    private String flagSys;

    public String getPkDeptbu(){
        return this.pkDeptbu;
    }
    public void setPkDeptbu(String pkDeptbu){
        this.pkDeptbu = pkDeptbu;
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

    public String getDtButype(){
        return this.dtButype;
    }
    public void setDtButype(String dtButype){
        this.dtButype = dtButype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getFlagSys(){
        return this.flagSys;
    }
    public void setFlagSys(String flagSys){
        this.flagSys = flagSys;
    }

}