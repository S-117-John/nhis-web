/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 *
 */
public class Medical {


    private String pkpv;
    @JSONField(name = "HospitalId")
    private String hospitalId;
    @JSONField(name = "AdmissionNo")
    private String admissionNo ;
    @JSONField(name = "SciCardNo")
    private String sciCardNo;
    @JSONField(name = "SciCardIdentified")
    private String sciCardIdentified;
    @JSONField(name = "OutBedNum")
    private String outBedNum;
    @JSONField(name = "AdmissionDate")
    private Date admissionDate;
    @JSONField(name = "DischargeDate")
    private Date dischargeDate;
    @JSONField(name = "DoctorCode")
    private String doctorCode;
    @JSONField(name = "DoctorName")
    private String doctorName;
    @JSONField(name = "IsDrugAllergy")
    private String isDrugAllergy;
    @JSONField(name = "AllergyDrugCode")
    private String allergyDrugCode;
    @JSONField(name = "AllergyDrugName")
    private String allergyDrugName;
    @JSONField(name = "IsPathologicalExamination")
    private String isPathologicalExamination;
    @JSONField(name = "PathologyCode")
    private String pathologyCode;
    @JSONField(name = "IsHospitalInfected")
    private String isHospitalInfected;
    @JSONField(name = "HospitalInfectedCode")
    private String hospitalInfectedCode;
    @JSONField(name = "BloodTypeS")
    private String bloodTypeS;
    @JSONField(name = "BloodTypeE")
    private String bloodTypeE;
    @JSONField(name = "LeaveHospitalType")
    private String leaveHospitalType;
    @JSONField(name = "ChiefComplaint")
    private String chiefComplaint;
    @JSONField(name = "MedicalHistory")
    private String medicalHistory;
    @JSONField(name = "SurgeryHistory")
    private String surgeryHistory;
    @JSONField(name = "BloodTransHistory")
    private String bloodTransHistory;
    @JSONField(name = "Marriage")
    private String marriage;
    @JSONField(name = "Height")
    private String height;
    @JSONField(name = "Weight")
    private String weight;
    @JSONField(name = "NewbornDate")
    private Date newbornDate;
    @JSONField(name = "NewbornWeight")
    private String newbornWeight;
    @JSONField(name = "NewbornCurrentWeight")
    private String newbornCurrentWeight;
    @JSONField(name = "BearPregnancy")
    private String bearPregnancy;
    @JSONField(name = "BearYie")
    private String bearYie;
    @JSONField(name = "AdmissionDiseaseId")
    private String admissionDiseaseId;
    @JSONField(name = "AdmissionDiseaseName")
    private String admissionDiseaseName;
    @JSONField(name = "DiagnosePosition1")
    private String diagnosePosition1;
    @JSONField(name = "DischargeDiseaseId")
    private String dischargeDiseaseId;
    @JSONField(name = "DischargeDiseaseName")
    private String dischargeDiseaseName;
    @JSONField(name = "DiagnosePosition2")
    private String diagnosePosition2;
    @JSONField(name = "Tsblbs")
    private String tsblbs;
    @JSONField(name = "DiagnosisCode1")
    private String diagnosisCode1;
    @JSONField(name = "DiagnosisName1")
    private String diagnosisName1;
    @JSONField(name = "DiagnosisCode2")
    private String diagnosisCode2;
    @JSONField(name = "DiagnosisName2")
    private String diagnosisName2;
    @JSONField(name = "DiagnosisCode3")
    private String diagnosisCode3;
    @JSONField(name = "DiagnosisName3")
    private String diagnosisName3;
    @JSONField(name = "DiagnosisCode4")
    private String diagnosisCode4;
    @JSONField(name = "DiagnosisName4")
    private String diagnosisName4;
    @JSONField(name = "DiagnosisCode5")
    private String diagnosisCode5;
    @JSONField(name = "DiagnosisName5")
    private String diagnosisName5;
    @JSONField(name = "DiagnosisCode6")
    private String diagnosisCode6;
    @JSONField(name = "DiagnosisName6")
    private String diagnosisName6;
    @JSONField(name = "DiagnosisCode7")
    private String diagnosisCode7;
    @JSONField(name = "DiagnosisName7")
    private String diagnosisName7;
    @JSONField(name = "DiagnosisCode8")
    private String diagnosisCode8;
    @JSONField(name = "DiagnosisName8")
    private String diagnosisName8;
    @JSONField(name = "DiagnosisCode9")
    private String diagnosisCode9;
    @JSONField(name = "DiagnosisName9")
    private String diagnosisName9;
    @JSONField(name = "DiagnosisCode10")
    private String diagnosisCode10;
    @JSONField(name = "DiagnosisName10")
    private String diagnosisName10;
    @JSONField(name = "DiagnosisCode11")
    private String diagnosisCode11;
    @JSONField(name = "DiagnosisName11")
    private String diagnosisName11;
    @JSONField(name = "DiagnosisCode12")
    private String diagnosisCode12;
    @JSONField(name = "DiagnosisName12")
    private String diagnosisName12;
    @JSONField(name = "DiagnosisCode13")
    private String diagnosisCode13;
    @JSONField(name = "DiagnosisName13")
    private String diagnosisName13;
    @JSONField(name = "DiagnosisCode14")
    private String diagnosisCode14;
    @JSONField(name = "DiagnosisName14")
    private String diagnosisName14;
    @JSONField(name = "DiagnosisCode15")
    private String diagnosisCode15;
    @JSONField(name = "DiagnosisName15")
    private String diagnosisName15;
    @JSONField(name = "DiagnosisCode16")
    private String diagnosisCode16;
    @JSONField(name = "DiagnosisName16")
    private String diagnosisName16;
    @JSONField(name = "HealingEffectFlag")
    private String healingEffectFlag;

    public String getPkpv() {
        return pkpv;
    }

    public void setPkpv(String pkpv) {
        this.pkpv = pkpv;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getSciCardNo() {
        return sciCardNo;
    }

    public void setSciCardNo(String sciCardNo) {
        this.sciCardNo = sciCardNo;
    }

    public String getSciCardIdentified() {
        return sciCardIdentified;
    }

    public void setSciCardIdentified(String sciCardIdentified) {
        this.sciCardIdentified = sciCardIdentified;
    }

    public String getOutBedNum() {
        return outBedNum;
    }

    public void setOutBedNum(String outBedNum) {
        this.outBedNum = outBedNum;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
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

    public String getIsDrugAllergy() {
        return isDrugAllergy;
    }

    public void setIsDrugAllergy(String isDrugAllergy) {
        this.isDrugAllergy = isDrugAllergy;
    }

    public String getAllergyDrugCode() {
        return allergyDrugCode;
    }

    public void setAllergyDrugCode(String allergyDrugCode) {
        this.allergyDrugCode = allergyDrugCode;
    }

    public String getAllergyDrugName() {
        return allergyDrugName;
    }

    public void setAllergyDrugName(String allergyDrugName) {
        this.allergyDrugName = allergyDrugName;
    }

    public String getIsPathologicalExamination() {
        return isPathologicalExamination;
    }

    public void setIsPathologicalExamination(String isPathologicalExamination) {
        this.isPathologicalExamination = isPathologicalExamination;
    }

    public String getPathologyCode() {
        return pathologyCode;
    }

    public void setPathologyCode(String pathologyCode) {
        this.pathologyCode = pathologyCode;
    }

    public String getIsHospitalInfected() {
        return isHospitalInfected;
    }

    public void setIsHospitalInfected(String isHospitalInfected) {
        this.isHospitalInfected = isHospitalInfected;
    }

    public String getHospitalInfectedCode() {
        return hospitalInfectedCode;
    }

    public void setHospitalInfectedCode(String hospitalInfectedCode) {
        this.hospitalInfectedCode = hospitalInfectedCode;
    }

    public String getBloodTypeS() {
        return bloodTypeS;
    }

    public void setBloodTypeS(String bloodTypeS) {
        this.bloodTypeS = bloodTypeS;
    }

    public String getBloodTypeE() {
        return bloodTypeE;
    }

    public void setBloodTypeE(String bloodTypeE) {
        this.bloodTypeE = bloodTypeE;
    }

    public String getLeaveHospitalType() {
        return leaveHospitalType;
    }

    public void setLeaveHospitalType(String leaveHospitalType) {
        this.leaveHospitalType = leaveHospitalType;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getSurgeryHistory() {
        return surgeryHistory;
    }

    public void setSurgeryHistory(String surgeryHistory) {
        this.surgeryHistory = surgeryHistory;
    }

    public String getBloodTransHistory() {
        return bloodTransHistory;
    }

    public void setBloodTransHistory(String bloodTransHistory) {
        this.bloodTransHistory = bloodTransHistory;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Date getNewbornDate() {
        return newbornDate;
    }

    public void setNewbornDate(Date newbornDate) {
        this.newbornDate = newbornDate;
    }

    public String getNewbornWeight() {
        return newbornWeight;
    }

    public void setNewbornWeight(String newbornWeight) {
        this.newbornWeight = newbornWeight;
    }

    public String getNewbornCurrentWeight() {
        return newbornCurrentWeight;
    }

    public void setNewbornCurrentWeight(String newbornCurrentWeight) {
        this.newbornCurrentWeight = newbornCurrentWeight;
    }

    public String getBearPregnancy() {
        return bearPregnancy;
    }

    public void setBearPregnancy(String bearPregnancy) {
        this.bearPregnancy = bearPregnancy;
    }

    public String getBearYie() {
        return bearYie;
    }

    public void setBearYie(String bearYie) {
        this.bearYie = bearYie;
    }

    public String getAdmissionDiseaseId() {
        return admissionDiseaseId;
    }

    public void setAdmissionDiseaseId(String admissionDiseaseId) {
        this.admissionDiseaseId = admissionDiseaseId;
    }

    public String getAdmissionDiseaseName() {
        return admissionDiseaseName;
    }

    public void setAdmissionDiseaseName(String admissionDiseaseName) {
        this.admissionDiseaseName = admissionDiseaseName;
    }

    public String getDiagnosePosition1() {
        return diagnosePosition1;
    }

    public void setDiagnosePosition1(String diagnosePosition1) {
        this.diagnosePosition1 = diagnosePosition1;
    }

    public String getDischargeDiseaseId() {
        return dischargeDiseaseId;
    }

    public void setDischargeDiseaseId(String dischargeDiseaseId) {
        this.dischargeDiseaseId = dischargeDiseaseId;
    }

    public String getDischargeDiseaseName() {
        return dischargeDiseaseName;
    }

    public void setDischargeDiseaseName(String dischargeDiseaseName) {
        this.dischargeDiseaseName = dischargeDiseaseName;
    }

    public String getDiagnosePosition2() {
        return diagnosePosition2;
    }

    public void setDiagnosePosition2(String diagnosePosition2) {
        this.diagnosePosition2 = diagnosePosition2;
    }

    public String getTsblbs() {
        return tsblbs;
    }

    public void setTsblbs(String tsblbs) {
        this.tsblbs = tsblbs;
    }

    public String getDiagnosisCode1() {
        return diagnosisCode1;
    }

    public void setDiagnosisCode1(String diagnosisCode1) {
        this.diagnosisCode1 = diagnosisCode1;
    }

    public String getDiagnosisName1() {
        return diagnosisName1;
    }

    public void setDiagnosisName1(String diagnosisName1) {
        this.diagnosisName1 = diagnosisName1;
    }

    public String getDiagnosisCode2() {
        return diagnosisCode2;
    }

    public void setDiagnosisCode2(String diagnosisCode2) {
        this.diagnosisCode2 = diagnosisCode2;
    }

    public String getDiagnosisName2() {
        return diagnosisName2;
    }

    public void setDiagnosisName2(String diagnosisName2) {
        this.diagnosisName2 = diagnosisName2;
    }

    public String getDiagnosisCode3() {
        return diagnosisCode3;
    }

    public void setDiagnosisCode3(String diagnosisCode3) {
        this.diagnosisCode3 = diagnosisCode3;
    }

    public String getDiagnosisName3() {
        return diagnosisName3;
    }

    public void setDiagnosisName3(String diagnosisName3) {
        this.diagnosisName3 = diagnosisName3;
    }

    public String getDiagnosisCode4() {
        return diagnosisCode4;
    }

    public void setDiagnosisCode4(String diagnosisCode4) {
        this.diagnosisCode4 = diagnosisCode4;
    }

    public String getDiagnosisName4() {
        return diagnosisName4;
    }

    public void setDiagnosisName4(String diagnosisName4) {
        this.diagnosisName4 = diagnosisName4;
    }

    public String getDiagnosisCode5() {
        return diagnosisCode5;
    }

    public void setDiagnosisCode5(String diagnosisCode5) {
        this.diagnosisCode5 = diagnosisCode5;
    }

    public String getDiagnosisName5() {
        return diagnosisName5;
    }

    public void setDiagnosisName5(String diagnosisName5) {
        this.diagnosisName5 = diagnosisName5;
    }

    public String getDiagnosisCode6() {
        return diagnosisCode6;
    }

    public void setDiagnosisCode6(String diagnosisCode6) {
        this.diagnosisCode6 = diagnosisCode6;
    }

    public String getDiagnosisName6() {
        return diagnosisName6;
    }

    public void setDiagnosisName6(String diagnosisName6) {
        this.diagnosisName6 = diagnosisName6;
    }

    public String getDiagnosisCode7() {
        return diagnosisCode7;
    }

    public void setDiagnosisCode7(String diagnosisCode7) {
        this.diagnosisCode7 = diagnosisCode7;
    }

    public String getDiagnosisName7() {
        return diagnosisName7;
    }

    public void setDiagnosisName7(String diagnosisName7) {
        this.diagnosisName7 = diagnosisName7;
    }

    public String getDiagnosisCode8() {
        return diagnosisCode8;
    }

    public void setDiagnosisCode8(String diagnosisCode8) {
        this.diagnosisCode8 = diagnosisCode8;
    }

    public String getDiagnosisName8() {
        return diagnosisName8;
    }

    public void setDiagnosisName8(String diagnosisName8) {
        this.diagnosisName8 = diagnosisName8;
    }

    public String getDiagnosisCode9() {
        return diagnosisCode9;
    }

    public void setDiagnosisCode9(String diagnosisCode9) {
        this.diagnosisCode9 = diagnosisCode9;
    }

    public String getDiagnosisName9() {
        return diagnosisName9;
    }

    public void setDiagnosisName9(String diagnosisName9) {
        this.diagnosisName9 = diagnosisName9;
    }

    public String getDiagnosisCode10() {
        return diagnosisCode10;
    }

    public void setDiagnosisCode10(String diagnosisCode10) {
        this.diagnosisCode10 = diagnosisCode10;
    }

    public String getDiagnosisName10() {
        return diagnosisName10;
    }

    public void setDiagnosisName10(String diagnosisName10) {
        this.diagnosisName10 = diagnosisName10;
    }

    public String getDiagnosisCode11() {
        return diagnosisCode11;
    }

    public void setDiagnosisCode11(String diagnosisCode11) {
        this.diagnosisCode11 = diagnosisCode11;
    }

    public String getDiagnosisName11() {
        return diagnosisName11;
    }

    public void setDiagnosisName11(String diagnosisName11) {
        this.diagnosisName11 = diagnosisName11;
    }

    public String getDiagnosisCode12() {
        return diagnosisCode12;
    }

    public void setDiagnosisCode12(String diagnosisCode12) {
        this.diagnosisCode12 = diagnosisCode12;
    }

    public String getDiagnosisName12() {
        return diagnosisName12;
    }

    public void setDiagnosisName12(String diagnosisName12) {
        this.diagnosisName12 = diagnosisName12;
    }

    public String getDiagnosisCode13() {
        return diagnosisCode13;
    }

    public void setDiagnosisCode13(String diagnosisCode13) {
        this.diagnosisCode13 = diagnosisCode13;
    }

    public String getDiagnosisName13() {
        return diagnosisName13;
    }

    public void setDiagnosisName13(String diagnosisName13) {
        this.diagnosisName13 = diagnosisName13;
    }

    public String getDiagnosisCode14() {
        return diagnosisCode14;
    }

    public void setDiagnosisCode14(String diagnosisCode14) {
        this.diagnosisCode14 = diagnosisCode14;
    }

    public String getDiagnosisName14() {
        return diagnosisName14;
    }

    public void setDiagnosisName14(String diagnosisName14) {
        this.diagnosisName14 = diagnosisName14;
    }

    public String getDiagnosisCode15() {
        return diagnosisCode15;
    }

    public void setDiagnosisCode15(String diagnosisCode15) {
        this.diagnosisCode15 = diagnosisCode15;
    }

    public String getDiagnosisName15() {
        return diagnosisName15;
    }

    public void setDiagnosisName15(String diagnosisName15) {
        this.diagnosisName15 = diagnosisName15;
    }

    public String getDiagnosisCode16() {
        return diagnosisCode16;
    }

    public void setDiagnosisCode16(String diagnosisCode16) {
        this.diagnosisCode16 = diagnosisCode16;
    }

    public String getDiagnosisName16() {
        return diagnosisName16;
    }

    public void setDiagnosisName16(String diagnosisName16) {
        this.diagnosisName16 = diagnosisName16;
    }

    public String getHealingEffectFlag() {
        return healingEffectFlag;
    }

    public void setHealingEffectFlag(String healingEffectFlag) {
        this.healingEffectFlag = healingEffectFlag;
    }
}