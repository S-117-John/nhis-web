package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnOrderVO extends CnOrder {
    private Double quanMin;
    private Double vol;
    private String flagPivas;
    private String pkUnitPack;
    private Integer cnt;//频次执行次数
    private Integer cntCycle;//频次执行周期
    private Double stock;//当前库存量
    private String pkUnitStock;//当前库存单位
    private String pkUnitMin;//最新单位
    private String euMuputype;//药品取整模式
    private String adjuvantDrug;//辅助用药标志
    private String dtLevelDise;//病情等级
    private String flagFunDept;//是否为（产房、血透）职能科室
    private String nameWg;//医疗组名称
    private String dtExcardtype;//对应执行卡类型

    public String getDtExcardtype() {
        return dtExcardtype;
    }

    public void setDtExcardtype(String dtExcardtype) {
        this.dtExcardtype = dtExcardtype;
    }

    public Double getPackSizeStoreDef() {
        return packSizeStoreDef;
    }

    public void setPackSizeStoreDef(Double packSizeStoreDef) {
        this.packSizeStoreDef = packSizeStoreDef;
    }

    private Double packSizeStoreDef;//仓库默认包装量


    public String getEuMuputype() {
        return euMuputype;
    }

    public void setEuMuputype(String euMuputype) {
        this.euMuputype = euMuputype;
    }

    public String getPkUnitMin() {
        return pkUnitMin;
    }

    public void setPkUnitMin(String pkUnitMin) {
        this.pkUnitMin = pkUnitMin;
    }

    public Integer getCntCycle() {
        return cntCycle;
    }

    public void setCntCycle(Integer cntCycle) {
        this.cntCycle = cntCycle;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getPkUnitStock() {
        return pkUnitStock;
    }

    public void setPkUnitStock(String pkUnitStock) {
        this.pkUnitStock = pkUnitStock;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }


    public Double getQuanMin() {
        return quanMin;
    }

    public void setQuanMin(Double quanMin) {
        this.quanMin = quanMin;
    }

    public String getFlagPivas() {
        return flagPivas;
    }

    public void setFlagPivas(String flagPivas) {
        this.flagPivas = flagPivas;
    }

    public String getPkUnitPack() {
        return pkUnitPack;
    }

    public void setPkUnitPack(String pkUnitPack) {
        this.pkUnitPack = pkUnitPack;
    }

    public String getAdjuvantDrug() {
        return adjuvantDrug;
    }

    public void setAdjuvantDrug(String adjuvantDrug) {
        this.adjuvantDrug = adjuvantDrug;
    }

    public String getDtLevelDise() {
        return dtLevelDise;
    }

    public void setDtLevelDise(String dtLevelDise) {
        this.dtLevelDise = dtLevelDise;
    }

    public String getFlagFunDept() {
        return flagFunDept;
    }

    public void setFlagFunDept(String flagFunDept) {
        this.flagFunDept = flagFunDept;
    }

    public String getNameWg() {
        return nameWg;
    }

    public void setNameWg(String nameWg) {
        this.nameWg = nameWg;
    }
}
