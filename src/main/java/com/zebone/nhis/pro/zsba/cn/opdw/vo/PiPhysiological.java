package com.zebone.nhis.pro.zsba.cn.opdw.vo;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PI_PHYSIOLOGICAL
 *
 * @since 2021-04-23 10:34:21
 */
@Table(value="PI_PHYSIOLOGICAL")
public class PiPhysiological {
    @PK
    @Field(value="PK_PIPHYSI",id=KeyId.UUID)
    private String pkPiphysi;

    @Field(value="PK_PI")
    private String pkPi;

    @Field(value="DT_PHYSI")
    private String dtPhysi;

    @Field(value="DESC_PHYSI")
    private String descPhysi;

    @Field(value="DATE_BEGIN")
    private Date dateBegin;

    @Field(value="DATE_END")
    private Date dateEnd;

    @Field(value="DATE_REC")
    private Date dateRec;

    @Field(value="PK_EMP_REC")
    private String pkEmpRec;

    @Field(value="NAME_EMP_REC")
    private String nameEmpRec;

    @Field(value="NOTE")
    private String note;

    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.INSERT)
    private String creator;

    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT)
    private Date createTime;

    @Field(userfield="pkEmp",userfieldscop=Field.FieldType.ALL)
    private String modifier;

    @Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="DEL_FLAG")
    private String delFlag;

    @Field(date=Field.FieldType.ALL)
    private Date ts;


    public String getPkPiphysi(){
        return this.pkPiphysi;
    }
    public void setPkPiphysi(String pkPiphysi){
        this.pkPiphysi = pkPiphysi;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getDtPhysi(){
        return this.dtPhysi;
    }
    public void setDtPhysi(String dtPhysi){
        this.dtPhysi = dtPhysi;
    }

    public String getDescPhysi(){
        return this.descPhysi;
    }
    public void setDescPhysi(String descPhysi){
        this.descPhysi = descPhysi;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public Date getDateRec(){
        return this.dateRec;
    }
    public void setDateRec(Date dateRec){
        this.dateRec = dateRec;
    }

    public String getPkEmpRec(){
        return this.pkEmpRec;
    }
    public void setPkEmpRec(String pkEmpRec){
        this.pkEmpRec = pkEmpRec;
    }

    public String getNameEmpRec(){
        return this.nameEmpRec;
    }
    public void setNameEmpRec(String nameEmpRec){
        this.nameEmpRec = nameEmpRec;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
