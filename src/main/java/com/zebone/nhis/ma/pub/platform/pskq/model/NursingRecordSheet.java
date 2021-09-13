package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

import java.util.Date;

/**
 * 护理记录单
 */
public class NursingRecordSheet {


    @MetadataDescribe(id= "LHDE0055001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0055002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0055003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0055004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0055005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0055006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0055007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0055008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0055009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0055010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0055011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0055012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0055013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @MetadataDescribe(id= "LHDE0055014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0055015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0055016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0021017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0055017",name = "记录时间",eName = "RECORDING_TIME")
    private String recordingTime;

    @MetadataDescribe(id= "LHDE0055018",name = "表单记录人",eName = "RECORDER")
    private String recorder;

    @MetadataDescribe(id= "LHDE0055019",name = "体温 ℃",eName = "BODY_TEMPERATURE")
    private String bodyTemperature;


    @MetadataDescribe(id= "LHDE0055020",name = "脉搏 次/分",eName = "PULSE")
    private String pulse;

    @MetadataDescribe(id= "LHDE0055021",name = "呼吸 次/分",eName = "BREATHING")
    private String breathingFrequency ;

    @MetadataDescribe(id= "LHDE0055022",name = "收缩压 mmHg",eName = "SYSTOLIC_BLOOD_PRESSURE")
    private String systolicBloodPressure;

    @MetadataDescribe(id= "LHDE0055023",name = "舒张压 mmHg",eName = "DIASTOLIC_BLOOD_PRESSURE")
    private String diastolicBloodPressure;

    @MetadataDescribe(id= "LHDE0055024",name = "血氧%",eName = "BLOOD_OXYGEN")
    private String bloodOxygen;

    @MetadataDescribe(id= "LHDE0055025",name = "意识",eName = "CONSCIOUSNESS")
    private String consciousness;

    @MetadataDescribe(id= "LHDE0055026",name = "入量内容",eName = "INPUT_AMOUNT_CONTENT")
    private String inputAmountContent;

    @MetadataDescribe(id= "LHDE0055028",name = "入量值ml",eName = "INPUT_AMOUNT_VALUE")
    private String inputAmountValue;

    @MetadataDescribe(id= "LHDE0055029",name = "出量内容",eName = "AMOUNT")
    private String amount;

    @MetadataDescribe(id= "LHDE0055030",name = "出量值 ml",eName = "OUTPUT_AMOUNT_VALUE")
    private String outputAmountValue;

    @MetadataDescribe(id= "LHDE0055031",name = "伤口",eName = "WOUND")
    private String wound;

    @MetadataDescribe(id= "LHDE0055032",name = "伤口敷料",eName = "WOUND_DRESSING")
    private String woundDressing;

    @MetadataDescribe(id= "LHDE0055033",name = "吸氧",eName = "OXYGEN")
    private String oxygen;

    @MetadataDescribe(id= "LHDE0055034",name = "呼吸",eName = "BREATHING")
    private String breathing;

    @MetadataDescribe(id= "LHDE0055035",name = "冰敷",eName = "ICE_COMPRESS")
    private String iceCompress;

    @MetadataDescribe(id= "LHDE0055036",name = "记录单ID",eName = "RECORD_ID")
    private String recordId;

    @MetadataDescribe(id= "LHDE0055037",name = "行号",eName = "ROW_ID")
    private String rowId;

    @MetadataDescribe(id= "LHDE0055038",name = "特殊情况",eName = "REMARKS")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getEmpiId() {
        return empiId;
    }

    public void setEmpiId(String empiId) {
        this.empiId = empiId;
    }

    public String getPkPatient() {
        return pkPatient;
    }

    public void setPkPatient(String pkPatient) {
        this.pkPatient = pkPatient;
    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
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

    public String getEncounterTypeCode() {
        return encounterTypeCode;
    }

    public void setEncounterTypeCode(String encounterTypeCode) {
        this.encounterTypeCode = encounterTypeCode;
    }

    public String getEncounterTypeName() {
        return encounterTypeName;
    }

    public void setEncounterTypeName(String encounterTypeName) {
        this.encounterTypeName = encounterTypeName;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAgeYear() {
        return ageYear;
    }

    public void setAgeYear(String ageYear) {
        this.ageYear = ageYear;
    }

    public String getAgeMonth() {
        return ageMonth;
    }

    public void setAgeMonth(String ageMonth) {
        this.ageMonth = ageMonth;
    }

    public String getAgeDay() {
        return ageDay;
    }

    public void setAgeDay(String ageDay) {
        this.ageDay = ageDay;
    }

    public String getAgeHour() {
        return ageHour;
    }

    public void setAgeHour(String ageHour) {
        this.ageHour = ageHour;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getBreathingFrequency() {
        return breathingFrequency;
    }

    public void setBreathingFrequency(String breathingFrequency) {
        this.breathingFrequency = breathingFrequency;
    }

    public String getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(String systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public String getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(String diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public String getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(String bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public String getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(String consciousness) {
        this.consciousness = consciousness;
    }

    public String getInputAmountContent() {
        return inputAmountContent;
    }

    public void setInputAmountContent(String inputAmountContent) {
        this.inputAmountContent = inputAmountContent;
    }

    public String getInputAmountValue() {
        return inputAmountValue;
    }

    public void setInputAmountValue(String inputAmountValue) {
        this.inputAmountValue = inputAmountValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOutputAmountValue() {
        return outputAmountValue;
    }

    public void setOutputAmountValue(String outputAmountValue) {
        this.outputAmountValue = outputAmountValue;
    }

    public String getWound() {
        return wound;
    }

    public void setWound(String wound) {
        this.wound = wound;
    }

    public String getWoundDressing() {
        return woundDressing;
    }

    public void setWoundDressing(String woundDressing) {
        this.woundDressing = woundDressing;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }

    public String getBreathing() {
        return breathing;
    }

    public void setBreathing(String breathing) {
        this.breathing = breathing;
    }

    public String getIceCompress() {
        return iceCompress;
    }

    public void setIceCompress(String iceCompress) {
        this.iceCompress = iceCompress;
    }
}
