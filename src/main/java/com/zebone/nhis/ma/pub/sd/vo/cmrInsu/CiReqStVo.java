package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

public class CiReqStVo {

    /**
     * 就诊流水号
     */
    private String medicalNum;

    /**
     * 单据号
     */
    private String billNum;

    /**
     * 发票号
     */
    private String invoiceNO;

    /**
     * 业务流水号
     */
    private String businessSerialNum;

    /**
     * 结算处方关联流水号
     */
    private String relationNum;

    /**
     * 医疗类别
     */
    private String medicalType;

    /**
     * 结算日期
     */
    private String settleDate;

    /**
     * 出院日期
     */
    private String dischDate;

    /**
     * 出院原因
     */
    private String dischCause;

    /**
     * 住院天数
     */
    private int hospitalDay;

    /**
     * 出院临床诊断
     */
    private String dischClinicalDiagnosis;

    /**
     * 出院诊断ICD列表
     */
    private List<CiIcdVo> dischDiagnosisList;

    /**
     * 统筹区代码
     */
    private String areaCode;

    /**
     * 统筹区名称
     */
    private String areaName;

    /**
     * 参保医疗险种
     */
    private String medicalInsurance;

    /**
     * 医疗人员类别
     * 客户医保身份，如“在职”、“退休”，如果是非医保或者自费就医患者请传入“自费”
     */
    private String personnelCat;

    /**
     * 是否进行医保结算
     */
    private String isMedicalInsuranceSettlement;

    /**
     * 是否按病种付费
     */
    private String isPaidByDiagnosis;

    /**
     * 费用总额
     */
    private Double sumMoney;

    /**
     * 乙类自负金额
     */
    private Double selfCareAmount;

    /**
     * 自费金额
     */
    private Double selfAmount;

    /**
     * 符合医保费用
     */
    private Double inInsureMoney;

    /**
     * 医保基金
     */
    private Double medicareFundCost;

    /**
     * 医保起付线
     */
    private Double medicarePayLine;

    /**
     * 医院负担
     */
    private Double hosBearMoney;

    /**
     * 转诊先自付
     */
    private Double priorBurdenMoney;
    /**
     * 统筹基金支付
     */
    private Double fundMoney;
    /**
     * 公务员基金支付
     */
    private Double civilServantFundMoney;
    /**
     * 大病基金支付
     */
    private Double seriousFundMoney;

    /**
     * 账户支付
     */
    private Double accountFundMoney;

    /**
     * 民政救助支付
     */
    private Double civilSubsidy;
    /**
     * 商保支付
     */
    private Double commercialInsurancePayment;

    /**
     * 其他基金支付
     */
    private Double otherFundMoney;
    /**
     * 本次现金支付
     */
    private Double cashMoney;

    /**
     * 医保原始费用列表
     */
    private List<CiMedFeeVo> medFeeList;

    /**
     * 发票费用列表
     */
    private List<CiInvoiceFee> invoiceFeeList;


    /**
     * 经办人
     */
    private String updateBy;

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
    }

    public String getBusinessSerialNum() {
        return businessSerialNum;
    }

    public void setBusinessSerialNum(String businessSerialNum) {
        this.businessSerialNum = businessSerialNum;
    }

    public String getRelationNum() {
        return relationNum;
    }

    public void setRelationNum(String relationNum) {
        this.relationNum = relationNum;
    }

    public String getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(String medicalType) {
        this.medicalType = medicalType;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getDischDate() {
        return dischDate;
    }

    public void setDischDate(String dischDate) {
        this.dischDate = dischDate;
    }

    public String getDischCause() {
        return dischCause;
    }

    public void setDischCause(String dischCause) {
        this.dischCause = dischCause;
    }

    public int getHospitalDay() {
        return hospitalDay;
    }

    public void setHospitalDay(int hospitalDay) {
        this.hospitalDay = hospitalDay;
    }

    public String getDischClinicalDiagnosis() {
        return dischClinicalDiagnosis;
    }

    public void setDischClinicalDiagnosis(String dischClinicalDiagnosis) {
        this.dischClinicalDiagnosis = dischClinicalDiagnosis;
    }

    public List<CiIcdVo> getDischDiagnosisList() {
        return dischDiagnosisList;
    }

    public void setDischDiagnosisList(List<CiIcdVo> dischDiagnosisList) {
        this.dischDiagnosisList = dischDiagnosisList;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(String medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public String getPersonnelCat() {
        return personnelCat;
    }

    public void setPersonnelCat(String personnelCat) {
        this.personnelCat = personnelCat;
    }

    public String getIsMedicalInsuranceSettlement() {
        return isMedicalInsuranceSettlement;
    }

    public void setIsMedicalInsuranceSettlement(String isMedicalInsuranceSettlement) {
        this.isMedicalInsuranceSettlement = isMedicalInsuranceSettlement;
    }

    public String getIsPaidByDiagnosis() {
        return isPaidByDiagnosis;
    }

    public void setIsPaidByDiagnosis(String isPaidByDiagnosis) {
        this.isPaidByDiagnosis = isPaidByDiagnosis;
    }

    public Double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Double getSelfCareAmount() {
        return selfCareAmount;
    }

    public void setSelfCareAmount(Double selfCareAmount) {
        this.selfCareAmount = selfCareAmount;
    }

    public Double getSelfAmount() {
        return selfAmount;
    }

    public void setSelfAmount(Double selfAmount) {
        this.selfAmount = selfAmount;
    }

    public Double getInInsureMoney() {
        return inInsureMoney;
    }

    public void setInInsureMoney(Double inInsureMoney) {
        this.inInsureMoney = inInsureMoney;
    }

    public Double getMedicareFundCost() {
        return medicareFundCost;
    }

    public void setMedicareFundCost(Double medicareFundCost) {
        this.medicareFundCost = medicareFundCost;
    }

    public Double getMedicarePayLine() {
        return medicarePayLine;
    }

    public void setMedicarePayLine(Double medicarePayLine) {
        this.medicarePayLine = medicarePayLine;
    }

    public Double getHosBearMoney() {
        return hosBearMoney;
    }

    public void setHosBearMoney(Double hosBearMoney) {
        this.hosBearMoney = hosBearMoney;
    }

    public Double getPriorBurdenMoney() {
        return priorBurdenMoney;
    }

    public void setPriorBurdenMoney(Double priorBurdenMoney) {
        this.priorBurdenMoney = priorBurdenMoney;
    }

    public Double getFundMoney() {
        return fundMoney;
    }

    public void setFundMoney(Double fundMoney) {
        this.fundMoney = fundMoney;
    }

    public Double getCivilServantFundMoney() {
        return civilServantFundMoney;
    }

    public void setCivilServantFundMoney(Double civilServantFundMoney) {
        this.civilServantFundMoney = civilServantFundMoney;
    }

    public Double getSeriousFundMoney() {
        return seriousFundMoney;
    }

    public void setSeriousFundMoney(Double seriousFundMoney) {
        this.seriousFundMoney = seriousFundMoney;
    }

    public Double getAccountFundMoney() {
        return accountFundMoney;
    }

    public void setAccountFundMoney(Double accountFundMoney) {
        this.accountFundMoney = accountFundMoney;
    }

    public Double getCivilSubsidy() {
        return civilSubsidy;
    }

    public void setCivilSubsidy(Double civilSubsidy) {
        this.civilSubsidy = civilSubsidy;
    }

    public Double getCommercialInsurancePayment() {
        return commercialInsurancePayment;
    }

    public void setCommercialInsurancePayment(Double commercialInsurancePayment) {
        this.commercialInsurancePayment = commercialInsurancePayment;
    }

    public Double getOtherFundMoney() {
        return otherFundMoney;
    }

    public void setOtherFundMoney(Double otherFundMoney) {
        this.otherFundMoney = otherFundMoney;
    }

    public Double getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(Double cashMoney) {
        this.cashMoney = cashMoney;
    }

    public List<CiMedFeeVo> getMedFeeList() {
        return medFeeList;
    }

    public void setMedFeeList(List<CiMedFeeVo> medFeeList) {
        this.medFeeList = medFeeList;
    }

    public List<CiInvoiceFee> getInvoiceFeeList() {
        return invoiceFeeList;
    }

    public void setInvoiceFeeList(List<CiInvoiceFee> invoiceFeeList) {
        this.invoiceFeeList = invoiceFeeList;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
