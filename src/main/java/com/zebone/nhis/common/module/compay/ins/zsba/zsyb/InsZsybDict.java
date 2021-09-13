package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_dict - 外部医保-医保数据字典 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_DICT")
public class InsZsybDict extends BaseModule  {

	@PK
	@Field(value="PK_INSDICT",id=KeyId.UUID)
    private String pkInsdict;

	@Field(value="PK_INSDICTTYPE")
    private String pkInsdicttype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInsdict(){
        return this.pkInsdict;
    }
    public void setPkInsdict(String pkInsdict){
        this.pkInsdict = pkInsdict;
    }

    public String getPkInsdicttype(){
        return this.pkInsdicttype;
    }
    public void setPkInsdicttype(String pkInsdicttype){
        this.pkInsdicttype = pkInsdicttype;
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

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
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