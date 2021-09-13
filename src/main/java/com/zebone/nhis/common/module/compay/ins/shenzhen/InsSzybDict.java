package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_DICT  - ins_szyb_dict 
 *
 * @since 2020-01-16 07:55:18
 */
@Table(value="INS_SZYB_DICT")
public class InsSzybDict extends BaseModule  {

	@PK
	@Field(value="PK_INSDICT",id=KeyId.UUID)
    private String pkInsdict;

    /** EU_HPDICTTYPE - 01=深圳医保，02=异地医保 */
	@Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;

    /** CODE_TYPE - 字典类型 */
	@Field(value="CODE_TYPE")
    private String codeType;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="NOTE")
    private String note;

    /** FLAG_CHD - 0 父记录，1子记录 */
	@Field(value="FLAG_CHD")
    private String flagChd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODIFIER_TIME")
    private Date modifierTime;

	@Field(value="STOP_FLAG")
    private String stopFlag;


    public String getPkInsdict(){
        return this.pkInsdict;
    }
    public void setPkInsdict(String pkInsdict){
        this.pkInsdict = pkInsdict;
    }

    public String getEuHpdicttype(){
        return this.euHpdicttype;
    }
    public void setEuHpdicttype(String euHpdicttype){
        this.euHpdicttype = euHpdicttype;
    }

    public String getCodeType(){
        return this.codeType;
    }
    public void setCodeType(String codeType){
        this.codeType = codeType;
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

    public String getFlagChd(){
        return this.flagChd;
    }
    public void setFlagChd(String flagChd){
        this.flagChd = flagChd;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifierTime(){
        return this.modifierTime;
    }
    public void setModifierTime(Date modifierTime){
        this.modifierTime = modifierTime;
    }

    public String getStopFlag(){
        return this.stopFlag;
    }
    public void setStopFlag(String stopFlag){
        this.stopFlag = stopFlag;
    }
}