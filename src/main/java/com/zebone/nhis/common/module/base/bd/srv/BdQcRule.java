package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_RULE 
 *
 * @since 2019-03-18 04:26:52
 */
@Table(value="BD_QC_RULE")
public class BdQcRule extends BaseModule  {

	@PK
	@Field(value="PK_QCRULE",id=KeyId.UUID)
    private String pkQcrule;

	@Field(value="CODE_RULE")
    private String codeRule;

	@Field(value="NAME_RULE")
    private String nameRule;

	@Field(value="CNT_WAIT")
    private Integer cntWait;

	@Field(value="CNT_CONTE")
    private Integer cntConte;

	@Field(value="FLAG_APPT")
    private String flagAppt;

	@Field(value="FLAG_DISE")
    private String flagDise;

	@Field(value="FLAG_FUNC")
    private String flagFunc;

	@Field(value="FUNC")
    private String func;

	@Field(value="SORTNO_OVER")
    private Integer sortnoOver;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="CNT_WAIT2")
    private Integer cntWait2;


    public String getPkQcrule(){
        return this.pkQcrule;
    }
    public void setPkQcrule(String pkQcrule){
        this.pkQcrule = pkQcrule;
    }

    public String getCodeRule(){
        return this.codeRule;
    }
    public void setCodeRule(String codeRule){
        this.codeRule = codeRule;
    }

    public String getNameRule(){
        return this.nameRule;
    }
    public void setNameRule(String nameRule){
        this.nameRule = nameRule;
    }

    public Integer getCntWait(){
        return this.cntWait;
    }
    public void setCntWait(Integer cntWait){
        this.cntWait = cntWait;
    }

    public Integer getCntConte(){
        return this.cntConte;
    }
    public void setCntConte(Integer cntConte){
        this.cntConte = cntConte;
    }

    public String getFlagAppt(){
        return this.flagAppt;
    }
    public void setFlagAppt(String flagAppt){
        this.flagAppt = flagAppt;
    }

    public String getFlagDise(){
        return this.flagDise;
    }
    public void setFlagDise(String flagDise){
        this.flagDise = flagDise;
    }

    public String getFlagFunc(){
        return this.flagFunc;
    }
    public void setFlagFunc(String flagFunc){
        this.flagFunc = flagFunc;
    }

    public String getFunc(){
        return this.func;
    }
    public void setFunc(String func){
        this.func = func;
    }

    public Integer getSortnoOver(){
        return this.sortnoOver;
    }
    public void setSortnoOver(Integer sortnoOver){
        this.sortnoOver = sortnoOver;
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

    public Integer getCntWait2(){
        return this.cntWait2;
    }
    public void setCntWait2(Integer cntWait2){
        this.cntWait2 = cntWait2;
    }
}