package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_CATE_ITEM  - bd_cate_item 
 *
 * @since 2016-09-19 10:02:04
 */
@Table(value="BD_CATE_ITEM")
public class BdCateItem extends BaseModule  {

	@PK
	@Field(value="PK_CATEITEM",id=KeyId.UUID)
    private String pkCateitem;

	@Field(value="PK_CATECONT")
    private String pkCatecont;

	@Field(value="PK_ITEMCATE")
    private String pkItemcate;


    public String getPkCateitem(){
        return this.pkCateitem;
    }
    public void setPkCateitem(String pkCateitem){
        this.pkCateitem = pkCateitem;
    }

    public String getPkCatecont(){
        return this.pkCatecont;
    }
    public void setPkCatecont(String pkCatecont){
        this.pkCatecont = pkCatecont;
    }

    public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

}