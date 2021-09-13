package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_AUDIT_ITEMCATE  - bd_audit_itemcate 
 *
 * @since 2016-09-09 02:00:45
 */
@Table(value="BD_AUDIT_ITEMCATE")
public class BdAuditItemcate extends BaseModule  {

	@PK
	@Field(value="PK_AUDITITEM",id=KeyId.UUID)
    private String pkAudititem;

	@Field(value="PK_AUDIT")
    private String pkAudit;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;

    public String getPkAudititem(){
        return this.pkAudititem;
    }
    public void setPkAudititem(String pkAudititem){
        this.pkAudititem = pkAudititem;
    }

    public String getPkAudit(){
        return this.pkAudit;
    }
    public void setPkAudit(String pkAudit){
        this.pkAudit = pkAudit;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }
}