package com.zebone.nhis.common.module.scm.pub;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_PD_OUTCODE - bd_pd_outcode
 *
 * @since 2020-10-16 16:39:16
 */
@Table(value = "BD_PD_OUTCODE")
public class BdPdOutcode extends BaseModule {
    @Field(value = "PK_OUTCODE")
    private String pkOUtcode;

    @Field(value="PK_PD")
    private String pkPd;

    @Field(value="DT_PLATFORM")
    private String dtPlatform;

    @Field(value="OUTCODE")
    private String outcode;

    @Field(value="NOTE")
    private String note;

    @Field(value="FLAG_ACTIVE")
    private String flagActive;

    public String getPkOUtcode() {
        return pkOUtcode;
    }

    public void setPkOUtcode(String pkOUtcode) {
        this.pkOUtcode = pkOUtcode;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public String getDtPlatform() {
        return dtPlatform;
    }

    public void setDtPlatform(String dtPlatform) {
        this.dtPlatform = dtPlatform;
    }

    public String getOutcode() {
        return outcode;
    }

    public void setOutcode(String outcode) {
        this.outcode = outcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFlagActive() {
        return flagActive;
    }

    public void setFlagActive(String flagActive) {
        this.flagActive = flagActive;
    }
}
