package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;

public class CnConsultResponseCaVO extends CnConsultResponse {
    //CA认证信息
    private CnSignCa cnSignCa;
    //会诊项目主键
    private String pkOrd;
    //会诊医嘱主键
    private  String pkCnord;

    //是否签署后的编辑操作
    private int isEditing;

    public void setCnSignCa(CnSignCa cnSignCa) {
        this.cnSignCa = cnSignCa;
    }

    public CnSignCa getCnSignCa() {
        return cnSignCa;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public int getIsEditing() {
        return isEditing;
    }

    public void setIsEditing(int isEditing) {
        this.isEditing = isEditing;
    }
}
