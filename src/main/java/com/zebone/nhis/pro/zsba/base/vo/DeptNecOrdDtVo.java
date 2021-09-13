package com.zebone.nhis.pro.zsba.base.vo;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "BD_ORD_NEC_DT")
public class DeptNecOrdDtVo extends BaseModule {
    @PK
    @Field(value = "PK_ORDNECDT", id = Field.KeyId.UUID)
    private String pkOrdnecdt;
    @Field(value = "PK_ORDNEC")
    private String pkOrdnec;
    @Field(value = "PK_ORD")
    private String pkOrd;

    private String code;
    private String name;

    public String getPkOrdnecdt() {
        return pkOrdnecdt;
    }

    public void setPkOrdnecdt(String pkOrdnecdt) {
        this.pkOrdnecdt = pkOrdnecdt;
    }

    public String getPkOrdnec() {
        return pkOrdnec;
    }

    public void setPkOrdnec(String pkOrdnec) {
        this.pkOrdnec = pkOrdnec;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
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
}
