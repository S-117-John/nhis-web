package com.zebone.nhis.common.module.cms;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CMS_CONTCATE - cms_contcate 
 *
 * @since 2016-11-15 02:35:38
 */
@Table(value="CMS_CONTCATE")
public class CmsContcate extends BaseModule  {

	@PK
	@Field(value="PK_CMSCONTCATE",id=KeyId.UUID)
    private String pkCmscontcate;

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

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCmscontcate(){
        return this.pkCmscontcate;
    }
    public void setPkCmscontcate(String pkCmscontcate){
        this.pkCmscontcate = pkCmscontcate;
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

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}