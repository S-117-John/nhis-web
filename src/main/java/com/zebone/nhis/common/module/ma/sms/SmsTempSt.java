package com.zebone.nhis.common.module.ma.sms;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="SMS_TEMP_ST")
public class SmsTempSt extends BaseModule {

    @PK
    @Field(value="PK_SMSTEMPST",id= Field.KeyId.UUID)
    private String pkSmstempst;

    @Field(value="PK_SMSTEMP")
    private String pkSmstemp;

    @Field(value="DT_TYPE")
    private String dtType;

    @Field(value="VAL")
    private String val;


    public String getPkSmstempst() {
        return pkSmstempst;
    }

    public void setPkSmstempst(String pkSmstempst) {
        this.pkSmstempst = pkSmstempst;
    }

    public String getPkSmstemp() {
        return pkSmstemp;
    }

    public void setPkSmstemp(String pkSmstemp) {
        this.pkSmstemp = pkSmstemp;
    }

    public String getDtType() {
        return dtType;
    }

    public void setDtType(String dtType) {
        this.dtType = dtType;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
