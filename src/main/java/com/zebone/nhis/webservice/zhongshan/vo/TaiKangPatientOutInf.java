package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author wq
 * @Classname TaiKangPatientOutInf
 * @Description 患者出院信息集合
 * @Date 2020-11-23 18:55
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MedicalInfo")
public class TaiKangPatientOutInf {

    /*
     * 业务流水号_入院日期（拼接）
     * */

    @XmlElement(name = "TreatSerialNo")
    private String codeIpDateAdmit;

    /*
     * 就诊号-业务流水号
     * */

    @XmlElement(name = "PatientNumber")
    private String codeIp;

    /*
     * 入院时间-入院时间
     * */

    @XmlElement(name = "InHosDate")
    private String dateAdmit;

    /*
     * 出院诊断名称-中文
     * */

    @XmlElement(name = "OutHospitalDiagnoses")
    private String outHospitalDiagnoses;
    /*
     *诊治医生
     * */

    @XmlElement(name = "MasterDoctor")

    private String masterDoctor;
    /*
     * 出院日期
     * */

    @XmlElement(name = "LeaveHospitalDate")

    private String leaveHospitalDate;
    /*
     * 起付线
     * */

    @XmlElement(name = "UnderwayCriterion")

    private Double underwayCriterion;
    /*
     * 基本医疗保险金额
     * */

    @XmlElement(name = "BaseInsurance")

    private Double baseInsurance;
    /*
     * 补充医疗赔付金额
     * */

    @XmlElement(name = "ComplementarityInsurance")

    private Double complementarityInsurance;
    /*
     * 其他赔付金额
     * */

    @XmlElement(name = "ElseInsuranceMoney")

    private Double elseInsuranceMoney;
    /*
     * 总费用
     * */

    @XmlElement(name = "TotalMedicalCost")

    private Double totalMedicalCost;
    /*
     * 总自费
     * */

    @XmlElement(name = "InsuranceDeduct")

    private Double insuranceDeduct;
    /*
     * 全自费
     * */

    @XmlElement(name = "Selfamnt")

    private Double selfamnt;
    /*
     * 费用列表 包含了发票信息和二级分类
     * */
    @XmlElementWrapper(name = "SettlementItemList")
    @XmlElement(name = "SettlementItem")

    private List<TaiKangSettlementItem> taiKangSettlementItems;
    /*
    * 结算主键
    * */

    @XmlTransient
    private  String pkSettle;

    public String getcodeIp() {
        return codeIp;
    }

    public void setcodeIp(String codeIp) {
        this.codeIp = codeIp;
    }



    public String getOutHospitalDiagnoses() {
        return outHospitalDiagnoses;
    }

    public void setOutHospitalDiagnoses(String outHospitalDiagnoses) {
        this.outHospitalDiagnoses = outHospitalDiagnoses;
    }

    public String getMasterDoctor() {
        return masterDoctor;
    }

    public void setMasterDoctor(String masterDoctor) {
        this.masterDoctor = masterDoctor;
    }



    public Double getUnderwayCriterion() {
        return underwayCriterion;
    }

    public void setUnderwayCriterion(Double underwayCriterion) {
        this.underwayCriterion = underwayCriterion;
    }

    public Double getBaseInsurance() {
        return baseInsurance;
    }

    public void setBaseInsurance(Double baseInsurance) {
        this.baseInsurance = baseInsurance;
    }

    public Double getComplementarityInsurance() {
        return complementarityInsurance;
    }

    public void setComplementarityInsurance(Double complementarityInsurance) {
        this.complementarityInsurance = complementarityInsurance;
    }

    public Double getElseInsuranceMoney() {
        return elseInsuranceMoney;
    }

    public void setElseInsuranceMoney(Double elseInsuranceMoney) {
        this.elseInsuranceMoney = elseInsuranceMoney;
    }

    public Double getTotalMedicalCost() {
        return totalMedicalCost;
    }

    public void setTotalMedicalCost(Double totalMedicalCost) {
        this.totalMedicalCost = totalMedicalCost;
    }

    public Double getInsuranceDeduct() {
        return insuranceDeduct;
    }

    public void setInsuranceDeduct(Double insuranceDeduct) {
        this.insuranceDeduct = insuranceDeduct;
    }

    public Double getSelfamnt() {
        return selfamnt;
    }

    public void setSelfamnt(Double selfamnt) {
        this.selfamnt = selfamnt;
    }


    public List<TaiKangSettlementItem> getTaiKangSettlementItems() {
        return taiKangSettlementItems;
    }

    public void setTaiKangSettlementItems(List<TaiKangSettlementItem> taiKangSettlementItems) {
        this.taiKangSettlementItems = taiKangSettlementItems;
    }

    public String getCodeIpDateAdmit() {
        return codeIpDateAdmit;
    }

    public void setCodeIpDateAdmit(String codeIpDateAdmit) {
        this.codeIpDateAdmit = codeIpDateAdmit;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getLeaveHospitalDate() {
        return leaveHospitalDate;
    }

    public void setLeaveHospitalDate(String leaveHospitalDate) {
        this.leaveHospitalDate = leaveHospitalDate;
    }

    public String getDateAdmit() {
        return dateAdmit;
    }

    public void setDateAdmit(String dateAdmit) {
        this.dateAdmit = dateAdmit;
    }
}
