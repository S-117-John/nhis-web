package com.zebone.nhis.common.module.bl;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(value = "BL_DEPO_REMOVE")
public class BlDepoRemove {
    @PK
    @Field(value = "PK_REMOVE", id = Field.KeyId.UUID)
    private String pkRemove;
    @Field(value = "PK_DEPO")
    private String pkDepo;
    @Field(value = "EU_DIRECT")
    private String euDirect;
    @Field(value = "DT_PAYMODE")
    private String dtPaymode;
    @Field(value = "AMOUNT_REMOVE")
    private BigDecimal amountRemove;
    @Field(value = "PK_EMP_REMOVE")
    private String pkEmpRemove;
    @Field(value = "DATE_REMOVE")
    private Date dateRemove;
    @Field(value = "NOTE_REMOVE")
    private String noteRemove;
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;
    @Field(value = "create_time", date = Field.FieldType.INSERT)
    private Date createTime;
    @Field(userfield = "pkEmp", value = "MODIFIER", userfieldscop = Field.FieldType.ALL)
    private String modifier;
    @Field(value = "modity_time", date = Field.FieldType.UPDATE)
    private Date modityTime;
    @Field(date = Field.FieldType.ALL)
    private Date ts;
    @Field(value = "del_flag")
    private String delFlag = "0";  // 0未删除  1：删除

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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
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

    public String getPkRemove() {
        return pkRemove;
    }

    public void setPkRemove(String pkRemove) {
        this.pkRemove = pkRemove;
    }

    public String getPkDepo() {
        return pkDepo;
    }

    public void setPkDepo(String pkDepo) {
        this.pkDepo = pkDepo;
    }

    public String getEuDirect() {
        return euDirect;
    }

    public void setEuDirect(String euDirect) {
        this.euDirect = euDirect;
    }

    public String getDtPaymode() {
        return dtPaymode;
    }

    public void setDtPaymode(String dtPaymode) {
        this.dtPaymode = dtPaymode;
    }

    public BigDecimal getAmountRemove() {
        return amountRemove;
    }

    public void setAmountRemove(BigDecimal amountRemove) {
        this.amountRemove = amountRemove;
    }

    public String getPkEmpRemove() {
        return pkEmpRemove;
    }

    public void setPkEmpRemove(String pkEmpRemove) {
        this.pkEmpRemove = pkEmpRemove;
    }

    public Date getDateRemove() {
        return dateRemove;
    }

    public void setDateRemove(Date dateRemove) {
        this.dateRemove = dateRemove;
    }

    public String getNoteRemove() {
        return noteRemove;
    }

    public void setNoteRemove(String noteRemove) {
        this.noteRemove = noteRemove;
    }
}
