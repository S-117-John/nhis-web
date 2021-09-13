package com.zebone.nhis.common.module.bl;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_ST_INV - bl_st_inv 
 *
 * @since 2016-10-11 09:06:42
 */
@Table(value="BL_ST_INV")
public class BlStInv extends BaseModule  {

	@PK
	@Field(value="PK_STINV",id=KeyId.UUID)
    private String pkStinv;

	@Field(value="PK_INVOICE")
    private String pkInvoice;

	@Field(value="PK_SETTLE")
    private String pkSettle;


    public String getPkStinv(){
        return this.pkStinv;
    }
    public void setPkStinv(String pkStinv){
        this.pkStinv = pkStinv;
    }

    public String getPkInvoice(){
        return this.pkInvoice;
    }
    public void setPkInvoice(String pkInvoice){
        this.pkInvoice = pkInvoice;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }
}