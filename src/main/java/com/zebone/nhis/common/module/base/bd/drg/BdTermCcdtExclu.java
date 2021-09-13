package com.zebone.nhis.common.module.base.bd.drg;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
@Table("BD_TERM_CCDT_EXCLU")
public class BdTermCcdtExclu {
    @PK
    @Field(value="PK_CCDTEXCLU",id= Field.KeyId.UUID)
     private String  pkCcdtexclu ;
    @Field(value="PK_ORG")
     private String  pkOrg;
    @Field(value="EU_CHILD")
     private String  euChild;
    @Field(value="GROUPNO")
     private String  groupno;
    @Field(value="NAME_RULE")
     private String  nameRule;
    @Field(value="SPCODE")
     private String  spcode;
    @Field(value="EU_EXCLUTYPE")
     private String  euExclutype;
    @Field(value="GROUPNO_EXCLU")
     private String  groupnoExclu;
    @Field(value="CODE_CCDT")
     private String  codeCcdt;
    @Field(value="PK_CCDT")
     private String  pkCcdt;
    @Field(value="NOTE")
     private String  note;
    @Field(value="CREATOR")
     private String  creator;
    @Field(value="CREATE_TIME")
     private Date createTime;
    @Field(value="MODIFIER")
     private String  modifier;
    @Field(value="FLAG_DEL")
     private String  flagDel;
    @Field(value="TS")
     private Date  ts;
    private String bdTermCcdt;


    public String getBdTermCcdt() {
        return bdTermCcdt;
    }

    public void setBdTermCcdt(String bdTermCcdt) {
        this.bdTermCcdt = bdTermCcdt;
    }


    public String getPkCcdtexclu() {
        return pkCcdtexclu;
    }

    public void setPkCcdtexclu(String pkCcdtexclu) {
        this.pkCcdtexclu = pkCcdtexclu;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getEuChild() {
        return euChild;
    }

    public void setEuChild(String euChild) {
        this.euChild = euChild;
    }

    public String getGroupno() {
        return groupno;
    }

    public void setGroupno(String groupno) {
        this.groupno = groupno;
    }

    public String getNameRule() {
        return nameRule;
    }

    public void setNameRule(String nameRule) {
        this.nameRule = nameRule;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getEuExclutype() {
        return euExclutype;
    }

    public void setEuExclutype(String euExclutype) {
        this.euExclutype = euExclutype;
    }

    public String getGroupnoExclu() {
        return groupnoExclu;
    }

    public void setGroupnoExclu(String groupnoExclu) {
        this.groupnoExclu = groupnoExclu;
    }

    public String getCodeCcdt() {
        return codeCcdt;
    }

    public void setCodeCcdt(String codeCcdt) {
        this.codeCcdt = codeCcdt;
    }

    public String getPkCcdt() {
        return pkCcdt;
    }

    public void setPkCcdt(String pkCcdt) {
        this.pkCcdt = pkCcdt;
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

    public String getFlagDel() {
        return flagDel;
    }

    public void setFlagDel(String flagDel) {
        this.flagDel = flagDel;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
