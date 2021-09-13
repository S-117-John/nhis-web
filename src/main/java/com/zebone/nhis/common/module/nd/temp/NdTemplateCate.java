package com.zebone.nhis.common.module.nd.temp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_TEMPLATE_CATE 
 *
 * @since 2017-06-07 10:49:27
 */
@Table(value="ND_TEMPLATE_CATE")
public class NdTemplateCate extends BaseModule  {

	@PK
	@Field(value="PK_TEMPLATECATE",id=KeyId.UUID)
    private String pkTemplatecate;

	@Field(value="CODE_CATE")
    private String codeCate;

	@Field(value="NAME_CATE")
    private String nameCate;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_FATHER")
    private String pkFather;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTemplatecate(){
        return this.pkTemplatecate;
    }
    public void setPkTemplatecate(String pkTemplatecate){
        this.pkTemplatecate = pkTemplatecate;
    }

    public String getCodeCate(){
        return this.codeCate;
    }
    public void setCodeCate(String codeCate){
        this.codeCate = codeCate;
    }

    public String getNameCate(){
        return this.nameCate;
    }
    public void setNameCate(String nameCate){
        this.nameCate = nameCate;
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

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}