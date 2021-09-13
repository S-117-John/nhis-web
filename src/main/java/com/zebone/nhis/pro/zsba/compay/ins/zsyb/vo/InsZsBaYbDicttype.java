package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_dicttype - 外部医保-医保数据字典类别 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_DICTTYPE")
public class InsZsBaYbDicttype extends BaseModule  {

	@PK
	@Field(value="PK_INSDICTTYPE",id=KeyId.UUID)
    private String pkInsdicttype;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="CODE_TYPE")
    private String codeType;

	@Field(value="NAME_TYPE")
    private String nameType;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsdicttype(){
        return this.pkInsdicttype;
    }
    public void setPkInsdicttype(String pkInsdicttype){
        this.pkInsdicttype = pkInsdicttype;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getCodeType(){
        return this.codeType;
    }
    public void setCodeType(String codeType){
        this.codeType = codeType;
    }

    public String getNameType(){
        return this.nameType;
    }
    public void setNameType(String nameType){
        this.nameType = nameType;
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