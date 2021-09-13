package com.zebone.nhis.scm.pub.vo;

import java.util.Date;
import java.util.Objects;

/**
 * 批次Vo
 */
public class MtlPdBatchVo {

    private String pkPd;
    private double price;
    private double priceCost;
    private String batchNo;
    private Date dateExpire;

    public MtlPdBatchVo(){}

    public MtlPdBatchVo(String pkPd, double price, double priceCost, String batchNo, Date dateExpire) {
        this.pkPd = pkPd;
        this.price = price;
        this.priceCost = priceCost;
        this.batchNo = batchNo;
        this.dateExpire = dateExpire;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(double priceCost) {
        this.priceCost = priceCost;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtlPdBatchVo batchVo = (MtlPdBatchVo) o;
        return Double.compare(batchVo.getPrice(), getPrice()) == 0 &&
                Double.compare(batchVo.getPriceCost(), getPriceCost()) == 0 &&
                Objects.equals(getPkPd(),batchVo.getPkPd()) &&
                Objects.equals(getBatchNo(), batchVo.getBatchNo()) &&
                Objects.equals(getDateExpire(), batchVo.getDateExpire());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPkPd(), getPrice(), getPriceCost(), getBatchNo(), getDateExpire());
    }

}
