package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

/**
 * 医嘱计费接口Vo
 */
public class CnBlVo {

    public String pkOrg; //机构
    public String pkPres; //处方主键，可以为null
    public String euPvType ; //患者就诊类型
    public String pkPv ; //患者就诊主键
    public String pkPi ; //患者主键
    public String pkOrd ; //医嘱项目，可以为null
    public String pkCnord ; //医嘱主键，可以为null
    public String pkItem ; //计费项目主键
    public String pkUnitCg ;//计费单位
    public String pkOrgEx ;//执行机构
    public String pkOrgApp ;//开立机构
    public String pkDeptEx ;//执行科室
    public String pkDeptApp ;//开立科室
    public String pkDeptNsApp ;//开立病区
    public String pkEmpApp ;//开立医生
    public String nameEmpApp;//开立医生名称
    public String flagPd ;//物品标志
    public String flagPv ;//XXX 默认为0
    public Date   dateHap;//费用发生日期
    public String pkDeptCg ;//记费部门
    public String pkEmpCg ;//记费人员
    public String nameEmpCg;//记费人员名称
    public String name;//计费项目名称
    public String spec ; //规格
    public Double price ;//单价
    public Double amount; //价格
    public Double quanCg ;//数量
    public Double amountPi;//患者支付
    public String id ;
    public String barcode ;// 耗材编码
    public String dtItemtype ;// 是否是高值耗材类型
    public String oldId ;// 老系统Id
    public String pkEmpEx ;//执行医生
    public String nameEmpEx ;//执行医生姓名
    public String euBltype ;//记费类型--中二  默认9
    public String pkCnordRl ;//关联医嘱--中二
    public String unit ;//单位
    public String nameSupply ;//用法
    public String nameFreq ;//频次
    public String dosage ;//用量
    public String unitDos ;//用量单位
    public String batchNo ;//批次
    public double priceCost ;//购入价_当前包装单位
    public String packSize ;//包装量
    public String pkUnitPd ;//物品单位

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPres() {
        return pkPres;
    }

    public void setPkPres(String pkPres) {
        this.pkPres = pkPres;
    }

    public String getEuPvType() {
        return euPvType;
    }

    public void setEuPvType(String euPvType) {
        this.euPvType = euPvType;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
    }

    public String getpkCnord() {
        return pkCnord;
    }

    public void setpkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getPkUnitCg() {
        return pkUnitCg;
    }

    public void setPkUnitCg(String pkUnitCg) {
        this.pkUnitCg = pkUnitCg;
    }

    public String getPkOrgEx() {
        return pkOrgEx;
    }

    public void setPkOrgEx(String pkOrgEx) {
        this.pkOrgEx = pkOrgEx;
    }

    public String getPkOrgApp() {
        return pkOrgApp;
    }

    public void setPkOrgApp(String pkOrgApp) {
        this.pkOrgApp = pkOrgApp;
    }

    public String getPkDeptEx() {
        return pkDeptEx;
    }

    public void setPkDeptEx(String pkDeptEx) {
        this.pkDeptEx = pkDeptEx;
    }

    public String getPkDeptApp() {
        return pkDeptApp;
    }

    public void setPkDeptApp(String pkDeptApp) {
        this.pkDeptApp = pkDeptApp;
    }

    public String getPkDeptNsApp() {
        return pkDeptNsApp;
    }

    public void setPkDeptNsApp(String pkDeptNsApp) {
        this.pkDeptNsApp = pkDeptNsApp;
    }

    public String getPkEmpApp() {
        return pkEmpApp;
    }

    public void setPkEmpApp(String pkEmpApp) {
        this.pkEmpApp = pkEmpApp;
    }

    public String getNameEmpApp() {
        return nameEmpApp;
    }

    public void setNameEmpApp(String nameEmpApp) {
        this.nameEmpApp = nameEmpApp;
    }

    public String getFlagPd() {
        return flagPd;
    }

    public void setFlagPd(String flagPd) {
        this.flagPd = flagPd;
    }

    public String getFlagPv() {
        return flagPv;
    }

    public void setFlagPv(String flagPv) {
        this.flagPv = flagPv;
    }

    public Date getDateHap() {
        return dateHap;
    }

    public void setDateHap(Date dateHap) {
        this.dateHap = dateHap;
    }

    public String getPkDeptCg() {
        return pkDeptCg;
    }

    public void setPkDeptCg(String pkDeptCg) {
        this.pkDeptCg = pkDeptCg;
    }

    public String getPkEmpCg() {
        return pkEmpCg;
    }

    public void setPkEmpCg(String pkEmpCg) {
        this.pkEmpCg = pkEmpCg;
    }

    public String getNameEmpCg() {
        return nameEmpCg;
    }

    public void setNameEmpCg(String nameEmpCg) {
        this.nameEmpCg = nameEmpCg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getQuanCg() {
        return quanCg;
    }

    public void setQuanCg(Double quanCg) {
        this.quanCg = quanCg;
    }

    public Double getAmountPi() {
        return amountPi;
    }

    public void setAmountPi(Double amountPi) {
        this.amountPi = amountPi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDtItemtype() {
        return dtItemtype;
    }

    public void setDtItemtype(String dtItemtype) {
        this.dtItemtype = dtItemtype;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getPkEmpEx() {
        return pkEmpEx;
    }

    public void setPkEmpEx(String pkEmpEx) {
        this.pkEmpEx = pkEmpEx;
    }

    public String getNameEmpEx() {
        return nameEmpEx;
    }

    public void setNameEmpEx(String nameEmpEx) {
        this.nameEmpEx = nameEmpEx;
    }

    public String getEuBltype() {
        return euBltype;
    }

    public void setEuBltype(String euBltype) {
        this.euBltype = euBltype;
    }

    public String getPkCnordRl() {
        return pkCnordRl;
    }

    public void setPkCnordRl(String pkCnordRl) {
        this.pkCnordRl = pkCnordRl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNameSupply() {
        return nameSupply;
    }

    public void setNameSupply(String nameSupply) {
        this.nameSupply = nameSupply;
    }

    public String getNameFreq() {
        return nameFreq;
    }

    public void setNameFreq(String nameFreq) {
        this.nameFreq = nameFreq;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUnitDos() {
        return unitDos;
    }

    public void setUnitDos(String unitDos) {
        this.unitDos = unitDos;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public double getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(double priceCost) {
        this.priceCost = priceCost;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getPkUnitPd() {
        return pkUnitPd;
    }

    public void setPkUnitPd(String pkUnitPd) {
        this.pkUnitPd = pkUnitPd;
    }
}
