package com.zebone.nhis.common.module.compay.ins.qgyb;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;

import java.util.Date;

public class MedicalCharges {
    private String pkItem;//HIS项目主键
    private String code;//HIS项目编码
    private String name;//中文名称
    private String nameEn;//英文名称（只针对药品）
    private String mnemonic;//助记码
    private String dtItem;//收费项目分类
    private String price;//标准价格
    private String unit;//单位
    private String doseType;//剂型（只针对药品）
    private String factoryName;//生产厂家
    private String apprNo;//批准文号
    private String dosage;//每次用量（只针对药品）
    private String frequency;// 使用频次（只针对药品）
    private String usage;//用法（只针对药品）
    private String spec;//规格（只针对药品）
    private String flagPre;// 处方药标识（只针对药品）
    private String flagSpec;// 限制类用药标识（只针对药品）
    private String state;//审核状态
    private String hisType;//目录类别
    private Date createTime;//创建时间
    private String creator;//创建人
    private String oldCode;//旧医保编码/省编码
    /**
     * 查询对照信息
     */
    private String pkItemmap;//医保对照主键
    private String hisItemCode;//his对照项目编码
    private String hisItemName;//his对照项目名称
    private String insItemCode;//医保对照项目编码
    private String insItemName;//医保对照项目名称
    @Field(value = "UPLOAD_DATE", date = FieldType.INSERT)
	private Date uploadDate;//上传时间

    private String fagFit;//限制用药标志
    private String descFit;//限制使用描述
    private String classType;//甲乙类
    private String dateBegin;//开始时间
    private String dateEnd;//结束时间

    private String fixedPriceFlag;//特殊限价药品标志
    private String specialDrugsFlag;//特殊限价药品标志
    private String DrugRegNo;//药品注册证号
    private String DrugRegStartTime;//药品注册证号开始日期
    private String DrugRegEndTime;//药品注册证号结束日期



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

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }
    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getDtItem() {
        return dtItem;
    }

    public void setDtItem(String dtItem) {
        this.dtItem = dtItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getApprNo() {
        return apprNo;
    }

    public void setApprNo(String apprNo) {
        this.apprNo = apprNo;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getSpec() {
        return spec;
    }

    public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFlagPre() {
        return flagPre;
    }

    public void setFlagPre(String flagPre) {
        this.flagPre = flagPre;
    }

    public String getFlagSpec() {
        return flagSpec;
    }

    public void setFlagSpec(String flagSpec) {
        this.flagSpec = flagSpec;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHisItemCode() {
        return hisItemCode;
    }

    public void setHisItemCode(String hisItemCode) {
        this.hisItemCode = hisItemCode;
    }

    public String getHisItemName() {
        return hisItemName;
    }

    public void setHisItemName(String hisItemName) {
        this.hisItemName = hisItemName;
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

    public String getPkItemmap() {
        return pkItemmap;
    }

    public void setPkItemmap(String pkItemmap) {
        this.pkItemmap = pkItemmap;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getHisType() {
        return hisType;
    }

    public void setHisType(String hisType) {
        this.hisType = hisType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
    
}
