package com.zebone.nhis.ma.pub.zsba.vo.outflow;

public class DrugVo {
    /**药品对照id	string		是*/
    private String drugId;
    /**药品对照id 这个是对应bd_pd的code?*/
    private String drugContrastId;
    /**厂商名称*/
    private String supplierName;
    /**药品名称*/
    private String drugName;
    /**规格描述--非必填*/
    private String specDesc;

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc;
    }

    public String getSpecDesc() {
        return specDesc;
    }

    public String getDrugContrastId() {
        return drugContrastId;
    }

    public void setDrugContrastId(String drugContrastId) {
        this.drugContrastId = drugContrastId;
    }
}
