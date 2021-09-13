package com.zebone.nhis.pro.zsba.ex.vo;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;

import java.util.Date;
import java.util.List;

/**
 * @Classname ExAssistOcc 医技业务处理批量记费接收实体
 * @Description TODO
 * @Date 2020-08-10 14:44
 * @Created by wuqiang
 */
public class ExAssistOccPar {
    private String nameEmpOrd;
    private String dtSex;

    private String pkDept;

    private String purpose;

    private String codeOrdtype;

    private String descBody;

    private String flagEmer;

    private String pkDeptNs;

    private String codeIp;

    private String pkDeptApp;

    private String pkExocc;

    private int recentApply;

    private String pkPv;

    private String dateStart;

    private String dmissDiag;

    private String euPvtype;

    private String risStatus;

    private String namePi;

    private String pkPi;

    private String pkDeptOcc;

    private String flagPrint2;

    private int quan;

    private String pkOrgOcc;

    private int cnt;

    private String codePv;

    private String codeApply;

    private int ordsn;

    private Date datePlan;

    private String pkCnord;

    private String nameOrd;

    private String pkOrdris;

    private int priceCg;

    private String euStatusOcc;

    private String bedNo;

    private String pkOrd;

    private int ordsnParent;
    /*医嘱类型*/
    private String euOrdtype;

    private String nowDept;
    private  Date Ts;
    /*记费明细*/
    private List<BlPubParamVo> blPubParamVos;

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCodeOrdtype() {
        return codeOrdtype;
    }

    public void setCodeOrdtype(String codeOrdtype) {
        this.codeOrdtype = codeOrdtype;
    }

    public String getDescBody() {
        return descBody;
    }

    public void setDescBody(String descBody) {
        this.descBody = descBody;
    }

    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public String getPkDeptNs() {
        return pkDeptNs;
    }

    public void setPkDeptNs(String pkDeptNs) {
        this.pkDeptNs = pkDeptNs;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getPkDeptApp() {
        return pkDeptApp;
    }

    public void setPkDeptApp(String pkDeptApp) {
        this.pkDeptApp = pkDeptApp;
    }

    public String getPkExocc() {
        return pkExocc;
    }

    public void setPkExocc(String pkExocc) {
        this.pkExocc = pkExocc;
    }

    public int getRecentApply() {
        return recentApply;
    }

    public void setRecentApply(int recentApply) {
        this.recentApply = recentApply;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDmissDiag() {
        return dmissDiag;
    }

    public void setDmissDiag(String dmissDiag) {
        this.dmissDiag = dmissDiag;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getRisStatus() {
        return risStatus;
    }

    public void setRisStatus(String risStatus) {
        this.risStatus = risStatus;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkDeptOcc() {
        return pkDeptOcc;
    }

    public void setPkDeptOcc(String pkDeptOcc) {
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getFlagPrint2() {
        return flagPrint2;
    }

    public void setFlagPrint2(String flagPrint2) {
        this.flagPrint2 = flagPrint2;
    }

    public int getQuan() {
        return quan;
    }

    public void setQuan(int quan) {
        this.quan = quan;
    }

    public String getPkOrgOcc() {
        return pkOrgOcc;
    }

    public void setPkOrgOcc(String pkOrgOcc) {
        this.pkOrgOcc = pkOrgOcc;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodeApply() {
        return codeApply;
    }

    public void setCodeApply(String codeApply) {
        this.codeApply = codeApply;
    }

    public int getOrdsn() {
        return ordsn;
    }

    public void setOrdsn(int ordsn) {
        this.ordsn = ordsn;
    }

    public Date getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(Date datePlan) {
        this.datePlan = datePlan;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getPkOrdris() {
        return pkOrdris;
    }

    public void setPkOrdris(String pkOrdris) {
        this.pkOrdris = pkOrdris;
    }

    public int getPriceCg() {
        return priceCg;
    }

    public void setPriceCg(int priceCg) {
        this.priceCg = priceCg;
    }

    public String getEuStatusOcc() {
        return euStatusOcc;
    }

    public void setEuStatusOcc(String euStatusOcc) {
        this.euStatusOcc = euStatusOcc;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public int getOrdsnParent() {
        return ordsnParent;
    }

    public void setOrdsnParent(int ordsnParent) {
        this.ordsnParent = ordsnParent;
    }

    public String getEuOrdtype() {
        return euOrdtype;
    }

    public void setEuOrdtype(String euOrdtype) {
        this.euOrdtype = euOrdtype;
    }

    public String getNowDept() {
        return nowDept;
    }

    public void setNowDept(String nowDept) {
        this.nowDept = nowDept;
    }


    public String getNameEmpOrd() {
        return nameEmpOrd;
    }

    public void setNameEmpOrd(String nameEmpOrd) {
        this.nameEmpOrd = nameEmpOrd;
    }

    public List<BlPubParamVo> getBlPubParamVos() {
        return blPubParamVos;
    }

    public void setBlPubParamVos(List<BlPubParamVo> blPubParamVos) {
        this.blPubParamVos = blPubParamVos;
    }

    public Date getTs() {
        return Ts;
    }

    public void setTs(Date ts) {
        Ts = ts;
    }
}
