package com.zebone.nhis.base.ou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: BD_OU_ORG_AREA 
 *
 * @since 2019-08-21 06:05:56
 */
@Table(value="BD_OU_ORG_AREA")
public class BdOuOrgArea extends BaseModule  {

	@PK
	@Field(value="PK_ORGAREA",id=KeyId.UUID)
    private String pkOrgarea;

	@Field(value="CODE_AREA")
    private String codeArea;

	@Field(value="NAME_AREA")
    private String nameArea;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    /** DT_HOSPTYPE - 医院类型 */
	@Field(value="DT_HOSPTYPE")
    private String dtHosptype;

    /** DT_GRADE - 医院等级 */
	@Field(value="DT_GRADE")
    private String dtGrade;

    /** CODE_HOSP - 组织机构编码 */
	@Field(value="CODE_HOSP")
    private String codeHosp;

    /** CODE_DIVISION - 行政区划编码 */
	@Field(value="CODE_DIVISION")
    private String codeDivision;

    /** ADDR - 机构地址 */
	@Field(value="ADDR")
    private String addr;

    /** BEDNUM - 核定床位 */
	@Field(value="BEDNUM")
    private Integer bednum;

    /** INTRO - 简介 */
	@Field(value="INTRO")
    private String intro;

    /** CODE_HPORG - 医保机构编码 */
	@Field(value="CODE_HPORG")
    private String codeHporg;

    /** PARA_AREA - 院区参数 */
	@Field(value="PARA_AREA")
    private String paraArea;


    public String getPkOrgarea(){
        return this.pkOrgarea;
    }
    public void setPkOrgarea(String pkOrgarea){
        this.pkOrgarea = pkOrgarea;
    }

    public String getCodeArea(){
        return this.codeArea;
    }
    public void setCodeArea(String codeArea){
        this.codeArea = codeArea;
    }

    public String getNameArea(){
        return this.nameArea;
    }
    public void setNameArea(String nameArea){
        this.nameArea = nameArea;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDtHosptype(){
        return this.dtHosptype;
    }
    public void setDtHosptype(String dtHosptype){
        this.dtHosptype = dtHosptype;
    }

    public String getDtGrade(){
        return this.dtGrade;
    }
    public void setDtGrade(String dtGrade){
        this.dtGrade = dtGrade;
    }

    public String getCodeHosp(){
        return this.codeHosp;
    }
    public void setCodeHosp(String codeHosp){
        this.codeHosp = codeHosp;
    }

    public String getCodeDivision(){
        return this.codeDivision;
    }
    public void setCodeDivision(String codeDivision){
        this.codeDivision = codeDivision;
    }

    public String getAddr(){
        return this.addr;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }

    public Integer getBednum(){
        return this.bednum;
    }
    public void setBednum(Integer bednum){
        this.bednum = bednum;
    }

    public String getIntro(){
        return this.intro;
    }
    public void setIntro(String intro){
        this.intro = intro;
    }

    public String getCodeHporg(){
        return this.codeHporg;
    }
    public void setCodeHporg(String codeHporg){
        this.codeHporg = codeHporg;
    }

    public String getParaArea(){
        return this.paraArea;
    }
    public void setParaArea(String paraArea){
        this.paraArea = paraArea;
    }
}