package com.zebone.nhis.webservice.lbzy.model.ipin;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryInHospitalDetail {

    @XmlTransient
    private String pkPv;

    @XmlElement(name = "ItemType")
    private String itemType;
    @XmlElement(name = "ItemCode")
    private String itemCode;
    @XmlElement(name = "ItemName")
    private String itemName;
    @XmlElement(name = "Specification")
    private String spec;
    @XmlElement(name = "Unit")
    private String unit;
    @XmlElement(name = "Quantity")
    private String quantity;
    @XmlElement(name = "Price")
    private Double price;

    @XmlElement(name = "PactCode")
    private String pactCode;

    @XmlElement(name = "PactName")
    private String pactName;

    @XmlElement(name = "FeeType")
    private String feeType;

    @XmlElement(name = "FeeDate")
    private String feeDate;

    @XmlElement(name = "TotalCost")
    private Double totalCost;

    @XmlElement(name = "PubCost")
    private Double pubCost;

    @XmlElement(name = "Paycost")
    private Double payCost;

    @XmlElement(name = "OwnCost")
    private Double ownCost;

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPactCode() {
        return pactCode;
    }

    public void setPactCode(String pactCode) {
        this.pactCode = pactCode;
    }

    public String getPactName() {
        return pactName;
    }

    public void setPactName(String pactName) {
        this.pactName = pactName;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getPubCost() {
        return pubCost;
    }

    public void setPubCost(Double pubCost) {
        this.pubCost = pubCost;
    }

    public Double getPayCost() {
        return payCost;
    }

    public void setPayCost(Double payCost) {
        this.payCost = payCost;
    }

    public Double getOwnCost() {
        return ownCost;
    }

    public void setOwnCost(Double ownCost) {
        this.ownCost = ownCost;
    }

    public String getFeeDate() {
        return feeDate;
    }

    public void setFeeDate(String feeDate) {
        this.feeDate = feeDate;
    }
}
