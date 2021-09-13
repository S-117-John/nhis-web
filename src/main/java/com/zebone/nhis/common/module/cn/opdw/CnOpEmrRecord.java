package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;

@Table(value="CN_EMR_OP")
public class CnOpEmrRecord extends BaseModule  {

	@PK
	@Field(value="PK_EMROP",id=KeyId.UUID)
    private String pkEmrop;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PROBLEM")
    private String problem;

	@Field(value="PRESENT")
    private String present;

	@Field(value="HISTORY")
    private String history;

	@Field(value="ALLERGY")
    private String allergy;

	@Field(value="HEIGHT")
    private Double height;

	@Field(value="WEIGHT")
    private Double weight;

	@Field(value="TEMPERATURE")
    private Double temperature;

	@Field(value="SBP")
    private Integer sbp;

	@Field(value="DBP")
    private Integer dbp;

	@Field(value="EXAM_PHY")
    private String examPhy;

	@Field(value="EXAM_SPEC")
    private String examSpec;
	
	@Field(value="EXAM_AUX")
    private String examAux;

    @Field(value="TREATMENT_PROGRAMS")
    private String treatmentPrograms;

    @Field(value="TREATMENT")
    private String treatment;

    @Field(value="ORDERS")
    private String orders;

    @Field(value="DIAGNOSE")
    private String diagnose;

	@Field(value="DATE_EMR")
    private Date dateEmr;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP_EMR")
    private String pkEmpEmr;

	@Field(value="NAME_EMP_EMR")
    private String nameEmpEmr;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_EVENT")
    private String pkEvent;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="BREATHE")
	private String breathe; //呼吸

    @Field(value="PULSE")
    private String pulse; //脉搏

    @Field(value="OBS_RECORD")
    private String obsRecord; //观察记录

    @Field(value="PK_DOC")
    private String pkDoc;

    @Field(value="PSNHIST")
    public String psnhist;

    @Field(value="ALLERGY_OTHER")
    public String allergyOther;

    @Field(value="COURSE")
    private String course; //病程记录

    @Field(value="EU_VISIT")
    private String euVisit; //初诊标志

    @Field(value="CODE_DIAG_TCM")
    private String codeDiagTcm; //中医病名代码
    
    @Field(value="NAME_DIAG_TCM")
    private String nameDiagTcm; //中医病名名称
    
    @Field(value="CODE_SYND_TCM")
    private String codeSyndTcm; //中医证候代码
    
    @Field(value="NAME_SYND_TCM")
    private String nameSyndTcm; //中医证候名称
    
    @Field(value="RESULT_TCM")
    private String resultTcm; //中医“四诊”观察结果
    
    @Field(value="DIALECTIC")
    private String dialectic; //辨证依据
    
    @Field(value="PRINCIPLE")
    private String principle; //治则治法
    
    private String fmyhist;//家族史

    @Field(value="HEART_RATE")
    private Integer heartRate; //心率

    public String getPkEmrop(){
        return this.pkEmrop;
    }
    public void setPkEmrop(String pkEmrop){
        this.pkEmrop = pkEmrop;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getProblem(){
        return this.problem;
    }
    public void setProblem(String problem){
        this.problem = problem;
    }

    public String getPresent(){
        return this.present;
    }
    public void setPresent(String present){
        this.present = present;
    }

    public String getHistory(){
        return this.history;
    }
    public void setHistory(String history){
        this.history = history;
    }

    public String getAllergy(){
        return this.allergy;
    }
    public void setAllergy(String allergy){
        this.allergy = allergy;
    }

    public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWeight(){
        return this.weight;
    }
    public void setWeight(Double weight){
        this.weight = weight;
    }

    public Double getTemperature(){
        return this.temperature;
    }
    public void setTemperature(Double temperature){
        this.temperature = temperature;
    }

    public Integer getSbp(){
        return this.sbp;
    }
    public void setSbp(Integer sbp){
        this.sbp = sbp;
    }

    public Integer getDbp(){
        return this.dbp;
    }
    public void setDbp(Integer dbp){
        this.dbp = dbp;
    }

    public String getExamPhy(){
        return this.examPhy;
    }
    public void setExamPhy(String examPhy){
        this.examPhy = examPhy;
    }

    public String getExamAux(){
        return this.examAux;
    }
    public void setExamAux(String examAux){
        this.examAux = examAux;
    }

    public String getTreatmentPrograms() {
        return treatmentPrograms;
    }

    public void setTreatmentPrograms(String treatmentPrograms) {
        this.treatmentPrograms = treatmentPrograms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public Date getDateEmr(){
        return this.dateEmr;
    }
    public void setDateEmr(Date dateEmr){
        this.dateEmr = dateEmr;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmpEmr(){
        return this.pkEmpEmr;
    }
    public void setPkEmpEmr(String pkEmpEmr){
        this.pkEmpEmr = pkEmpEmr;
    }

    public String getNameEmpEmr(){
        return this.nameEmpEmr;
    }
    public void setNameEmpEmr(String nameEmpEmr){
        this.nameEmpEmr = nameEmpEmr;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkEvent(){
        return this.pkEvent;
    }
    public void setPkEvent(String pkEvent){
        this.pkEvent = pkEvent;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getBreathe() {
        return breathe;
    }

    public void setBreathe(String breathe) {
        this.breathe = breathe;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getObsRecord() {
        return obsRecord;
    }

    public void setObsRecord(String obsRecord) {
        this.obsRecord = obsRecord;
    }

    public String getPkDoc() {
        return pkDoc;
    }

    public void setPkDoc(String pkDoc) {
        this.pkDoc = pkDoc;
    }

    public String getPsnhist() {
        return psnhist;
    }

    public void setPsnhist(String psnhist) {
        this.psnhist = psnhist;
    }

    public String getAllergyOther() {
        return allergyOther;
    }

    public void setAllergyOther(String allergyOther) {
        this.allergyOther = allergyOther;
    }
	public String getExamSpec() {
		return examSpec;
	}
	public void setExamSpec(String examSpec) {
		this.examSpec = examSpec;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getEuVisit() {
		return euVisit;
	}
	public void setEuVisit(String euVisit) {
		this.euVisit = euVisit;
	}
	public String getCodeDiagTcm() {
		return codeDiagTcm;
	}
	public void setCodeDiagTcm(String codeDiagTcm) {
		this.codeDiagTcm = codeDiagTcm;
	}
	public String getNameDiagTcm() {
		return nameDiagTcm;
	}
	public void setNameDiagTcm(String nameDiagTcm) {
		this.nameDiagTcm = nameDiagTcm;
	}
	public String getCodeSyndTcm() {
		return codeSyndTcm;
	}
	public void setCodeSyndTcm(String codeSyndTcm) {
		this.codeSyndTcm = codeSyndTcm;
	}
	public String getNameSyndTcm() {
		return nameSyndTcm;
	}
	public void setNameSyndTcm(String nameSyndTcm) {
		this.nameSyndTcm = nameSyndTcm;
	}
	public String getResultTcm() {
		return resultTcm;
	}
	public void setResultTcm(String resultTcm) {
		this.resultTcm = resultTcm;
	}
	public String getDialectic() {
		return dialectic;
	}
	public void setDialectic(String dialectic) {
		this.dialectic = dialectic;
	}
	public String getPrinciple() {
		return principle;
	}
	public void setPrinciple(String principle) {
		this.principle = principle;
	}
	public String getFmyhist() {
		return fmyhist;
	}
	public void setFmyhist(String fmyhist) {
		this.fmyhist = fmyhist;
	}

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }
}
