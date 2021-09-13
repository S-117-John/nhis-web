package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * @author lijin
 * 物资材料项目消息模型
 */
public class MaterialItem {

    @MetadataDescribe(id= "LHDE0012001",name = "物资ID",eName = "MATERIAL_ID")
    private String materialId;

    @MetadataDescribe(id= "LHDE0012002",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0012003",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0012004",name = "物资编码",eName = "MATERIAL_CODE")
    private String materialCode;

    @MetadataDescribe(id= "LHDE0012005",name = "物资名称",eName = "MATERIAL_NAME")
    private String materialName;

    @MetadataDescribe(id= "LHDE0012006",name = "物资类别代码",eName = "MATERIAL_TYPE_CODE")
    private String materialTypeCode;

    @MetadataDescribe(id= "LHDE0012007",name = "物资类别名称",eName = "MATERIAL_TYPE_NAME")
    private String materialTypeName;

    @MetadataDescribe(id= "LHDE0012008",name = "规格",eName = "SPECS")
    private String specs;

    @MetadataDescribe(id= "LHDE0012009",name = "物资型号",eName = "MATERIAL_MODEL")
    private String materialModel;

    @MetadataDescribe(id= "LHDE0012010",name = "计量单位",eName = "UNIT")
    private String unit;

    @MetadataDescribe(id= "LHDE0012011",name = "物资品牌",eName = "MATERIAL_BRAND")
    private String materialBrand;

    @MetadataDescribe(id= "LHDE0012012",name = "国产/进口",eName = "DOMESTIC_IMPORTED")
    private String domesticImported;

    @MetadataDescribe(id= "LHDE0012013",name = "别名",eName = "ALIAS")
    private String alias;

    @MetadataDescribe(id= "LHDE0012014",name = "生产厂商",eName = "PRODUCER_NAME")
    private String producerName;

    @MetadataDescribe(id= "LHDE0012015",name = "是否收费",eName = "IS_CHARGE")
    private String isCharge;

    @MetadataDescribe(id= "LHDE0012016",name = "是否打包",eName = "IS_PACK")
    private String isPack;

    @MetadataDescribe(id= "LHDE0012017",name = "是否高值",eName = "IS_HIGHVALUE")
    private String isHighvalue;

    @MetadataDescribe(id= "LHDE0012018",name = "收费项目代码",eName = "CHARGE_ITEM_CODE")
    private String chargeItemCode;

    @MetadataDescribe(id= "LHDE0012019",name = "收费项目名称",eName = "CHARGE_ITEM_NAME")
    private String chargeItemName;

    @MetadataDescribe(id= "LHDE0012020",name = "财务分类代码",eName = "ACCOUNT_CLASS_CODE")
    private String accountClassCode;

    @MetadataDescribe(id= "LHDE0012021",name = "财务分类名称",eName = "ACCOUNT_CLASS_NAME")
    private String accountClassName;

    @MetadataDescribe(id= "LHDE0012022",name = "有效性标志",eName = "VALID_STATE")
    private String validState;


    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialTypeCode() {
        return materialTypeCode;
    }

    public void setMaterialTypeCode(String materialTypeCode) {
        this.materialTypeCode = materialTypeCode;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMaterialBrand() {
        return materialBrand;
    }

    public void setMaterialBrand(String materialBrand) {
        this.materialBrand = materialBrand;
    }

    public String getDomesticImported() {
        return domesticImported;
    }

    public void setDomesticImported(String domesticImported) {
        this.domesticImported = domesticImported;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(String isCharge) {
        this.isCharge = isCharge;
    }

    public String getIsPack() {
        return isPack;
    }

    public void setIsPack(String isPack) {
        this.isPack = isPack;
    }

    public String getIsHighvalue() {
        return isHighvalue;
    }

    public void setIsHighvalue(String isHighvalue) {
        this.isHighvalue = isHighvalue;
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

    public String getAccountClassCode() {
        return accountClassCode;
    }

    public void setAccountClassCode(String accountClassCode) {
        this.accountClassCode = accountClassCode;
    }

    public String getAccountClassName() {
        return accountClassName;
    }

    public void setAccountClassName(String accountClassName) {
        this.accountClassName = accountClassName;
    }

    public String getValidState() {
        return validState;
    }

    public void setValidState(String validState) {
        this.validState = validState;
    }
}
