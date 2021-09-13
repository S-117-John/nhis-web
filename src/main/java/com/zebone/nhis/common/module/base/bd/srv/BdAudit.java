package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_AUDIT  - bd_audit 
 *
 * @since 2016-09-09 01:59:54
 */
@Table(value="BD_AUDIT")
public class BdAudit extends BaseModule  {

	@PK
	@Field(value="PK_AUDIT",id=KeyId.UUID)
    private String pkAudit;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;



    public String getPkAudit(){
        return this.pkAudit;
    }
    public void setPkAudit(String pkAudit){
        this.pkAudit = pkAudit;
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

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
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

}