package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_CATE_CONT  - bd_cate_cont 
 *
 * @since 2016-09-19 10:01:10
 */
@Table(value="BD_CATE_CONT")
public class BdCateCont extends BaseModule  {

	@PK
	@Field(value="PK_CATECONT",id=KeyId.UUID)
    private String pkCatecont;

	@Field(value="PK_CATEDEF")
    private String pkCatedef;

	@Field(value="SORTNO")
    private Long sortno;

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


    public String getPkCatecont(){
        return this.pkCatecont;
    }
    public void setPkCatecont(String pkCatecont){
        this.pkCatecont = pkCatecont;
    }

    public String getPkCatedef(){
        return this.pkCatedef;
    }
    public void setPkCatedef(String pkCatedef){
        this.pkCatedef = pkCatedef;
    }

    public Long getSortno(){
        return this.sortno;
    }
    public void setSortno(Long sortno){
        this.sortno = sortno;
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