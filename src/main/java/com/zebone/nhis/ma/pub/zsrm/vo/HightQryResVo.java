package com.zebone.nhis.ma.pub.zsrm.vo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 高值耗材条码返回值
 */
public class HightQryResVo {
    @JsonProperty("BAR_CODE")
    private String barCode;

    @JsonProperty("MATE_CODE")
    private String mateCode;

    @JsonProperty("MATE_NAME")
    private String mateName;

    @JsonProperty("MATE_SPEC")
    private String mateSpec;

    @JsonProperty("MATE_UNIT")
    private String mateUnit;

    @JsonProperty("MANUFACTURER_NAME")
    private String manufacturerName;

    @JsonProperty("PRODUCTION_BATCH")
    private String productionBatch;

    @JsonProperty("PRODUCTION_DATE")
    private String productionDate;

    @JsonProperty("EXPIRE_DATE")
    private String expireDate;

    @JsonProperty("CHARGE_ITEM_CODE")
    private String chargeItemCode;

    @JsonProperty("CHARGE_ITEM_NAME")
    private String chargeItemName;

    @JsonProperty("CHARGE_ITEM_UNIT")
    private String chargeItemUnit;

    @JsonProperty("CHARGE_PRICE")
    private Double chargePrice;

    @JsonProperty("BAR_STATE")
    private String barState;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMateCode() {
        return mateCode;
    }

    public void setMateCode(String mateCode) {
        this.mateCode = mateCode;
    }

    public String getMateName() {
        return mateName;
    }

    public void setMateName(String mateName) {
        this.mateName = mateName;
    }

    public String getMateSpec() {
        return mateSpec;
    }

    public void setMateSpec(String mateSpec) {
        this.mateSpec = mateSpec;
    }

    public String getMateUnit() {
        return mateUnit;
    }

    public void setMateUnit(String mateUnit) {
        this.mateUnit = mateUnit;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getChargeItemCode() {
        return chargeItemCode;
    }

    public void setChargeItemCode(String chargeItemCode) {
        this.chargeItemCode = chargeItemCode;
    }

    public String getChargeItemName() {
        return chargeItemName;
    }

    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    public String getChargeItemUnit() {
        return chargeItemUnit;
    }

    public void setChargeItemUnit(String chargeItemUnit) {
        this.chargeItemUnit = chargeItemUnit;
    }

    public Double getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(Double chargePrice) {
        this.chargePrice = chargePrice;
    }

    public String getBarState() {
        return barState;
    }

    public void setBarState(String barState) {
        this.barState = barState;
    }
}
