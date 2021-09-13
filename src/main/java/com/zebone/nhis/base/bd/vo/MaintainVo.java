package com.zebone.nhis.base.bd.vo;

import java.util.Date;

public class MaintainVo {

    //主键
    private String pkItem ;

    private String dtItemtype ;

    //类型名称
    private String itemtype ;

    //编码
    private String code ;

    //项目名称
    private String name ;

    //规格
    private String spec ;

    //单位
    private String unit ;

    //价格
    private String price ;

    //拼音码
    private String spcode ;

    //数量
    private int quan ;

    //计费套餐主键（新增的行是null的）
    private String pkCgset ;

    private String pkOrg ;

    private String pkDept ;

    private String euItemtype ;

    private Date ts ;

    //用量
    private Double dosage;

    //用量单位
    private String unitDos;

    //用法
    private String nameSupply;

    //频次
    private String nameFreq;


    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public String getUnitDos() {
        return unitDos;
    }

    public void setUnitDos(String unitDos) {
        this.unitDos = unitDos;
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


    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getDtItemtype() {
        return dtItemtype;
    }

    public void setDtItemtype(String dtItemtype) {
        this.dtItemtype = dtItemtype;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public int getQuan() {
        return quan;
    }

    public void setQuan(int quan) {
        this.quan = quan;
    }

    public String getPkCgset() {
        return pkCgset;
    }

    public void setPkCgset(String pkCgset) {
        this.pkCgset = pkCgset;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getEuItemtype() {
        return euItemtype;
    }

    public void setEuItemtype(String euItemtype) {
        this.euItemtype = euItemtype;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
