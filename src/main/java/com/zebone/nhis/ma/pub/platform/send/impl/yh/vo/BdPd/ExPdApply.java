package com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd;

import java.io.Serializable;
import java.util.Date;

/**
 * EX_PD_APPLY
 * @author 
 */
public class ExPdApply implements Serializable {
    private String pkPdap;

    private String pkOrg;

    private String euDirect;

    private String euAptype;

    private String codeApply;

    private String pkDeptAp;

    private String pkEmpAp;

    private String nameEmpAp;

    private Date dateAp;

    private String pkOrgDe;

    private String pkDeptDe;

    private Date dateDe;

    private String flagCancel;

    private String pkDeptCancel;

    private String pkEmpCancel;

    private String nameEmpCancel;

    private Date dateCancel;

    private String flagFinish;

    private String euStatus;

    private String note;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modityTime;

    private String delFlag;

    private Date ts;

    private String euPrint;

    private static final long serialVersionUID = 1L;

    public String getPkPdap() {
        return pkPdap;
    }

    public void setPkPdap(String pkPdap) {
        this.pkPdap = pkPdap;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getEuDirect() {
        return euDirect;
    }

    public void setEuDirect(String euDirect) {
        this.euDirect = euDirect;
    }

    public String getEuAptype() {
        return euAptype;
    }

    public void setEuAptype(String euAptype) {
        this.euAptype = euAptype;
    }

    public String getCodeApply() {
        return codeApply;
    }

    public void setCodeApply(String codeApply) {
        this.codeApply = codeApply;
    }

    public String getPkDeptAp() {
        return pkDeptAp;
    }

    public void setPkDeptAp(String pkDeptAp) {
        this.pkDeptAp = pkDeptAp;
    }

    public String getPkEmpAp() {
        return pkEmpAp;
    }

    public void setPkEmpAp(String pkEmpAp) {
        this.pkEmpAp = pkEmpAp;
    }

    public String getNameEmpAp() {
        return nameEmpAp;
    }

    public void setNameEmpAp(String nameEmpAp) {
        this.nameEmpAp = nameEmpAp;
    }

    public Date getDateAp() {
        return dateAp;
    }

    public void setDateAp(Date dateAp) {
        this.dateAp = dateAp;
    }

    public String getPkOrgDe() {
        return pkOrgDe;
    }

    public void setPkOrgDe(String pkOrgDe) {
        this.pkOrgDe = pkOrgDe;
    }

    public String getPkDeptDe() {
        return pkDeptDe;
    }

    public void setPkDeptDe(String pkDeptDe) {
        this.pkDeptDe = pkDeptDe;
    }

    public Date getDateDe() {
        return dateDe;
    }

    public void setDateDe(Date dateDe) {
        this.dateDe = dateDe;
    }

    public String getFlagCancel() {
        return flagCancel;
    }

    public void setFlagCancel(String flagCancel) {
        this.flagCancel = flagCancel;
    }

    public String getPkDeptCancel() {
        return pkDeptCancel;
    }

    public void setPkDeptCancel(String pkDeptCancel) {
        this.pkDeptCancel = pkDeptCancel;
    }

    public String getPkEmpCancel() {
        return pkEmpCancel;
    }

    public void setPkEmpCancel(String pkEmpCancel) {
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel() {
        return nameEmpCancel;
    }

    public void setNameEmpCancel(String nameEmpCancel) {
        this.nameEmpCancel = nameEmpCancel;
    }

    public Date getDateCancel() {
        return dateCancel;
    }

    public void setDateCancel(Date dateCancel) {
        this.dateCancel = dateCancel;
    }

    public String getFlagFinish() {
        return flagFinish;
    }

    public void setFlagFinish(String flagFinish) {
        this.flagFinish = flagFinish;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getEuPrint() {
        return euPrint;
    }

    public void setEuPrint(String euPrint) {
        this.euPrint = euPrint;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ExPdApply other = (ExPdApply) that;
        return (this.getPkPdap() == null ? other.getPkPdap() == null : this.getPkPdap().equals(other.getPkPdap()))
            && (this.getPkOrg() == null ? other.getPkOrg() == null : this.getPkOrg().equals(other.getPkOrg()))
            && (this.getEuDirect() == null ? other.getEuDirect() == null : this.getEuDirect().equals(other.getEuDirect()))
            && (this.getEuAptype() == null ? other.getEuAptype() == null : this.getEuAptype().equals(other.getEuAptype()))
            && (this.getCodeApply() == null ? other.getCodeApply() == null : this.getCodeApply().equals(other.getCodeApply()))
            && (this.getPkDeptAp() == null ? other.getPkDeptAp() == null : this.getPkDeptAp().equals(other.getPkDeptAp()))
            && (this.getPkEmpAp() == null ? other.getPkEmpAp() == null : this.getPkEmpAp().equals(other.getPkEmpAp()))
            && (this.getNameEmpAp() == null ? other.getNameEmpAp() == null : this.getNameEmpAp().equals(other.getNameEmpAp()))
            && (this.getDateAp() == null ? other.getDateAp() == null : this.getDateAp().equals(other.getDateAp()))
            && (this.getPkOrgDe() == null ? other.getPkOrgDe() == null : this.getPkOrgDe().equals(other.getPkOrgDe()))
            && (this.getPkDeptDe() == null ? other.getPkDeptDe() == null : this.getPkDeptDe().equals(other.getPkDeptDe()))
            && (this.getDateDe() == null ? other.getDateDe() == null : this.getDateDe().equals(other.getDateDe()))
            && (this.getFlagCancel() == null ? other.getFlagCancel() == null : this.getFlagCancel().equals(other.getFlagCancel()))
            && (this.getPkDeptCancel() == null ? other.getPkDeptCancel() == null : this.getPkDeptCancel().equals(other.getPkDeptCancel()))
            && (this.getPkEmpCancel() == null ? other.getPkEmpCancel() == null : this.getPkEmpCancel().equals(other.getPkEmpCancel()))
            && (this.getNameEmpCancel() == null ? other.getNameEmpCancel() == null : this.getNameEmpCancel().equals(other.getNameEmpCancel()))
            && (this.getDateCancel() == null ? other.getDateCancel() == null : this.getDateCancel().equals(other.getDateCancel()))
            && (this.getFlagFinish() == null ? other.getFlagFinish() == null : this.getFlagFinish().equals(other.getFlagFinish()))
            && (this.getEuStatus() == null ? other.getEuStatus() == null : this.getEuStatus().equals(other.getEuStatus()))
            && (this.getNote() == null ? other.getNote() == null : this.getNote().equals(other.getNote()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getModifier() == null ? other.getModifier() == null : this.getModifier().equals(other.getModifier()))
            && (this.getModityTime() == null ? other.getModityTime() == null : this.getModityTime().equals(other.getModityTime()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getTs() == null ? other.getTs() == null : this.getTs().equals(other.getTs()))
            && (this.getEuPrint() == null ? other.getEuPrint() == null : this.getEuPrint().equals(other.getEuPrint()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPkPdap() == null) ? 0 : getPkPdap().hashCode());
        result = prime * result + ((getPkOrg() == null) ? 0 : getPkOrg().hashCode());
        result = prime * result + ((getEuDirect() == null) ? 0 : getEuDirect().hashCode());
        result = prime * result + ((getEuAptype() == null) ? 0 : getEuAptype().hashCode());
        result = prime * result + ((getCodeApply() == null) ? 0 : getCodeApply().hashCode());
        result = prime * result + ((getPkDeptAp() == null) ? 0 : getPkDeptAp().hashCode());
        result = prime * result + ((getPkEmpAp() == null) ? 0 : getPkEmpAp().hashCode());
        result = prime * result + ((getNameEmpAp() == null) ? 0 : getNameEmpAp().hashCode());
        result = prime * result + ((getDateAp() == null) ? 0 : getDateAp().hashCode());
        result = prime * result + ((getPkOrgDe() == null) ? 0 : getPkOrgDe().hashCode());
        result = prime * result + ((getPkDeptDe() == null) ? 0 : getPkDeptDe().hashCode());
        result = prime * result + ((getDateDe() == null) ? 0 : getDateDe().hashCode());
        result = prime * result + ((getFlagCancel() == null) ? 0 : getFlagCancel().hashCode());
        result = prime * result + ((getPkDeptCancel() == null) ? 0 : getPkDeptCancel().hashCode());
        result = prime * result + ((getPkEmpCancel() == null) ? 0 : getPkEmpCancel().hashCode());
        result = prime * result + ((getNameEmpCancel() == null) ? 0 : getNameEmpCancel().hashCode());
        result = prime * result + ((getDateCancel() == null) ? 0 : getDateCancel().hashCode());
        result = prime * result + ((getFlagFinish() == null) ? 0 : getFlagFinish().hashCode());
        result = prime * result + ((getEuStatus() == null) ? 0 : getEuStatus().hashCode());
        result = prime * result + ((getNote() == null) ? 0 : getNote().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getModifier() == null) ? 0 : getModifier().hashCode());
        result = prime * result + ((getModityTime() == null) ? 0 : getModityTime().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getTs() == null) ? 0 : getTs().hashCode());
        result = prime * result + ((getEuPrint() == null) ? 0 : getEuPrint().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pkPdap=").append(pkPdap);
        sb.append(", pkOrg=").append(pkOrg);
        sb.append(", euDirect=").append(euDirect);
        sb.append(", euAptype=").append(euAptype);
        sb.append(", codeApply=").append(codeApply);
        sb.append(", pkDeptAp=").append(pkDeptAp);
        sb.append(", pkEmpAp=").append(pkEmpAp);
        sb.append(", nameEmpAp=").append(nameEmpAp);
        sb.append(", dateAp=").append(dateAp);
        sb.append(", pkOrgDe=").append(pkOrgDe);
        sb.append(", pkDeptDe=").append(pkDeptDe);
        sb.append(", dateDe=").append(dateDe);
        sb.append(", flagCancel=").append(flagCancel);
        sb.append(", pkDeptCancel=").append(pkDeptCancel);
        sb.append(", pkEmpCancel=").append(pkEmpCancel);
        sb.append(", nameEmpCancel=").append(nameEmpCancel);
        sb.append(", dateCancel=").append(dateCancel);
        sb.append(", flagFinish=").append(flagFinish);
        sb.append(", euStatus=").append(euStatus);
        sb.append(", note=").append(note);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifier=").append(modifier);
        sb.append(", modityTime=").append(modityTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", ts=").append(ts);
        sb.append(", euPrint=").append(euPrint);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}