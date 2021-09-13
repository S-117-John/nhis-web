package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author chengjia
 * @Description 患者病历入院记录明细
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TaiKangRespEmrAdmitRecInfo {

    /*
     * 业务流水号_入院日期（拼接）
     * */
    @XmlElement(name = "TreatSerialNo")
    private String codeIpDateBegin;
    /*
     * 就诊号-业务流水号
     * */
    @XmlElement(name = "PatientNumber")
    private String codeIp;

    /*
     * 入院时间-入院时间
     * */
    @XmlElement(name = "InHosDate")
    private String dateBegin;

    /*
     * 主诉
     * */
    @XmlElement(name = "MainSuit")
    private String mainSuit;
    /*
     *现病史
     * */
    @XmlElement(name = "HistoryPresentIllness")

    private String historyPresentIllness;
    /*
     * 既往史
     * */
    @XmlElement(name = "PastHistory")
    private String pastHistory;
    /*
     * 个人史
     * */
    @XmlElement(name = "PersonalHistory")
    private String personalHistory;
    /*
     * 家族史
     * */
    @XmlElement(name = "FamilyHistory")
    private String familyHistory;
    /*
     * 疾病代码
     * */
    @XmlElement(name = "DiseaseCode")
    private String diseaseCode;
    /*
     * 疾病名称
     * */
    @XmlElement(name = "DiseaseName")
    private String diseaseName;
    /*
     * 手术编码
     * */
    @XmlElement(name = "OpsCode")
    private String opsCode;
    /*
     * 手术名称
     * */
   @XmlElement(name = "OpsName")
    private String opsName;
    /*
     * 出院诊断
     * */
    @XmlElement(name = "LeaveMedicalSick")
    private String leaveMedicalSick;
   
    /*
     * 输血史
     * */
    @XmlElement(name = "HistoryOfBloodTransfusion")
    private String historyOfBloodTransfusion;
   
    /*
     * 过敏史
     * */
    @XmlElement(name = "Allergies")
    private String allergies;
    /*
     * 预防接种史
     * */
    @XmlElement(name = "HistoryOfImmunizations")
    private String historyOfImmunizations;
   
    /*
     * 月经史
     * */
    @XmlElement(name = "MenstrualHistory")
    private String menstrualHistory;
    
    /*
     * 体格检查结果
     * */
    @XmlElement(name = "PhysicalExamination")
    private String physicalExamination;
    
    /*
     * 辅助检查结果
     * */
    @XmlElement(name = "AuxiliaryExamination")
    private String auxiliaryExamination;

    /*
     * 科室
     * */
    @XmlElement(name = "TreatOffice")
    private String treatOffice;
    
    /*
     * 病理检查号
     * */
    @XmlElement(name = "PathologicalExaminationNo")
    private String pathologicalExaminationNo;
    
    /*
     * 入院情况
     * */
    @XmlElement(name = "CheckInDescription")
    private String checkInDescription;
    
    /*
     * 诊疗经过
     * */
    @XmlElement(name = "TreatmentRecord")
    private String treatmentRecord;
    
    /*
     * 治疗结果
     * */
    @XmlElement(name = "TreatmentResult")
    private String treatmentResult;
    
    /*
     * 出院情况描述
     * */
    @XmlElement(name = "LeaveDescription")
    private String leaveDescription;
    
    /*
     * 出院医嘱
     * */
    @XmlElement(name = "DischargeOrder")
    private String dischargeOrder;
    
    /*
     * 经治医生
     * */
    @XmlElement(name = "MasterDoctor")
    private String masterDoctor;
    
    /*
     * 上级医生
     * */
    @XmlElement(name = "SuperiorDoctor")
    private String superiorDoctor;
    
    /*
     * 出院带药
     * */
    @XmlElement(name = "DischargeMedication")
    private String dischargeMedication;

	public String getCodeIpDateBegin() {
		return codeIpDateBegin;
	}

	public void setCodeIpDateBegin(String codeIpDateBegin) {
		this.codeIpDateBegin = codeIpDateBegin;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getMainSuit() {
		return mainSuit;
	}

	public void setMainSuit(String mainSuit) {
		this.mainSuit = mainSuit;
	}

	public String getHistoryPresentIllness() {
		return historyPresentIllness;
	}

	public void setHistoryPresentIllness(String historyPresentIllness) {
		this.historyPresentIllness = historyPresentIllness;
	}

	public String getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(String pastHistory) {
		this.pastHistory = pastHistory;
	}

	public String getPersonalHistory() {
		return personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getDiseaseCode() {
		return diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getOpsCode() {
		return opsCode;
	}

	public void setOpsCode(String opsCode) {
		this.opsCode = opsCode;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getLeaveMedicalSick() {
		return leaveMedicalSick;
	}

	public void setLeaveMedicalSick(String leaveMedicalSick) {
		this.leaveMedicalSick = leaveMedicalSick;
	}

	public String getHistoryOfBloodTransfusion() {
		return historyOfBloodTransfusion;
	}

	public void setHistoryOfBloodTransfusion(String historyOfBloodTransfusion) {
		this.historyOfBloodTransfusion = historyOfBloodTransfusion;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getHistoryOfImmunizations() {
		return historyOfImmunizations;
	}

	public void setHistoryOfImmunizations(String historyOfImmunizations) {
		this.historyOfImmunizations = historyOfImmunizations;
	}

	public String getMenstrualHistory() {
		return menstrualHistory;
	}

	public void setMenstrualHistory(String menstrualHistory) {
		this.menstrualHistory = menstrualHistory;
	}

	public String getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public String getAuxiliaryExamination() {
		return auxiliaryExamination;
	}

	public void setAuxiliaryExamination(String auxiliaryExamination) {
		this.auxiliaryExamination = auxiliaryExamination;
	}

	public String getTreatOffice() {
		return treatOffice;
	}

	public void setTreatOffice(String treatOffice) {
		this.treatOffice = treatOffice;
	}

	public String getPathologicalExaminationNo() {
		return pathologicalExaminationNo;
	}

	public void setPathologicalExaminationNo(String pathologicalExaminationNo) {
		this.pathologicalExaminationNo = pathologicalExaminationNo;
	}

	public String getCheckInDescription() {
		return checkInDescription;
	}

	public void setCheckInDescription(String checkInDescription) {
		this.checkInDescription = checkInDescription;
	}

	public String getTreatmentRecord() {
		return treatmentRecord;
	}

	public void setTreatmentRecord(String treatmentRecord) {
		this.treatmentRecord = treatmentRecord;
	}

	public String getTreatmentResult() {
		return treatmentResult;
	}

	public void setTreatmentResult(String treatmentResult) {
		this.treatmentResult = treatmentResult;
	}

	

	public String getLeaveDescription() {
		return leaveDescription;
	}

	public void setLeaveDescription(String leaveDescription) {
		this.leaveDescription = leaveDescription;
	}

	public String getDischargeOrder() {
		return dischargeOrder;
	}

	public void setDischargeOrder(String dischargeOrder) {
		this.dischargeOrder = dischargeOrder;
	}

	public String getMasterDoctor() {
		return masterDoctor;
	}

	public void setMasterDoctor(String masterDoctor) {
		this.masterDoctor = masterDoctor;
	}

	public String getSuperiorDoctor() {
		return superiorDoctor;
	}

	public void setSuperiorDoctor(String superiorDoctor) {
		this.superiorDoctor = superiorDoctor;
	}

	public String getDischargeMedication() {
		return dischargeMedication;
	}

	public void setDischargeMedication(String dischargeMedication) {
		this.dischargeMedication = dischargeMedication;
	}
    
    
}
