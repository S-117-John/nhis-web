package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: BD_ORD  - bd_ord 
 *
 * @since 2016-09-08 01:28:00
 */
@Table(value="BD_ORD")
public class BdOrd {

	@PK
	@Field(value="PK_ORD",id=KeyId.UUID)
    private String pkOrd;

	@Field(value="PK_ORDTYPE")
    private String pkOrdtype;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="CODE")
    private String code;

	
	@Field(value="NAME")
    private String name;

	@Field(value="NAME_PRT")
    private String namePrt;
	
	@Field(value="AGE_MIN")
	private Integer ageMin;
	
	@Field(value="AGE_MAX")
	private Integer ageMax;

	/**
	 * 规格
	 */
	@Field(value="SPEC")
    private String spec;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

    /** EU_EXCLUDE - 0不排 1全排 2组排 3单排 */
	@Field(value="EU_EXCLUDE")
    private String euExclude;

	@Field(value="FLAG_NS")
    private String flagNs;

	@Field(value="FLAG_DR")
    private String flagDr;
	
	/**儿科 */
	@Field(value="FLAG_PED")
	private String flagPed;
	
	/**
	 * 单位
	 */
	@Field(value="PK_UNIT")
	private String pkUnit;
	
	/**
	 * 单位标志
	 */
	@Field(value="FLAG_UNIT")
	private String flagUnit;
	
	/**
	 * 性别
	 */
	@Field(value="EU_SEX")
	private String euSex;
	/**
	 * 归档类型
	 */
	@Field(value="eu_archtype")
	private String euArchtype  ;
	
	/**
	 * 医嘱分类
	 */
	@Field(value="DT_ORDCATE")
	private String dtOrdcate;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="QUAN_DEF")
    private double quanDef;

	@Field(value="FLAG_IP")
    private String flagIp;

	@Field(value="FLAG_OP")
    private String flagOp;

	@Field(value="FLAG_ER")
    private String flagEr;


    public String getCodeExt() {
        return codeExt;
    }

    public void setCodeExt(String codeExt) {
        this.codeExt = codeExt;
    }

    @Field(value="CODE_EXT")
    private String codeExt;

    @Field(value="CODE_EXT2")
    private String codeExt2;

	@Field(value="FLAG_HM")
    private String flagHm;

	@Field(value="FLAG_PE")
    private String flagPe;

	@Field(value="FLAG_EMR")
    private String flagEmr;

	@Field(value="FLAG_CG")
    private String flagCg;

    /** FLAG_PD - 用于标识材料 */
	@Field(value="FLAG_PD")
    private String flagPd;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="DESC_ORD")
    private String descOrd;
	
	@Field(value="EXCEPT_ORD")
    private String exceptOrd;


	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

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
	
	/**
     *删除标志  
     */
	@Field(value="del_flag")
	public String delFlag = "0";  // 0未删除  1：删除
	
	public String pkItem;
	
	public String pkOrdorg;
	
	public String pkOrg;
	
	/**
	 * 禁止医生修改检查检验属性
	 */
	@Field(value="FLAG_NOC")
    private String flagNoc;

    @Field(value="EU_ORDTYPE")
    private String euOrdtype;

    public String getEuOrdtype() {
        return euOrdtype;
    }

    public void setEuOrdtype(String euOrdtype) {
        this.euOrdtype = euOrdtype;
    }

    public String getFlagNoc() {
		return flagNoc;
	}
	public void setFlagNoc(String flagNoc) {
		this.flagNoc = flagNoc;
	}
	public Integer getAgeMin() {
		return ageMin;
	}
	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}
	public Integer getAgeMax() {
		return ageMax;
	}
	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}
    public String getPkOrdorg() {
		return pkOrdorg;
	}
	public void setPkOrdorg(String pkOrdorg) {
		this.pkOrdorg = pkOrdorg;
	}
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getFlagPed() {
		return flagPed;
	}
	public void setFlagPed(String flagPed) {
		this.flagPed = flagPed;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getFlagUnit() {
		return flagUnit;
	}
	public void setFlagUnit(String flagUnit) {
		this.flagUnit = flagUnit;
	}
	public String getEuSex() {
		return euSex;
	}
	public void setEuSex(String euSex) {
		this.euSex = euSex;
	}
	public String getDtOrdcate() {
		return dtOrdcate;
	}
	public void setDtOrdcate(String dtOrdcate) {
		this.dtOrdcate = dtOrdcate;
	}
	public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getPkOrdtype(){
        return this.pkOrdtype;
    }
    public void setPkOrdtype(String pkOrdtype){
        this.pkOrdtype = pkOrdtype;
    }

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
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

    public String getNamePrt(){
        return this.namePrt;
    }
    public void setNamePrt(String namePrt){
        this.namePrt = namePrt;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
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

    public String getEuExclude(){
        return this.euExclude;
    }
    public void setEuExclude(String euExclude){
        this.euExclude = euExclude;
    }

    public String getFlagNs(){
        return this.flagNs;
    }
    public void setFlagNs(String flagNs){
        this.flagNs = flagNs;
    }

    public String getFlagDr(){
        return this.flagDr;
    }
    public void setFlagDr(String flagDr){
        this.flagDr = flagDr;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public double getQuanDef(){
        return this.quanDef;
    }
    public void setQuanDef(double quanDef){
        this.quanDef = quanDef;
    }

    public String getFlagIp(){
        return this.flagIp;
    }
    public void setFlagIp(String flagIp){
        this.flagIp = flagIp;
    }

    public String getFlagOp(){
        return this.flagOp;
    }
    public void setFlagOp(String flagOp){
        this.flagOp = flagOp;
    }

    public String getFlagEr(){
        return this.flagEr;
    }
    public void setFlagEr(String flagEr){
        this.flagEr = flagEr;
    }

    public String getFlagHm(){
        return this.flagHm;
    }
    public void setFlagHm(String flagHm){
        this.flagHm = flagHm;
    }

    public String getFlagPe(){
        return this.flagPe;
    }
    public void setFlagPe(String flagPe){
        this.flagPe = flagPe;
    }

    public String getFlagEmr(){
        return this.flagEmr;
    }
    public void setFlagEmr(String flagEmr){
        this.flagEmr = flagEmr;
    }

    public String getFlagCg(){
        return this.flagCg;
    }
    public void setFlagCg(String flagCg){
        this.flagCg = flagCg;
    }

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getDescOrd() {
		return descOrd;
	}
	public void setDescOrd(String descOrd) {
		this.descOrd = descOrd;
	}
	public String getExceptOrd() {
		return exceptOrd;
	}
	public void setExceptOrd(String exceptOrd) {
		this.exceptOrd = exceptOrd;
	}

	public String getEuArchtype() {
		return euArchtype;
	}

	public void setEuArchtype(String euArchtype) {
		this.euArchtype = euArchtype;
	}

    public String getCodeExt2() {
        return codeExt2;
    }

    public void setCodeExt2(String codeExt2) {
        this.codeExt2 = codeExt2;
    }

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
    
}