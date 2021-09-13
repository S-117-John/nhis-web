package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_OPT_DAY 
 *
 * @since 2017-06-27 09:26:22
 */
@Table(value="INS_OPT_DAY")
public class InsOptDay extends BaseModule  {

	@PK
	@Field(value="PK_OPTDAY",id=KeyId.UUID)
    private String pkOptday;

	@Field(value="PK_INSPV")
    private String pkInspv;

	@Field(value="CODE_HPPV")
    private String codeHppv;

	@Field(value="DICT_ICD10")
    private String dictIcd10;

	@Field(value="NAME_ICD10")
    private String nameIcd10;

	@Field(value="DICT_TREATWAY")
    private String dictTreatway;

	@Field(value="NAME_TREATWAY")
    private String nameTreatway;

	@Field(value="DATE_OPT")
    private Date dateOpt;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOptday(){
        return this.pkOptday;
    }
    public void setPkOptday(String pkOptday){
        this.pkOptday = pkOptday;
    }

    public String getPkInspv(){
        return this.pkInspv;
    }
    public void setPkInspv(String pkInspv){
        this.pkInspv = pkInspv;
    }

    public String getCodeHppv(){
        return this.codeHppv;
    }
    public void setCodeHppv(String codeHppv){
        this.codeHppv = codeHppv;
    }

    public String getDictIcd10() {
		return dictIcd10;
	}
	public void setDictIcd10(String dictIcd10) {
		this.dictIcd10 = dictIcd10;
	}
	public String getDictTreatway() {
		return dictTreatway;
	}
	public void setDictTreatway(String dictTreatway) {
		this.dictTreatway = dictTreatway;
	}
	public String getNameTreatway(){
        return this.nameTreatway;
    }
    public void setNameTreatway(String nameTreatway){
        this.nameTreatway = nameTreatway;
    }
    public Date getDateOpt() {
		return dateOpt;
	}
	public void setDateOpt(Date dateOpt) {
		this.dateOpt = dateOpt;
	}
	public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getNameIcd10() {
		return nameIcd10;
	}
	public void setNameIcd10(String nameIcd10) {
		this.nameIcd10 = nameIcd10;
	}
	/**
	 * 医保就在类别
	 */
    private String dictPvtype;


	public String getDictPvtype() {
		return dictPvtype;
	}
	public void setDictPvtype(String dictPvtype) {
		this.dictPvtype = dictPvtype;
	}
}