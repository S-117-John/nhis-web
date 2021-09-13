package com.zebone.nhis.common.module.ex.pivas.conf;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PIVAS_CATE - bd_pivas_cate 
 *
 * @since 2016-12-06 01:53:32
 */
@Table(value="BD_PIVAS_CATE")
public class BdPivasCate extends BaseModule  {

	@PK
	@Field(value="PK_PIVASCATE",id=KeyId.UUID)
    private String pkPivascate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_ITEM")
    private String pkItem;


    public String getPkPivascate(){
        return this.pkPivascate;
    }
    public void setPkPivascate(String pkPivascate){
        this.pkPivascate = pkPivascate;
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

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }
}