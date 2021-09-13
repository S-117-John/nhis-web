package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


@Table(value="EX_OP_OCC")
public class ExOpOcc extends BaseModule {
    @PK
    @Field(value="PK_OPOCC",id= Field.KeyId.UUID)
    private String pkOpocc;

    @Field(value = "PK_ORG")
    private String pkOrg;

    @Field(value = "PK_PV")
    private String pkPv;

    @Field(value = "PK_CNORD")
    private String pkCnord;

    @Field(value = "EU_OPTYPE")
    private String euOptype;

    @Field(value = "PK_DIAG_PRE")
    private String pkDiagPre;

    @Field(value = "DESC_DIAG_PRE")
    private String descDiagPre;

    @Field(value = "PK_DIAG_AF")
    private String pkDiagAf;

    @Field(value = "DESC_DIAG_AF")
    private String descDiagAf;

    @Field(value = "DESC_OPBODY")
    private String descOpbody;

    @Field(value = "DT_POSI")
    private String dtPosi;

    @Field(value = "DT_OPLEVEL")
    private String dtOplevel;

    @Field(value = "PK_OP")
    private String pkOp;

    @Field(value = "DESC_OP")
    private String descOp;

    @Field(value = "CNT_OP")
    private String cntOp;

    @Field(value = "DESC_OP_SUB")
    private String descOpSub;

    @Field(value = "DT_ANAE")
    private String dtAnae;

    @Field(value = "DESC_ANBODY")
    private String descAnbody;

    @Field(value = "DT_INCITYPE")
    private String dtIncitype;

    @Field(value = "DT_HEAL")
    private String dtHeal;

    @Field(value = "DT_ASEPSIS")
    private String dtAsepsis;

    @Field(value = "QUAN_BL")
    private String quanBl;

    @Field(value = "QUAN_BT")
    private String quanBt;

    @Field(value = "FLAG_REACT_BT")
    private String flagReactBt;

    @Field(value = "QUAN_IV")
    private String quanIv;

    @Field(value = "DRUG_BEFORE")
    private String drugBefore;

    @Field(value = "DRUG_CUR")
    private String drugCur;

    @Field(value = "DRUG_AFTER")
    private String drugAfter;

    @Field(value = "FLAG_DRAINAGE")
    private String flagDrainage;

    @Field(value = "NUM_DRAINAGE")
    private String maDrainage;

    @Field(value = "NUM_DRAINAGE")
    private String numDrainage;

    @Field(value = "BODY_DRAINAGE")
    private String bodyDrainage;

    @Field(value = "FLAG_UNEXPECTED")
    private String flagUnexpected;

    @Field(value = "NOTE_UNEXPECTED")
    private String noteUnexpected;

    @Field(value = "FLAG_ISOLATE")
    private String flagIsolate;

    @Field(value = "PK_EMR")
    private String pkEmr;

    @Field(value = "PK_EMP_PHY_OP")
    private String pkEmpPhyOp;

    @Field(value = "NAME_EMP_PHY_OP")
    private String nameEmpPhyOp;

    @Field(value = "PK_EMP_ANAE")
    private String pkEmpAnae;

    @Field(value = "NAME_EMP_ANAE")
    private String nameEmpAnae;

    @Field(value = "PK_EMP_ASIS")
    private String pkEmpAsis;

    @Field(value = "NAME_EMP_ASIS")
    private String nameEmpAsis;

    @Field(value = "PK_EMP_ASIS2")
    private String pkEmpAsis2;

    @Field(value = "NAME_EMP_ASIS2")
    private String nameEmpAsis2;

    @Field(value = "PK_EMP_ASIS3")
    private String pkEmpAsis3;

    @Field(value = "NAME_EMP_ASIS3")
    private String nameEmpAsis3;

    @Field(value = "PK_EMP_SCRUB")
    private String pkEmpScrub;

    @Field(value = "NAME_EMP_SCRUB")
    private String nameEmpScrub;

    @Field(value = "PK_EMP_CIRCUL")
    private String pkEmpCircul;

    @Field(value = "NAME_EMP_CRICUL")
    private String nameEmpCricul;

    @Field(value = "PK_OPT")
    private String pkOpt;

    @Field(value = "DATE_BEGIN")
    private Date dateBegin;

    @Field(value = "DATE_END")
    private Date dateEnd;

    @Field(value = "NOTE")
    private String note;

    @Field(value = "CREATOR")
    private String creator;

    @Field(value = "CREATE_TIME")
    private Date createTime;

    @Field(value = "MODIFIER")
    private String modifier;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    @Field(value = "DEL_FLAG")
    private String delFlag;

    @Field(value = "TS")
    private Date ts;

    @Field(value = "PK_EMP_TECH")
    private String pkEmpTech;
    
    @Field(value = "NAME_EMP_TECH")
    private String nameEmpTech;
    
    @Field(value = "DSA_NO")
    private String dsaNo;

    @Field(value = "PK_DEPT_ANAE")
    private String pkDeptAnae;

    //手术方式
    @Field(value = "DT_OPMODE")
    private String dtOpmode;
    
    public String getDtOpmode() {
		return dtOpmode;
	}

	public void setDtOpmode(String dtOpmode) {
		this.dtOpmode = dtOpmode;
	}

	public String getPkOpocc() {
        return pkOpocc;
    }

    public void setPkOpocc(String pkOpocc) {
        this.pkOpocc = pkOpocc;
    }


    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }


    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }


    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }


    public String getEuOptype() {
        return euOptype;
    }

    public void setEuOptype(String euOptype) {
        this.euOptype = euOptype;
    }


    public String getPkDiagPre() {
        return pkDiagPre;
    }

    public void setPkDiagPre(String pkDiagPre) {
        this.pkDiagPre = pkDiagPre;
    }


    public String getDescDiagPre() {
        return descDiagPre;
    }

    public void setDescDiagPre(String descDiagPre) {
        this.descDiagPre = descDiagPre;
    }


    public String getPkDiagAf() {
        return pkDiagAf;
    }

    public void setPkDiagAf(String pkDiagAf) {
        this.pkDiagAf = pkDiagAf;
    }


    public String getDescDiagAf() {
        return descDiagAf;
    }

    public void setDescDiagAf(String descDiagAf) {
        this.descDiagAf = descDiagAf;
    }


    public String getDescOpbody() {
        return descOpbody;
    }

    public void setDescOpbody(String descOpbody) {
        this.descOpbody = descOpbody;
    }


    public String getDtPosi() {
        return dtPosi;
    }

    public void setDtPosi(String dtPosi) {
        this.dtPosi = dtPosi;
    }


    public String getDtOplevel() {
        return dtOplevel;
    }

    public void setDtOplevel(String dtOplevel) {
        this.dtOplevel = dtOplevel;
    }


    public String getPkOp() {
        return pkOp;
    }

    public void setPkOp(String pkOp) {
        this.pkOp = pkOp;
    }


    public String getDescOp() {
        return descOp;
    }

    public void setDescOp(String descOp) {
        this.descOp = descOp;
    }


    public String getCntOp() {
        return cntOp;
    }

    public void setCntOp(String cntOp) {
        this.cntOp = cntOp;
    }


    public String getDescOpSub() {
        return descOpSub;
    }

    public void setDescOpSub(String descOpSub) {
        this.descOpSub = descOpSub;
    }


    public String getDtAnae() {
        return dtAnae;
    }

    public void setDtAnae(String dtAnae) {
        this.dtAnae = dtAnae;
    }


    public String getDescAnbody() {
        return descAnbody;
    }

    public void setDescAnbody(String descAnbody) {
        this.descAnbody = descAnbody;
    }


    public String getDtIncitype() {
        return dtIncitype;
    }

    public void setDtIncitype(String dtIncitype) {
        this.dtIncitype = dtIncitype;
    }


    public String getDtHeal() {
        return dtHeal;
    }

    public void setDtHeal(String dtHeal) {
        this.dtHeal = dtHeal;
    }


    public String getDtAsepsis() {
        return dtAsepsis;
    }

    public void setDtAsepsis(String dtAsepsis) {
        this.dtAsepsis = dtAsepsis;
    }


    public String getQuanBl() {
        return quanBl;
    }

    public void setQuanBl(String quanBl) {
        this.quanBl = quanBl;
    }


    public String getQuanBt() {
        return quanBt;
    }

    public void setQuanBt(String quanBt) {
        this.quanBt = quanBt;
    }


    public String getFlagReactBt() {
        return flagReactBt;
    }

    public void setFlagReactBt(String flagReactBt) {
        this.flagReactBt = flagReactBt;
    }


    public String getQuanIv() {
        return quanIv;
    }

    public void setQuanIv(String quanIv) {
        this.quanIv = quanIv;
    }


    public String getDrugBefore() {
        return drugBefore;
    }

    public void setDrugBefore(String drugBefore) {
        this.drugBefore = drugBefore;
    }


    public String getDrugCur() {
        return drugCur;
    }

    public void setDrugCur(String drugCur) {
        this.drugCur = drugCur;
    }


    public String getDrugAfter() {
        return drugAfter;
    }

    public void setDrugAfter(String drugAfter) {
        this.drugAfter = drugAfter;
    }


    public String getFlagDrainage() {
        return flagDrainage;
    }

    public void setFlagDrainage(String flagDrainage) {
        this.flagDrainage = flagDrainage;
    }


    public String getMaDrainage() {
        return maDrainage;
    }

    public void setMaDrainage(String maDrainage) {
        this.maDrainage = maDrainage;
    }


    public String getNumDrainage() {
        return numDrainage;
    }

    public void setNumDrainage(String numDrainage) {
        this.numDrainage = numDrainage;
    }


    public String getBodyDrainage() {
        return bodyDrainage;
    }

    public void setBodyDrainage(String bodyDrainage) {
        this.bodyDrainage = bodyDrainage;
    }


    public String getFlagUnexpected() {
        return flagUnexpected;
    }

    public void setFlagUnexpected(String flagUnexpected) {
        this.flagUnexpected = flagUnexpected;
    }


    public String getNoteUnexpected() {
        return noteUnexpected;
    }

    public void setNoteUnexpected(String noteUnexpected) {
        this.noteUnexpected = noteUnexpected;
    }


    public String getFlagIsolate() {
        return flagIsolate;
    }

    public void setFlagIsolate(String flagIsolate) {
        this.flagIsolate = flagIsolate;
    }


    public String getPkEmr() {
        return pkEmr;
    }

    public void setPkEmr(String pkEmr) {
        this.pkEmr = pkEmr;
    }


    public String getPkEmpPhyOp() {
        return pkEmpPhyOp;
    }

    public void setPkEmpPhyOp(String pkEmpPhyOp) {
        this.pkEmpPhyOp = pkEmpPhyOp;
    }


    public String getNameEmpPhyOp() {
        return nameEmpPhyOp;
    }

    public void setNameEmpPhyOp(String nameEmpPhyOp) {
        this.nameEmpPhyOp = nameEmpPhyOp;
    }


    public String getPkEmpAnae() {
        return pkEmpAnae;
    }

    public void setPkEmpAnae(String pkEmpAnae) {
        this.pkEmpAnae = pkEmpAnae;
    }


    public String getNameEmpAnae() {
        return nameEmpAnae;
    }

    public void setNameEmpAnae(String nameEmpAnae) {
        this.nameEmpAnae = nameEmpAnae;
    }


    public String getPkEmpAsis() {
        return pkEmpAsis;
    }

    public void setPkEmpAsis(String pkEmpAsis) {
        this.pkEmpAsis = pkEmpAsis;
    }


    public String getNameEmpAsis() {
        return nameEmpAsis;
    }

    public void setNameEmpAsis(String nameEmpAsis) {
        this.nameEmpAsis = nameEmpAsis;
    }


    public String getPkEmpAsis2() {
        return pkEmpAsis2;
    }

    public void setPkEmpAsis2(String pkEmpAsis2) {
        this.pkEmpAsis2 = pkEmpAsis2;
    }


    public String getNameEmpAsis2() {
        return nameEmpAsis2;
    }

    public void setNameEmpAsis2(String nameEmpAsis2) {
        this.nameEmpAsis2 = nameEmpAsis2;
    }


    public String getPkEmpAsis3() {
        return pkEmpAsis3;
    }

    public void setPkEmpAsis3(String pkEmpAsis3) {
        this.pkEmpAsis3 = pkEmpAsis3;
    }


    public String getNameEmpAsis3() {
        return nameEmpAsis3;
    }

    public void setNameEmpAsis3(String nameEmpAsis3) {
        this.nameEmpAsis3 = nameEmpAsis3;
    }


    public String getPkEmpScrub() {
        return pkEmpScrub;
    }

    public void setPkEmpScrub(String pkEmpScrub) {
        this.pkEmpScrub = pkEmpScrub;
    }


    public String getNameEmpScrub() {
        return nameEmpScrub;
    }

    public void setNameEmpScrub(String nameEmpScrub) {
        this.nameEmpScrub = nameEmpScrub;
    }


    public String getPkEmpCircul() {
        return pkEmpCircul;
    }

    public void setPkEmpCircul(String pkEmpCircul) {
        this.pkEmpCircul = pkEmpCircul;
    }


    public String getNameEmpCricul() {
        return nameEmpCricul;
    }

    public void setNameEmpCricul(String nameEmpCricul) {
        this.nameEmpCricul = nameEmpCricul;
    }


    public String getPkOpt() {
        return pkOpt;
    }

    public void setPkOpt(String pkOpt) {
        this.pkOpt = pkOpt;
    }


    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }


    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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

	public String getPkEmpTech() {
		return pkEmpTech;
	}

	public void setPkEmpTech(String pkEmpTech) {
		this.pkEmpTech = pkEmpTech;
	}

	public String getNameEmpTech() {
		return nameEmpTech;
	}

	public void setNameEmpTech(String nameEmpTech) {
		this.nameEmpTech = nameEmpTech;
	}

	public String getDsaNo() {
		return dsaNo;
	}

	public void setDsaNo(String dsaNo) {
		this.dsaNo = dsaNo;
	}

    public String getPkDeptAnae() {
        return pkDeptAnae;
    }

    public void setPkDeptAnae(String pkDeptAnae) {
        this.pkDeptAnae = pkDeptAnae;
    }
}
