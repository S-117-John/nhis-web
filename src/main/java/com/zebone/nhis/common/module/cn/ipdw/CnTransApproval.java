package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * 手术大量输血申请表
 * leiminjian 20201019
 */
@Table(value="CN_TRANS_APPROVAL")
public class CnTransApproval extends BaseModule {
    @PK
    @Field(value="PK_APPROVAL",id= Field.KeyId.UUID)
    public String   pkApproval   ;
    @Field(value="PK_PV")
    public String   pkPv         ;
    @Field(value="PK_ORG")
    public String   pkOrg        ;
    @Field(value="PK_ORD")
    public String   pkOrd        ;
    @Field(value="QUAN_BT")
    public Double   quanBt       ;
    @Field(value="PK_UNIT_BT")
    public String   pkUnitBt     ;
    @Field(value="DATE_APPLY")
    public Date     dateApply    ;
    @Field(value="PK_EMP_HEAD")
    public String   pkEmpHead    ;
    @Field(value="NAME_EMP_HEAD")
    public String   nameEmpHead  ;
    @Field(value="DATE_CHK_HEAD")
    public Date dateChkHead  ;
    @Field(value="FLAG_CHK_HEAD")
    public String   flagChkHead  ;
    @Field(value="FLAG_CHK_TRANS")
    public String   flagChkTrans ;
    @Field(value="NOTE_TRANS")
    public String   noteTrans    ;
    @Field(value="PK_EMP_TRANS")
    public String   pkEmpTrans   ;
    @Field(value="NAME_EMP_TRANS")
    public String   nameEmpTrans ;
    @Field(value="DATE_CHK_TRANS")
    public Date     dateChkTrans ;
    @Field(value="NOTE_MM")
    public String   noteMm       ;
    @Field(value="FLAG_CHK_MM")
    public String   flagChkMm    ;
    @Field(value="PK_EMP_MM")
    public String   pkEmpMm      ;
    @Field(value="NAME_EMP_MM")
    public String   nameEmpMm    ;
    @Field(value="DATE_CHK_MM")
    public Date     dateChkMm    ;
    @Field(value="MODIFIER")
    public String   modifier     ;
    @Field(value="MODITY_TIME")
    public Date     modityTime  ;
    @Field(value="PK_DEPT")
    public String pkDept;

    public String getPkApproval() {
        return pkApproval;
    }

    public void setPkApproval(String pkApproval) {
        this.pkApproval = pkApproval;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    @Override
    public String getPkOrg() {
        return pkOrg;
    }

    @Override
    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public Double getQuanBt() {
        return quanBt;
    }

    public void setQuanBt(Double quanBt) {
        this.quanBt = quanBt;
    }

    public String getPkUnitBt() {
        return pkUnitBt;
    }

    public void setPkUnitBt(String pkUnitBt) {
        this.pkUnitBt = pkUnitBt;
    }

    public Date getDateApply() {
        return dateApply;
    }

    public void setDateApply(Date dateApply) {
        this.dateApply = dateApply;
    }

    public String getPkEmpHead() {
        return pkEmpHead;
    }

    public void setPkEmpHead(String pkEmpHead) {
        this.pkEmpHead = pkEmpHead;
    }

    public String getNameEmpHead() {
        return nameEmpHead;
    }

    public void setNameEmpHead(String nameEmpHead) {
        this.nameEmpHead = nameEmpHead;
    }

    public Date getDateChkHead() {
        return dateChkHead;
    }

    public void setDateChkHead(Date dateChkHead) {
        this.dateChkHead = dateChkHead;
    }

    public String getFlagChkHead() {
        return flagChkHead;
    }

    public void setFlagChkHead(String flagChkHead)
    {
        if(CommonUtils.isEmptyString(flagChkHead)) flagChkHead="0";
        this.flagChkHead = flagChkHead;
    }

    public String getFlagChkTrans() {
        return flagChkTrans;
    }

    public void setFlagChkTrans(String flagChkTrans) {
        if(CommonUtils.isEmptyString(flagChkTrans)) flagChkTrans="0";
        this.flagChkTrans = flagChkTrans;
    }

    public String getNoteTrans() {
        return noteTrans;
    }

    public void setNoteTrans(String noteTrans) {
        this.noteTrans = noteTrans;
    }

    public String getPkEmpTrans() {
        return pkEmpTrans;
    }

    public void setPkEmpTrans(String pkEmpTrans) {
        this.pkEmpTrans = pkEmpTrans;
    }

    public String getNameEmpTrans() {
        return nameEmpTrans;
    }

    public void setNameEmpTrans(String nameEmpTrans) {
        this.nameEmpTrans = nameEmpTrans;
    }

    public Date getDateChkTrans() {
        return dateChkTrans;
    }

    public void setDateChkTrans(Date dateChkTrans) {
        this.dateChkTrans = dateChkTrans;
    }

    public String getNoteMm() {
        return noteMm;
    }

    public void setNoteMm(String noteMm) {
        this.noteMm = noteMm;
    }

    public String getFlagChkMm() {
        return flagChkMm;
    }

    public void setFlagChkMm(String flagChkMm) {
        if(CommonUtils.isEmptyString(flagChkMm)) flagChkMm="0";
        this.flagChkMm = flagChkMm;
    }

    public String getPkEmpMm() {
        return pkEmpMm;
    }

    public void setPkEmpMm(String pkEmpMm) {
        this.pkEmpMm = pkEmpMm;
    }

    public String getNameEmpMm() {
        return nameEmpMm;
    }

    public void setNameEmpMm(String nameEmpMm) {
        this.nameEmpMm = nameEmpMm;
    }

    public Date getDateChkMm() {
        return dateChkMm;
    }

    public void setDateChkMm(Date dateChkMm) {
        this.dateChkMm = dateChkMm;
    }

    @Override
    public String getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModity_time() {
        return modityTime;
    }

    public void setModity_time(Date modity_time) {
        this.modityTime = modity_time;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }
}
