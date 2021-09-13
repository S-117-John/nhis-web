package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

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
    
}
