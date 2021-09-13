package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_CATE_DEF  - bd_cate_def 
 *
 * @since 2016-09-19 10:01:45
 */
@Table(value="BD_CATE_DEF")
public class BdCateDef extends BaseModule  {

	@PK
	@Field(value="PK_CATEDEF",id=KeyId.UUID)
    private String pkCatedef;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="NOTE")
    private String note;


    public String getPkCatedef(){
        return this.pkCatedef;
    }
    public void setPkCatedef(String pkCatedef){
        this.pkCatedef = pkCatedef;
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

}