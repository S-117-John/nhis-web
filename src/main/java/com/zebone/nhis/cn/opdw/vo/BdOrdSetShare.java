package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 模板科室共享表
 */
@Table(value="BD_ORD_SET_SHARE")
public class BdOrdSetShare extends BaseModule {

    @PK
    @Field(value="PK_ORDSETSHARE",id= Field.KeyId.UUID)
    private String pkOrdsetshare;

    @Field(value = "PK_ORDSET")
    private String pkOrdset;

    @Field(value = "PK_DEPT_SHARE")
    private String pkDeptShare;

    public String getPkOrdsetshare() {
        return pkOrdsetshare;
    }

    public void setPkOrdsetshare(String pkOrdsetshare) {
        this.pkOrdsetshare = pkOrdsetshare;
    }

    public String getPkOrdset() {
        return pkOrdset;
    }

    public void setPkOrdset(String pkOrdset) {
        this.pkOrdset = pkOrdset;
    }

    public String getPkDeptShare() {
        return pkDeptShare;
    }

    public void setPkDeptShare(String pkDeptShare) {
        this.pkDeptShare = pkDeptShare;
    }
}
