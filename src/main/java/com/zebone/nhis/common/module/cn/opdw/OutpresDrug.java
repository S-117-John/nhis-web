package com.zebone.nhis.common.module.cn.opdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="OUTPRES_DRUG")
public class OutpresDrug extends BaseModule {
    @PK
    @Field(value="PK_DRUG")
    private String pkDrug;

    @Field(value="PK_PD")
    private String pkPd;

    @Field(value="CODE")
    private String code;

    @Field(value="DRUG_ID")
    private String drugId;

    @Field(value="SUPPLIER_NAME")
    private String supplierName;

    @Field(value="NAME")
    private String name;

    @Field(value="SPEC")
    private String spec;

    public String getPkDrug() {
        return pkDrug;
    }

    public void setPkDrug(String pkDrug) {
        this.pkDrug = pkDrug == null ? null : pkDrug.trim();
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd == null ? null : pkPd.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }
}