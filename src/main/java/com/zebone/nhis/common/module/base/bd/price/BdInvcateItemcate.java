package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_INVCATE_ITEMCATE  - bd_invcate_itemcate 
 *
 * @since 2016-09-19 06:51:57
 */
@Table(value="BD_INVCATE_ITEMCATE")
public class BdInvcateItemcate extends BaseModule  {

	@PK
	@Field(value="PK_INVITEM",id=KeyId.UUID)
    private String pkInvitem;

	@Field(value="PK_INVCATEITEM")
    private String pkInvcateitem;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;


    public String getPkInvitem(){
        return this.pkInvitem;
    }
    public void setPkInvitem(String pkInvitem){
        this.pkInvitem = pkInvitem;
    }

    public String getPkInvcateitem(){
        return this.pkInvcateitem;
    }
    public void setPkInvcateitem(String pkInvcateitem){
        this.pkInvcateitem = pkInvcateitem;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

}