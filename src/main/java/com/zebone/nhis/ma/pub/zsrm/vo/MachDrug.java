package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class MachDrug {

    @XmlElement(name = "编码")
    private String code;
    @XmlElement(name = "大小规格")//00小，01大
    private String bs;
    @XmlElement(name = "内码")
    private String codeISN;
    @XmlElement(name = "名称")
    private String name;
    @XmlElement(name = "商品名称")
    private String nameSp;
    @XmlElement(name = "拼音码")
    private String spcode;
    @XmlElement(name = "五笔码")
    private String dCode;
    @XmlElement(name = "包装数量")
    private Double packSize;
    @XmlElement(name = "包装规格")
    private String spec;
    @XmlElement(name = "厂商")
    private String factory;
    @XmlElement(name = "上机标志")
    private String flagIn;
    @XmlElement(name = "零售价")
    private Double price;
    @XmlElement(name = "包装单位")
    private String unit;
    @XmlElement(name = "删除标志")
    private String flagDel;
    @XmlElement(name = "购入价")
    private Double priceCost;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getCodeISN() {
        return codeISN;
    }

    public void setCodeISN(String codeISN) {
        this.codeISN = codeISN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public Double getPackSize() {
        return packSize;
    }

    public void setPackSize(Double packSize) {
        this.packSize = packSize;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getFlagIn() {
        return flagIn;
    }

    public void setFlagIn(String flagIn) {
        this.flagIn = flagIn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFlagDel() {
        return flagDel;
    }

    public void setFlagDel(String flagDel) {
        this.flagDel = flagDel;
    }

    public Double getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(Double priceCost) {
        this.priceCost = priceCost;
    }

    public String getNameSp() {
        return nameSp;
    }

    public void setNameSp(String nameSp) {
        this.nameSp = nameSp;
    }

    @XmlRootElement(name="DocumentElement")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class MachDrugHeader {

        @XmlElement(name = "DataTable")
        private List<MachDrug> items;

        public List<MachDrug> getItems() {
            return items;
        }

        public void setItems(List<MachDrug> items) {
            this.items = items;
        }
    }
}
