package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class MachItem {

    @XmlRootElement(name="DocumentElement")
    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class MachItemHeader {

        @XmlElement(name = "DataTable")
        private List<MachItem> items;

        public List<MachItem> getItems() {
            return items;
        }

        public void setItems(List<MachItem> items) {
            this.items = items;
        }
    }


    @XmlElement(name = "OrderNumber")
    private String orderNumber;
    @XmlElement(name = "Yp_id")
    private String ypId;
    @XmlElement(name = "Amount")
    private Integer amount;
    @XmlElement(name = "Frequency")
    private String frequency;
    @XmlElement(name = "Usage")
    private String usage;
    @XmlElement(name = "Dosage")
    private BigDecimal dosage;
    @XmlElement(name = "Dosage_Unit")
    private String dosageUnit;
    @XmlElement(name = "OrderItem")
    private String orderItem;
    @XmlElement(name = "Order_Item_Price")
    private Double orderItemPrice;
    @XmlElement(name = "HighRisk_Indicator")
    private String highRiskIndicator;
    @XmlElement(name = "Comment")
    private String comment;

    @XmlTransient
    private Double quanCg;
    @XmlTransient
    private Integer packSize;
    @XmlTransient
    private String spc;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getYpId() {
        return ypId;
    }

    public void setYpId(String ypId) {
        this.ypId = ypId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public BigDecimal getDosage() {
        return dosage;
    }

    public void setDosage(BigDecimal dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public Double getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(Double orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public String getHighRiskIndicator() {
        return highRiskIndicator;
    }

    public void setHighRiskIndicator(String highRiskIndicator) {
        this.highRiskIndicator = highRiskIndicator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getQuanCg() {
        return quanCg;
    }

    public void setQuanCg(Double quanCg) {
        this.quanCg = quanCg;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }
}
