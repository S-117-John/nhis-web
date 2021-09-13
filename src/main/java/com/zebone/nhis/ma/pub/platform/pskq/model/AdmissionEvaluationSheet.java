package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * 入院评估单数据集
 * @author 卡卡西
 */
public class AdmissionEvaluationSheet {

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
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0055014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0055015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0055016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0055017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0055018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0055019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0055020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0055021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0055022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0055023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0055024",name = "HOSPITAL_WAY",eName = "入院方式")
    private String hospitalWay;

    @MetadataDescribe(id= "LHDE0055025",name = "MARRIAGE",eName = "婚姻状况")
    private String marriage;

    @MetadataDescribe(id= "LHDE0055026",name = "RELIGIOUS",eName = "宗教信仰")
    private String religious;

    @MetadataDescribe(id= "LHDE0055027",name = "EDUCATION",eName = "教育程度")
    private String education;

    @MetadataDescribe(id= "LHDE0055028",name = "LANGUAGE",eName = "最常用语言")
    private String language;


    @MetadataDescribe(id= "LHDE0055029",name = "SMOKING",eName = "吸烟")
    private String smoking;


    @MetadataDescribe(id= "LHDE0055030",name = "DRINK_WINE",eName = "嗜酒")
    private String drinkWine;

    @MetadataDescribe(id= "LHDE0055031",name = "DRUG_DEPENDENCE",eName = "药物依赖")
    private String drugDependence;


    @MetadataDescribe(id= "LHDE0055032",name = "ALLERGY",eName = "过敏史")
    private String allergy;


    @MetadataDescribe(id= "LHDE0055033",name = "CHRONIC_DISEASES",eName = "慢性病")
    private String chronicDiseases;

    @MetadataDescribe(id= "LHDE0055023",name = "MORPHOLOGICAL_AWARENESS",eName = "BED_NO")
    private String morphologicalAwareness;

    @MetadataDescribe(id= "LHDE0055035",name = "ANSWER",eName = "对答")
    private String answer;


    @MetadataDescribe(id= "LHDE0055036",name = "VISION",eName = "视力")
    private String vision;


    @MetadataDescribe(id= "LHDE0055037",name = "HEARING",eName = "听力")
    private String hearing;


    @MetadataDescribe(id= "LHDE0055038",name = "APPETITE",eName = "食欲")
    private String appetite;


    @MetadataDescribe(id= "LHDE0055039",name = "EATING_SITUATION",eName = "进食情况")
    private String eatingSituation;


    @MetadataDescribe(id= "LHDE0055040",name = "MUCOUS_MEMBRANE",eName = "口腔粘膜")
    private String mucousMembrane;


    @MetadataDescribe(id= "LHDE0055041",name = "FALSE_TEETH",eName = "假牙")
    private String falseTeeth;


    @MetadataDescribe(id= "LHDE0055042",name = "LIFT_PROVIDE",eName = "生活自理能力")
    private String liftProvide;


    @MetadataDescribe(id= "LHDE0055043",name = "URINATION",eName = "排尿")
    private String urination;


    @MetadataDescribe(id= "LHDE0055044",name = "STOOL FREQUENCY",eName = "排便次数")
    private String stoolFrequency;


    @MetadataDescribe(id= "LHDE0055046",name = "性状",eName = "CHARACTER")
    private String character;


    @MetadataDescribe(id= "LHDE0055047",name = "皮肤",eName = "SKIN")
    private String skin;


    @MetadataDescribe(id= "LHDE0055048",name = "面色",eName = "FACE")
    private String face;


    @MetadataDescribe(id= "LHDE0055049",name = "体位",eName = "POSITION")
    private String position;


    @MetadataDescribe(id= "LHDE0055050",name = "四肢活动",eName = "LIMBS_ACTIVITY")
    private String limbsActivity;


    @MetadataDescribe(id= "LHDE0055051",name = "末梢体温",eName = "PERIPHERAL_TEMPERATURE")
    private String peripheralTemperature;


    @MetadataDescribe(id= "LHDE0055052",name = "睡眠",eName = "SLEEP")
    private String sleep;


    @MetadataDescribe(id= "LHDE0055053",name = "睡眠状况",eName = "SLEEP_CONDITION")
    private String sleepCondition;


    @MetadataDescribe(id= "LHDE0055054",name = "辅助睡眠",eName = "SLEEP_AIDS")
    private String sleepAids;


    @MetadataDescribe(id= "LHDE0055055",name = "情绪",eName = "MOOD")
    private String mood;


    @MetadataDescribe(id= "LHDE0055056",name = "家庭支持",eName = "FAMILY_SUPPORT")
    private String familySupport;

    @MetadataDescribe(id= "LHDE0055057",name = "跌倒风险",eName = "FALL_RISK")
    private String fallRisk;

    @MetadataDescribe(id= "LHDE0055058",name = "压疮风险",eName = "RISK_OF_PRESSURE_ULCERS")
    private String riskOfPressureUlcers;


    @MetadataDescribe(id= "LHDE0055059",name = "专科护理评估",eName = "SPECIALIST_NURSING_ASSESSMENT")
    private String specialistNursingAssessment;


    @MetadataDescribe(id= "LHDE0055060",name = "生活护理",eName = "LIFE CARE")
    private String lifeCare;


    @MetadataDescribe(id= "LHDE0055061",name = "监测",eName = "MONITORING")
    private String monitoring;


    @MetadataDescribe(id= "LHDE0055062",name = "管道护理",eName = "PIPELINE CARE")
    private String pipelineCare;


    @MetadataDescribe(id= "LHDE0055063",name = "安防护理",eName = "SECURITY CARE")
    private String securityCare;

    @MetadataDescribe(id= "LHDE0055064",name = "其他",eName = "OTHER")
    private String other;

    @MetadataDescribe(id= "LHDE0055065",name = "告知",eName = "INFORM")
    private String inform;


    @MetadataDescribe(id= "LHDE0055066",name = "休息/活动",eName = "REST/ACTIVITY")
    private String restActivity;

    @MetadataDescribe(id= "LHDE0055067",name = "饮食",eName = "DIET")
    private String diet;

    @MetadataDescribe(id= "LHDE0055068",name = "治疗",eName = "TREATMENT")
    private String treatment;

    @MetadataDescribe(id= "LHDE0055069",name = "告知指导其他",eName = "INFORM AND GUIDE OTHERS")
    private String informAndGuideOthers;


    @MetadataDescribe(id= "LHDE0055070",name = "记录时间",eName = "RECORDING_TIME")
    private String recordingTime;


    @MetadataDescribe(id= "LHDE0055071",name = "记录人",eName = "RECORDER")
    private String recorder;


    @MetadataDescribe(id= "LHDE0055072",name = "AUDIT_TIME",eName = "审核时间")
    private String auditTime;

    @MetadataDescribe(id= "LHDE0055073",name = "REVIEWER",eName = "审核人")
    private String reviewer;


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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

    public String getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getHospitalWay() {
        return hospitalWay;
    }

    public void setHospitalWay(String hospitalWay) {
        this.hospitalWay = hospitalWay;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getReligious() {
        return religious;
    }

    public void setReligious(String religious) {
        this.religious = religious;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinkWine() {
        return drinkWine;
    }

    public void setDrinkWine(String drinkWine) {
        this.drinkWine = drinkWine;
    }

    public String getDrugDependence() {
        return drugDependence;
    }

    public void setDrugDependence(String drugDependence) {
        this.drugDependence = drugDependence;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getMorphologicalAwareness() {
        return morphologicalAwareness;
    }

    public void setMorphologicalAwareness(String morphologicalAwareness) {
        this.morphologicalAwareness = morphologicalAwareness;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getHearing() {
        return hearing;
    }

    public void setHearing(String hearing) {
        this.hearing = hearing;
    }

    public String getAppetite() {
        return appetite;
    }

    public void setAppetite(String appetite) {
        this.appetite = appetite;
    }

    public String getEatingSituation() {
        return eatingSituation;
    }

    public void setEatingSituation(String eatingSituation) {
        this.eatingSituation = eatingSituation;
    }

    public String getMucousMembrane() {
        return mucousMembrane;
    }

    public void setMucousMembrane(String mucousMembrane) {
        this.mucousMembrane = mucousMembrane;
    }

    public String getFalseTeeth() {
        return falseTeeth;
    }

    public void setFalseTeeth(String falseTeeth) {
        this.falseTeeth = falseTeeth;
    }

    public String getLiftProvide() {
        return liftProvide;
    }

    public void setLiftProvide(String liftProvide) {
        this.liftProvide = liftProvide;
    }

    public String getUrination() {
        return urination;
    }

    public void setUrination(String urination) {
        this.urination = urination;
    }

    public String getStoolFrequency() {
        return stoolFrequency;
    }

    public void setStoolFrequency(String stoolFrequency) {
        this.stoolFrequency = stoolFrequency;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLimbsActivity() {
        return limbsActivity;
    }

    public void setLimbsActivity(String limbsActivity) {
        this.limbsActivity = limbsActivity;
    }

    public String getPeripheralTemperature() {
        return peripheralTemperature;
    }

    public void setPeripheralTemperature(String peripheralTemperature) {
        this.peripheralTemperature = peripheralTemperature;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getSleepCondition() {
        return sleepCondition;
    }

    public void setSleepCondition(String sleepCondition) {
        this.sleepCondition = sleepCondition;
    }

    public String getSleepAids() {
        return sleepAids;
    }

    public void setSleepAids(String sleepAids) {
        this.sleepAids = sleepAids;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getFamilySupport() {
        return familySupport;
    }

    public void setFamilySupport(String familySupport) {
        this.familySupport = familySupport;
    }

    public String getFallRisk() {
        return fallRisk;
    }

    public void setFallRisk(String fallRisk) {
        this.fallRisk = fallRisk;
    }

    public String getRiskOfPressureUlcers() {
        return riskOfPressureUlcers;
    }

    public void setRiskOfPressureUlcers(String riskOfPressureUlcers) {
        this.riskOfPressureUlcers = riskOfPressureUlcers;
    }

    public String getSpecialistNursingAssessment() {
        return specialistNursingAssessment;
    }

    public void setSpecialistNursingAssessment(String specialistNursingAssessment) {
        this.specialistNursingAssessment = specialistNursingAssessment;
    }

    public String getLifeCare() {
        return lifeCare;
    }

    public void setLifeCare(String lifeCare) {
        this.lifeCare = lifeCare;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring;
    }

    public String getPipelineCare() {
        return pipelineCare;
    }

    public void setPipelineCare(String pipelineCare) {
        this.pipelineCare = pipelineCare;
    }

    public String getSecurityCare() {
        return securityCare;
    }

    public void setSecurityCare(String securityCare) {
        this.securityCare = securityCare;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public String getRestActivity() {
        return restActivity;
    }

    public void setRestActivity(String restActivity) {
        this.restActivity = restActivity;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getInformAndGuideOthers() {
        return informAndGuideOthers;
    }

    public void setInformAndGuideOthers(String informAndGuideOthers) {
        this.informAndGuideOthers = informAndGuideOthers;
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

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}
