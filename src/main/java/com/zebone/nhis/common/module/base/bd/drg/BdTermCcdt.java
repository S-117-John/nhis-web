package com.zebone.nhis.common.module.base.bd.drg;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;



/**
 * Table: BD_TERM_CCDT - CCDT字典 
 * @author ds
 * @since 2019-12-23 04:00:28
 */
@Table(value="BD_TERM_CCDT")
public class BdTermCcdt  {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5037414054687444279L;
	/** PK_CCDT - 主键 */
	@PK
	@Field(value="PK_CCDT")
    private String pkCcdt;
    /** CODE_CCDT - ccdt编码 */
	@Field(value="CODE_CCDT")
    private String codeCcdt;

    /** NAME_CCDT - ccdt名称 */
	@Field(value="NAME_CCDT")
    private String nameCcdt;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spCode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** DT_CCDTTYPE - 术语类型 */
	@Field(value="DT_CCDTTYPE")
    private String dtCcdttype;

    /** VERSION - 版本 */
	@Field(value="VERSION")
    private String version;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

    /** CONTENT - 内涵 */
	@Field(value="CONTENT")
    private String content;

    /** EU_SEX - 适用性别 */
	@Field(value="EU_SEX")
    private String euSex;

    /** FLAG_NEWBORN - 适用新生儿 */
	@Field(value="FLAG_NEWBORN")
    private String flagNewborn;

    /** AGE_MAX - 适用年龄上限 */
	@Field(value="AGE_MAX")
    private Integer ageMax;

    /** AGE_MIN - 适用年龄下限 */
	@Field(value="AGE_MIN")
    private Integer ageMin;

    /** EU_MAJ - 主诊断标识 */
	@Field(value="EU_MAJ")
    private String euMaj;

    /** FLAG_STOP - 停用标志 */
	@Field(value="FLAG_STOP")
    private String flagStop;

    /** CODE_ICD - 对应icd编码 */
	@Field(value="CODE_ICD")
    private String codeIcd;

    /** NAME_ICD - 对应icd名称 */
	@Field(value="NAME_ICD")
    private String nameIcd;
	
	/** FLAG_DEL - 删除标志 */
	@Field(value="FLAG_DEL")
    private String flagDel;
	
	/**
     * 机构主键
     */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    public String pkOrg;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;
	


	/**
     * 修改人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;
    

	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	public Date ts;
	

    public String getPkCcdt(){
        return this.pkCcdt;
    }
    public void setPkCcdt(String pkCcdt){
        this.pkCcdt = pkCcdt;
    }

    public String getCodeCcdt(){
        return this.codeCcdt;
    }
    public void setCodeCcdt(String codeCcdt){
        this.codeCcdt = codeCcdt;
    }

    public String getNameCcdt(){
        return this.nameCcdt;
    }
    public void setNameCcdt(String nameCcdt){
        this.nameCcdt = nameCcdt;
    }


    public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getSpCode() {
		return spCode;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getDtCcdttype(){
        return this.dtCcdttype;
    }
    public void setDtCcdttype(String dtCcdttype){
        this.dtCcdttype = dtCcdttype;
    }

    public String getVersion(){
        return this.version;
    }
    public void setVersion(String version){
        this.version = version;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getEuSex(){
        return this.euSex;
    }
    public void setEuSex(String euSex){
        this.euSex = euSex;
    }

    public String getFlagNewborn(){
        return this.flagNewborn;
    }
    public void setFlagNewborn(String flagNewborn){
        this.flagNewborn = flagNewborn;
    }

    public Integer getAgeMax(){
        return this.ageMax;
    }
    public void setAgeMax(Integer ageMax){
        this.ageMax = ageMax;
    }

    public Integer getAgeMin(){
        return this.ageMin;
    }
    public void setAgeMin(Integer ageMin){
        this.ageMin = ageMin;
    }

    public String getEuMaj(){
        return this.euMaj;
    }
    public void setEuMaj(String euMaj){
        this.euMaj = euMaj;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getNameIcd(){
        return this.nameIcd;
    }
    public void setNameIcd(String nameIcd){
        this.nameIcd = nameIcd;
    }
	public String getFlagDel() {
		return flagDel;
	}
	public void setFlagDel(String flagDel) {
		this.flagDel = flagDel;
	}
}