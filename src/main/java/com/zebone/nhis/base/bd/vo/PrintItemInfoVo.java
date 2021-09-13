package com.zebone.nhis.base.bd.vo;

public class PrintItemInfoVo {


    private String ordName;
    
    private String code;
    

	private String countPrice;

    private String codeStd;

    private String itemName;

    private String price;

    private String unitName;

    private String itemCateName;

    private Double quan;

    private String ordTypName;

    private String nameDept;

    private String flagActive;

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    public String getOrdName() {
        return ordName;
    }

    public void setOrdName(String ordName) {
        this.ordName = ordName;
    }

    public String getCountPrice() {
        return countPrice;
    }

    public void setCountPrice(String countPrice) {
        this.countPrice = countPrice;
    }

    public String getCodeStd() {
        return codeStd;
    }

    public void setCodeStd(String codeStd) {
        this.codeStd = codeStd;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getItemCateName() {
        return itemCateName;
    }

    public void setItemCateName(String itemCateName) {
        this.itemCateName = itemCateName;
    }

    public Double getQuan() {
        return quan;
    }

    public void setQuan(Double quan) {
        this.quan = quan;
    }

    public String getOrdTypName() {
        return ordTypName;
    }

    public void setOrdTypName(String ordTypName) {
        this.ordTypName = ordTypName;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getFlagActive() {
        return flagActive;
    }

    public void setFlagActive(String flagActive) {
        this.flagActive = flagActive;
    }
}
