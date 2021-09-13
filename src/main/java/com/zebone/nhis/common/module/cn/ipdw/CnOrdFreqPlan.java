package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_ORD_FREQ_PLAN 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_ORD_FREQ_PLAN")
public class CnOrdFreqPlan   {

	@PK
	@Field(value="PK_FREQPLAN",id=KeyId.UUID)
    private String pkFreqplan;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="EU_WEEK")
    private String euWeek;

	@Field(value="TIME_EX")
    private Date timeEx;

	@Field(value="DESC_EXEC")
    private String descExec;

	@Field(value="QUAN_OCC")
    private Double quanOcc;

	@Field(value="PK_UNIT_OCC")
    private String pkUnitOcc;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkFreqplan(){
        return this.pkFreqplan;
    }
    public void setPkFreqplan(String pkFreqplan){
        this.pkFreqplan = pkFreqplan;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getEuWeek(){
        return this.euWeek;
    }
    public void setEuWeek(String euWeek){
        this.euWeek = euWeek;
    }

    public Date getTimeEx(){
        return this.timeEx;
    }
    public void setTimeEx(Date timeEx){
        this.timeEx = timeEx;
    }

    public String getDescExec(){
        return this.descExec;
    }
    public void setDescExec(String descExec){
        this.descExec = descExec;
    }

    public Double getQuanOcc(){
        return this.quanOcc;
    }
    public void setQuanOcc(Double quanOcc){
        this.quanOcc = quanOcc;
    }

    public String getPkUnitOcc(){
        return this.pkUnitOcc;
    }
    public void setPkUnitOcc(String pkUnitOcc){
        this.pkUnitOcc = pkUnitOcc;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
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

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}