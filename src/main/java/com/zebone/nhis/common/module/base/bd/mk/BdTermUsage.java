package com.zebone.nhis.common.module.base.bd.mk;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 医嘱用法<不再使用>
 * Table: BD_TERM_USAGE  - bd_term_usage 
 *
 * @since 2016-08-29 10:49:06
 */
@Table(value="BD_TERM_USAGE")
public class BdTermUsage extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_USAGE",id=KeyId.UUID)
    private String pkUsage;

	/** 编码 */
	@Field(value="CODE")
    private String code;

	/** 名称 */
	@Field(value="NAME")
    private String name;

	/** 拼音码 */
	@Field(value="PY_CODE")
    private String pyCode;

	/** 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

	/** 上级编码 */
	@Field(value="PK_FATHER")
    private String pkFather;

    /** 对应执行卡类型 DT_EXCARDTYPE - 例如 1 护理卡 2 口服卡 3 注射卡 4 输液卡 5 饮食卡 99  其他卡 */
	@Field(value="DT_EXCARDTYPE")
    private String dtExcardtype;

	/** 配液标志 */
	@Field(value="FLAG_PIVAS")
    private String flagPivas;

	/** 试敏标志 */
	@Field(value="FLAG_ST")
    private String flagSt;

	/** 启用标志 */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	/** 备注 */
	@Field(value="NOTE")
    private String note;


    public String getPkUsage(){
        return this.pkUsage;
    }
    public void setPkUsage(String pkUsage){
        this.pkUsage = pkUsage;
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

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
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

    public String getDtExcardtype(){
        return this.dtExcardtype;
    }
    public void setDtExcardtype(String dtExcardtype){
        this.dtExcardtype = dtExcardtype;
    }

    public String getFlagPivas(){
        return this.flagPivas;
    }
    public void setFlagPivas(String flagPivas){
        this.flagPivas = flagPivas;
    }

    public String getFlagSt(){
        return this.flagSt;
    }
    public void setFlagSt(String flagSt){
        this.flagSt = flagSt;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
}