package com.zebone.nhis.pro.zsba.base.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.List;

@Table(value = "BD_ORD_NEC")
public class DeptNecOrdGroupVo extends BaseModule {
    @PK
    @Field(value = "PK_ORDNEC", id = KeyId.UUID)
    private String pkOrdnec;
    @Field(value = "CODE_GROUP")
    private String codeGroup;
    @Field(value = "NAME_GROUP")
    private String nameGroup;
    @Field(value = "PK_DEPT")
    private String pkDept;

    private List<DeptNecOrdDtVo> ordDtList;
    public String getPkOrdnec() {
        return pkOrdnec;
    }

    public void setPkOrdnec(String pkOrdnec) {
        this.pkOrdnec = pkOrdnec;
    }


    public String getCodeGroup() {
        return codeGroup;
    }

    public void setCodeGroup(String codeGroup) {
        this.codeGroup = codeGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public List<DeptNecOrdDtVo> getOrdDtList() {
        return ordDtList;
    }

    public void setOrdDtList(List<DeptNecOrdDtVo> ordDtList) {
        this.ordDtList = ordDtList;
    }
}
