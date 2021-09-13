package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_INVCATE_ITEM  - bd_invcate_item 
 *
 * @since 2016-09-19 06:51:50
 */
@Table(value="BD_INVCATE_ITEM")
public class BdInvcateItem extends BaseModule  {

	@PK
	@Field(value="PK_INVCATEITEM",id=KeyId.UUID)
    private String pkInvcateitem;

	@Field(value="PK_INVCATE")
    private String pkInvcate;

	@Field(value="SORT_NO")
    private Long sortNo;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="NOTE")
    private String note;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;


    public String getPkInvcateitem(){
        return this.pkInvcateitem;
    }
    public void setPkInvcateitem(String pkInvcateitem){
        this.pkInvcateitem = pkInvcateitem;
    }

    public String getPkInvcate(){
        return this.pkInvcate;
    }
    public void setPkInvcate(String pkInvcate){
        this.pkInvcate = pkInvcate;
    }

    public Long getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Long sortNo){
        this.sortNo = sortNo;
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

}