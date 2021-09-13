package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPayDetailItem {

    @XmlElement(name = "RegFlow")
    private String regFlow;

    @XmlElement(name = "RecipeSEQ")
    private String recipeSEQ;

    @XmlElement(name = "Num")
    private String num;

    @XmlElement(name = "Code")
    private String code;

    @XmlElement(name = "Name")
    private String itemName;

    @XmlElement(name = "Unit")
    private String unitName;

    @XmlElement(name = "Spec")
    private String spec;

    @XmlElement(name = "Price")
    private Double price;

    @XmlElement(name = "Quantity")
    private Double quantity;

    @XmlElement(name = "Cost")
    private Double cost;

    @XmlElement(name = "Remark")
    private String remark;

    /** 收费项目中心编码	中心编码（医保用）*/
    @XmlElement(name = "CenterCode")
    private String centerCode;

    /** 收费项目中心名称	中心名称（医保用）*/
    @XmlElement(name = "CenterName")
    private String centerName;

    /** 费用类别	医保用*/
    @XmlElement(name = "FeeType")
    private String feeType;

    @XmlTransient
    private String flagPv;
    @XmlTransient
    private String pkItem;
    @XmlTransient
    private String pkCgop;

    @XmlRootElement(name = "Response")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class QueryToPayResult {

        @XmlElement(name = "ResultCode")
        public String resultCode;

        @XmlElement(name = "ErrorMsg")
        public String errorMsg;

        @XmlElement(name = "List")
        public QueryToPayList list;
    }

    @XmlRootElement(name = "Item")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class QueryToPayList {
        @XmlElement(name = "Item")
        public List<QueryToPayDetailItem> objectList;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getRecipeSEQ() {
        return recipeSEQ;
    }

    public void setRecipeSEQ(String recipeSEQ) {
        this.recipeSEQ = recipeSEQ;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFlagPv() {
        return flagPv;
    }

    public void setFlagPv(String flagPv) {
        this.flagPv = flagPv;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getPkCgop() {
        return pkCgop;
    }

    public void setPkCgop(String pkCgop) {
        this.pkCgop = pkCgop;
    }
}

