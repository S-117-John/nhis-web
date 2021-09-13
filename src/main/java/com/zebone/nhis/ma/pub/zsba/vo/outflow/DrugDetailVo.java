package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import java.util.List;

public class DrugDetailVo {

    /** 药品ID	string	36	是	平台药品id或对照药品id 用于关联医院和药店药品*/
    private String drugId;
    /** 药品商品名	string	50	是	*/
    private String drugName;

    /** 总价	double	（8,4）	是	*/
    private Double amount;
    /** 用量单位	string	10	是	*/
    private String doseUnit;
    /** 药品编码	string	50	是	药店内部唯一标识*/
    private String drugCode;

    /** 默认药品标识	string	1	是	是否默认药品*/
    private String isDefault;

    /** 外包装数量	string	10	是	*/
    private String packQtyOutter;
    /** 外包装单位	string	10	是*/
    private String packUnitOutter;
    /** 单价	double	（8,4）	是*/
    private Double price;
    /** */
    private String specId;
    /** 用药方法	string	36	是*/
    private String drugPathways;
    /** 用药频次	string	10	是*/
    private String drugRate;
    /** 用量	double	(8,3)	是*/
    private Double drugDose;

    /** 用药天数	int	4	是*/
    private Integer drugDays;

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getPackQtyOutter() {
        return packQtyOutter;
    }

    public void setPackQtyOutter(String packQtyOutter) {
        this.packQtyOutter = packQtyOutter;
    }

    public String getPackUnitOutter() {
        return packUnitOutter;
    }

    public void setPackUnitOutter(String packUnitOutter) {
        this.packUnitOutter = packUnitOutter;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getDrugPathways() {
        return drugPathways;
    }

    public void setDrugPathways(String drugPathways) {
        this.drugPathways = drugPathways;
    }

    public String getDrugRate() {
        return drugRate;
    }

    public void setDrugRate(String drugRate) {
        this.drugRate = drugRate;
    }

    public Double getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(Double drugDose) {
        this.drugDose = drugDose;
    }

    public Integer getDrugDays() {
        return drugDays;
    }

    public void setDrugDays(Integer drugDays) {
        this.drugDays = drugDays;
    }
}
