package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiUploadDtl {

    /**
     *就诊流水号
     *
     */
    private String medicalNum;

    /**
     *目录类别
     *数据字典：
     * 1-药品
     * 2-诊疗项目
     * 3-服务设施
     * 4-医用材料
     *
     */
    private String listCat;

    /**
     *医院收费类别编码
     */
    private String hospitalItemCatCode;

    /**
     *医院收费类别名
     *
     */
    private String hospitalItemCatName;

    /**
     *医保收费类别编码
     *
     */
    private String medicalItemCatCode;

    /**
     *医保收费类别名
     *
     */
    private String medicalItemCatName;

    /**
     *平台收费类别
     *数据字典：
     * 平台收费类别
     */
    private String platformItemCat;

    /**
     *收费项目等级
     *
     */
    private String feesGrade;

    /**
     *处方号
     *
     */
    private String recipeNum;

    /**
     *处方流水号
     *
     */
    private String recipeSerialNum;

    /**
     *处方日期
     *YYYYMMDDHH24MISS
     */
    private String recipeDate;

    /**
     *结算处方关联流水号
     *
     */
    private String relationNum;

    /**
     *医院收费项目编码
     *
     */
    private String hospitalChargeCode;

    /**
     *医院收费项目名
     *
     */
    private String hospitalChargeName;

    /**
     *医保收费项目编码
     *
     */
    private String medicareFeeitemCode;

    /**
     *医保收费项目名
     *
     */
    private String medicareFeeitemName;

    /**
     *自付比例
     *
     */
    private Double selfPayRatio;

    /**
     *医保限价
     *
     */
    private Double medlimitedPrice;

    /**
     *单价
     *
     */
    private Double price;

    /**
     *数量
     *
     */
    private Double quantity;

    /**
     *金额
     *
     */
    private Double money;

    /**
     *医院负担费用
     *
     */
    private Double hosBearMoney;

    /**
     *剂型
     *
     */
    private String formulation;

    /**
     *规格
     *
     */
    private String spec;

    /**
     *科室编码
     *
     */
    private String deptNum;

    /**
     *科室名称
     *
     */
    private String deptName;

    /**
     *处方医生编码
     *
     */
    private String doctorCode;

    /**
     *处方医生姓名
     *
     */
    private String doctorName;

    /**
     *经办人
     *
     */
    private String updateBy;

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum;
    }

    public String getListCat() {
        return listCat;
    }

    public void setListCat(String listCat) {
        this.listCat = listCat;
    }

    public String getHospitalItemCatCode() {
        return hospitalItemCatCode;
    }

    public void setHospitalItemCatCode(String hospitalItemCatCode) {
        this.hospitalItemCatCode = hospitalItemCatCode;
    }

    public String getHospitalItemCatName() {
        return hospitalItemCatName;
    }

    public void setHospitalItemCatName(String hospitalItemCatName) {
        this.hospitalItemCatName = hospitalItemCatName;
    }

    public String getMedicalItemCatCode() {
        return medicalItemCatCode;
    }

    public void setMedicalItemCatCode(String medicalItemCatCode) {
        this.medicalItemCatCode = medicalItemCatCode;
    }

    public String getMedicalItemCatName() {
        return medicalItemCatName;
    }

    public void setMedicalItemCatName(String medicalItemCatName) {
        this.medicalItemCatName = medicalItemCatName;
    }

    public String getPlatformItemCat() {
        return platformItemCat;
    }

    public void setPlatformItemCat(String platformItemCat) {
        this.platformItemCat = platformItemCat;
    }

    public String getFeesGrade() {
        return feesGrade;
    }

    public void setFeesGrade(String feesGrade) {
        this.feesGrade = feesGrade;
    }

    public String getRecipeNum() {
        return recipeNum;
    }

    public void setRecipeNum(String recipeNum) {
        this.recipeNum = recipeNum;
    }

    public String getRecipeSerialNum() {
        return recipeSerialNum;
    }

    public void setRecipeSerialNum(String recipeSerialNum) {
        this.recipeSerialNum = recipeSerialNum;
    }

    public String getRecipeDate() {
        return recipeDate;
    }

    public void setRecipeDate(String recipeDate) {
        this.recipeDate = recipeDate;
    }

    public String getRelationNum() {
        return relationNum;
    }

    public void setRelationNum(String relationNum) {
        this.relationNum = relationNum;
    }

    public String getHospitalChargeCode() {
        return hospitalChargeCode;
    }

    public void setHospitalChargeCode(String hospitalChargeCode) {
        this.hospitalChargeCode = hospitalChargeCode;
    }

    public String getHospitalChargeName() {
        return hospitalChargeName;
    }

    public void setHospitalChargeName(String hospitalChargeName) {
        this.hospitalChargeName = hospitalChargeName;
    }

    public String getMedicareFeeitemCode() {
        return medicareFeeitemCode;
    }

    public void setMedicareFeeitemCode(String medicareFeeitemCode) {
        this.medicareFeeitemCode = medicareFeeitemCode;
    }

    public String getMedicareFeeitemName() {
        return medicareFeeitemName;
    }

    public void setMedicareFeeitemName(String medicareFeeitemName) {
        this.medicareFeeitemName = medicareFeeitemName;
    }

    public Double getSelfPayRatio() {
        return selfPayRatio;
    }

    public void setSelfPayRatio(Double selfPayRatio) {
        this.selfPayRatio = selfPayRatio;
    }

    public Double getMedlimitedPrice() {
        return medlimitedPrice;
    }

    public void setMedlimitedPrice(Double medlimitedPrice) {
        this.medlimitedPrice = medlimitedPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getHosBearMoney() {
        return hosBearMoney;
    }

    public void setHosBearMoney(Double hosBearMoney) {
        this.hosBearMoney = hosBearMoney;
    }

    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(String deptNum) {
        this.deptNum = deptNum;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
