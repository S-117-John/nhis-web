package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Batch {

    private String lotNumber;
    private String expirationDate;

    public Batch() {
    }

    public Batch(String lotNumber, String expirationDate) {
        this.lotNumber = lotNumber;
        this.expirationDate = expirationDate;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}