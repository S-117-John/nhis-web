package com.zebone.nhis.pro.zsrm.bl.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnOrderVo extends CnOrder {
    private String nameUnitDos;
    private String nameSupply;
    private String nameSupplyAdd;
    private String presNo;
    private String euType;
    private String clsCode;

    public String getClsCode() {
        return clsCode;
    }

    public void setClsCode(String clsCode) {
        this.clsCode = clsCode;
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public String getNameUnitDos() {
        return nameUnitDos;
    }

    public void setNameUnitDos(String nameUnitDos) {
        this.nameUnitDos = nameUnitDos;
    }

    public String getNameSupply() {
        return nameSupply;
    }

    public void setNameSupply(String nameSupply) {
        this.nameSupply = nameSupply;
    }

    public String getNameSupplyAdd() {
        return nameSupplyAdd;
    }

    public void setNameSupplyAdd(String nameSupplyAdd) {
        this.nameSupplyAdd = nameSupplyAdd;
    }

    @Override
    public String getPresNo() {
        return presNo;
    }

    @Override
    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }
}
