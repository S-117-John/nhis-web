package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

public class CiUpMedicalRecordVo {

    /**
     *就诊流水号
     *是
     */
    private String medicalNum;


    /**
     *医院病历ID
     * 是
     */
    private String hospitalRecordId;

    /**
     *住院号
     */
    private String inHospitalNum;

    /**
     *主诉
     */
    private String cheifComplaint;

    /**
     *现病史
     */
    private String historyPresentIllness;

    /**
     *既往史
     */
    private String pastDiseaseHistory;

    /**
     *个人史
     */
    private String personalHistory;

    /**
     *婚育史
     */
    private String obstetricalHistory;

    /**
     *月经史
     */
    private String menstruationHistory;

    /**
     *家族史
     */
    private String familyHistory;

    /**
     *入院临床诊断
     */
    private String inHosClinicalDiagnosis;

    /**
     *入院诊断 ICD 列表
     */
    private List<CiIcdVo> inHosDiagnosisList;
    
    /**
     *出院临床诊断
     */
    private String dischClinicalDiagnosis;

    /**
     * 出院诊断 ICD 列表
     */
    private List<CiIcdVo> dischDiagnosisList;
    
    /**
     *门诊诊断
     */
    private String outpatientDiagnosis;
    
    /**
     * 门诊诊断 ICD 列表
     */
    private List<CiIcdVo> outpatientDiagnosisList;
    
    /**
     * 手术信息列表
     */
    private List<CiOperationVo> operationList;

    /**
     *手术经过
     */
    private String operationProcedure;

    /**
     *诊治经过
     */
    private String diagnosisTreatment;

    /**
     *主治医生
     */
    private String attendingPhysician;

    /**
     *出院情况
     */
    private String dischargeStatus;

    /**
     * 出院医嘱
     */
    private String dischargeOrder;

    /**
     *体格检查
     */
    private String physicalExamination;

    /**
     *专科情况
     */
    private String juniorCollege;

    /**
     *辅助检查
     */
    private String auxiliaryExamination;
    
    /**
     *全量病历信息
     *如果医院没有结构化的数据，可以放入此项中传输
     *非必填
     */
    private String totalRecordInfo;

	public String getMedicalNum() {
		return medicalNum;
	}

	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}

	public String getHospitalRecordId() {
		return hospitalRecordId;
	}

	public void setHospitalRecordId(String hospitalRecordId) {
		this.hospitalRecordId = hospitalRecordId;
	}

	public String getInHospitalNum() {
		return inHospitalNum;
	}

	public void setInHospitalNum(String inHospitalNum) {
		this.inHospitalNum = inHospitalNum;
	}

	public String getCheifComplaint() {
		return cheifComplaint;
	}

	public void setCheifComplaint(String cheifComplaint) {
		this.cheifComplaint = cheifComplaint;
	}

	public String getHistoryPresentIllness() {
		return historyPresentIllness;
	}

	public void setHistoryPresentIllness(String historyPresentIllness) {
		this.historyPresentIllness = historyPresentIllness;
	}

	public String getPastDiseaseHistory() {
		return pastDiseaseHistory;
	}

	public void setPastDiseaseHistory(String pastDiseaseHistory) {
		this.pastDiseaseHistory = pastDiseaseHistory;
	}

	public String getPersonalHistory() {
		return personalHistory;
	}

	public void setPersonalHistory(String personalHistory) {
		this.personalHistory = personalHistory;
	}

	public String getObstetricalHistory() {
		return obstetricalHistory;
	}

	public void setObstetricalHistory(String obstetricalHistory) {
		this.obstetricalHistory = obstetricalHistory;
	}

	public String getMenstruationHistory() {
		return menstruationHistory;
	}

	public void setMenstruationHistory(String menstruationHistory) {
		this.menstruationHistory = menstruationHistory;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getInHosClinicalDiagnosis() {
		return inHosClinicalDiagnosis;
	}

	public void setInHosClinicalDiagnosis(String inHosClinicalDiagnosis) {
		this.inHosClinicalDiagnosis = inHosClinicalDiagnosis;
	}

	public List<CiIcdVo> getInHosDiagnosisList() {
		return inHosDiagnosisList;
	}

	public void setInHosDiagnosisList(List<CiIcdVo> inHosDiagnosisList) {
		this.inHosDiagnosisList = inHosDiagnosisList;
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

	public String getOutpatientDiagnosis() {
		return outpatientDiagnosis;
	}

	public void setOutpatientDiagnosis(String outpatientDiagnosis) {
		this.outpatientDiagnosis = outpatientDiagnosis;
	}

	public List<CiIcdVo> getOutpatientDiagnosisList() {
		return outpatientDiagnosisList;
	}

	public void setOutpatientDiagnosisList(List<CiIcdVo> outpatientDiagnosisList) {
		this.outpatientDiagnosisList = outpatientDiagnosisList;
	}

	public List<CiOperationVo> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<CiOperationVo> operationList) {
		this.operationList = operationList;
	}

	public String getOperationProcedure() {
		return operationProcedure;
	}

	public void setOperationProcedure(String operationProcedure) {
		this.operationProcedure = operationProcedure;
	}

	public String getDiagnosisTreatment() {
		return diagnosisTreatment;
	}

	public void setDiagnosisTreatment(String diagnosisTreatment) {
		this.diagnosisTreatment = diagnosisTreatment;
	}

	public String getAttendingPhysician() {
		return attendingPhysician;
	}

	public void setAttendingPhysician(String attendingPhysician) {
		this.attendingPhysician = attendingPhysician;
	}

	public String getDischargeStatus() {
		return dischargeStatus;
	}

	public void setDischargeStatus(String dischargeStatus) {
		this.dischargeStatus = dischargeStatus;
	}

	public String getDischargeOrder() {
		return dischargeOrder;
	}

	public void setDischargeOrder(String dischargeOrder) {
		this.dischargeOrder = dischargeOrder;
	}

	public String getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public String getJuniorCollege() {
		return juniorCollege;
	}

	public void setJuniorCollege(String juniorCollege) {
		this.juniorCollege = juniorCollege;
	}

	public String getAuxiliaryExamination() {
		return auxiliaryExamination;
	}

	public void setAuxiliaryExamination(String auxiliaryExamination) {
		this.auxiliaryExamination = auxiliaryExamination;
	}

	public String getTotalRecordInfo() {
		return totalRecordInfo;
	}

	public void setTotalRecordInfo(String totalRecordInfo) {
		this.totalRecordInfo = totalRecordInfo;
	}

    
}
