package com.zebone.nhis.bl.pub.vo;

public class BlIpDtSptVo {

    private String pkCgip;
    private String pkPv;
    private String pkItem;
    private String nameCg;
    private String pkUnit;
    private String spec;
    private Integer quan;
    private String flagDisappear;
    private String note;
    private String pkOpspd;
    private String barcode;
    private String pkDept;
    private String nameDept;
    private String dateCg;
    private String nameUnit;
    private Integer price;
    private Integer amount;

    public String getPkDept() { return pkDept;  }

    public void setPkDept(String pkDept) { this.pkDept = pkDept; }

    public String getDateCg() { return dateCg; }

    public void setDateCg(String dateCg) { this.dateCg = dateCg; }

    public String getPkCgip() {
        return pkCgip;
    }

    public void setPkCgip(String pkCgip) {
        this.pkCgip = pkCgip;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getNameCg() {
        return nameCg;
    }

    public void setNameCg(String nameCg) {
        this.nameCg = nameCg;
    }

    public String getPkUnit() {
        return pkUnit;
    }

    public void setPkUnit(String pkUnit) {
        this.pkUnit = pkUnit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getQuan() {
        return quan;
    }

    public void setQuan(Integer quan) {
        this.quan = quan;
    }

    public String getFlagDisappear() {
        return flagDisappear;
    }

    public void setFlagDisappear(String flagDisappear) {
        this.flagDisappear = flagDisappear;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPkOpspd() {
        return pkOpspd;
    }

    public void setPkOpspd(String pkOpspd) {
        this.pkOpspd = pkOpspd;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNameDept() { return nameDept; }

    public void setNameDept(String nameDept) { this.nameDept = nameDept; }

	public String getNameUnit() {
		return nameUnit;
	}

	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
    
}
