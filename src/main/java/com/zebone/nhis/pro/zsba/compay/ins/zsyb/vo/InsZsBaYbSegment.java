package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_segment - 外部医保-医保结算返回段定义 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_SEGMENT")
public class InsZsBaYbSegment extends BaseModule  {

	@PK
	@Field(value="PK_INSSEGMENT",id=KeyId.UUID)
    private String pkInssegment;

	@Field(value="PK_HP")
    private String pkHp;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_SHOW")
    private String flagShow;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkInssegment(){
        return this.pkInssegment;
    }
    public void setPkInssegment(String pkInssegment){
        this.pkInssegment = pkInssegment;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
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

    public String getFlagShow(){
        return this.flagShow;
    }
    public void setFlagShow(String flagShow){
        this.flagShow = flagShow;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}