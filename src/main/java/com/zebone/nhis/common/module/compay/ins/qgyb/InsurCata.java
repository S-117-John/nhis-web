package com.zebone.nhis.common.module.compay.ins.qgyb;

public class InsurCata {
    //主键
    private String pkInsItem;
    //目录类型代码
    private String itemTypeName;
    //目录代码
    private String insItemCode;
    //目录名称
    private String insItemName;
    //价格
    private String price;
    //自付比例
    private Double ratioSelf;
    //规格
    private String spec;
    //批准文号
    private String apprNo;
    //厂商名称
    private String factoryName;
    //剂型
    private String doseType;
    //目录类别
    private String listType;
    //药品本位码
    private String codeStd;
    
    //旧医保编码（省编码）
    private String oldCode;

    //医保类型
    private String insType;

    //甲乙类
    private String classType;

    //特殊限价药品标志
    private String fixedPriceFlag;
    //特殊限价药品标志
    private String specialDrugsFlag;
    //药品注册证号
    private String DrugRegNo;
    //药品注册证号开始日期
    private String DrugRegStartTime;
    //药品注册证号结束日期
    private String DrugRegEndTime;
    //限制用药标志
    private String fagFit;
    //限制使用描述
    private String descFit;

    public String getFixedPriceFlag() {
        return fixedPriceFlag;
    }

    public void setFixedPriceFlag(String fixedPriceFlag) {
        this.fixedPriceFlag = fixedPriceFlag;
    }
    public String getSpecialDrugsFlag() {
        return specialDrugsFlag;
    }

    public void setSpecialDrugsFlag(String specialDrugsFlag) {
        this.specialDrugsFlag = specialDrugsFlag;
    }
    public String getDrugRegNo() {
        return DrugRegNo;
    }

    public void setDrugRegNo(String drugRegNo) {
        DrugRegNo = drugRegNo;
    }
    public String getDrugRegStartTime() {
        return DrugRegStartTime;
    }

    public void setDrugRegStartTime(String drugRegStartTime) {
        DrugRegStartTime = drugRegStartTime;
    }
    public String getDrugRegEndTime() {
        return DrugRegEndTime;
    }

    public void setDrugRegEndTime(String drugRegEndTime) {
        DrugRegEndTime = drugRegEndTime;
    }

    public String getFagFit() {
        return fagFit;
    }

    public void setFagFit(String fagFit) {
        this.fagFit = fagFit;
    }
    public String getDescFit() {
        return descFit;
    }

    public void setDescFit(String descFit) {
        this.descFit = descFit;
    }
    public String getPkInsItem() {
        return pkInsItem;
    }

    public void setPkInsItem(String pkInsItem) {
        this.pkInsItem = pkInsItem;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getInsItemCode() {
        return insItemCode;
    }

    public void setInsItemCode(String insItemCode) {
        this.insItemCode = insItemCode;
    }

    public String getInsItemName() {
        return insItemName;
    }

    public void setInsItemName(String insItemName) {
        this.insItemName = insItemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


	public Double getRatioSelf() {
		return ratioSelf;
	}

	public void setRatioSelf(Double ratioSelf) {
		this.ratioSelf = ratioSelf;
	}

	public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getApprNo() {
        return apprNo;
    }

    public void setApprNo(String apprNo) {
        this.apprNo = apprNo;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

	public String getCodeStd() {
		return codeStd;
	}

	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}
    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }
}
