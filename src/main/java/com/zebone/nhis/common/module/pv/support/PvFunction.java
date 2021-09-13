package com.zebone.nhis.common.module.pv.support;

/**
 * @Classname PvFunction
 * @Description 患者功能科室就诊表
 * @Date 2020-12-20 18:33
 * @Created by wuqiang
 */

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * Table: PV_FUNCTION
 *
 * @since 2020-12-20 06:30:40
 */
@Table(value="PV_FUNCTION")
public class PvFunction  {

    @PK
    @Field(value="PK_FUN",id= Field.KeyId.UUID)
    private String pkFun;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_DEPT")
    private String pkDept;

    @Field(value="DATE_FUN")
    private Date dateFun;
    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.INSERT)
    private String creator;

    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT)
    private Date createTime;

    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.ALL)
    private String modifier;

    @Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="DEL_FLAG")
    private String delFlag;

    @Field(date= Field.FieldType.ALL)
    private Date ts;


    public String getPkFun(){
        return this.pkFun;
    }
    public void setPkFun(String pkFun){
        this.pkFun = pkFun;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getDateFun(){
        return this.dateFun;
    }
    public void setDateFun(Date dateFun){
        this.dateFun = dateFun;
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