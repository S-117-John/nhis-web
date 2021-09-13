package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="INS_DICT")
public class InsDict extends BaseModule {

    @PK
    @Field(value="PK_INSDICT",id= Field.KeyId.UUID)
    private String pkInsdict;

    @Field(value="PK_INSDICTTYPE")
    private String pkInsdicttype;

    @Field(value="CODE")
    private String code;

    @Field(value="NAME")
    private String name;

    @Field(value="SPCODE")
    private String spcode;

    @Field(value="D_CODE")
    private String dcode;

    @Field(value="NOTE")
    private String note;

    @Field(value="FLAG_DEF")
    private String flagDef;

    public String getPkInsdict() {
        return pkInsdict;
    }

    public void setPkInsdict(String pkInsdict) {
        this.pkInsdict = pkInsdict;
    }

    public String getPkInsdicttype() {
        return pkInsdicttype;
    }

    public void setPkInsdicttype(String pkInsdicttype) {
        this.pkInsdicttype = pkInsdicttype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFlagDef() {
        return flagDef;
    }

    public void setFlagDef(String flagDef) {
        this.flagDef = flagDef;
    }
}
