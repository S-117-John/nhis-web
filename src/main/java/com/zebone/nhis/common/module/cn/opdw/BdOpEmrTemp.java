package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OPEMR_TEMP  - BD_OPEMR_TEMP 
 *
 * @since 2017-02-17 04:30:03
 */
@Table(value="BD_OPEMR_TEMP")
public class BdOpEmrTemp extends BaseModule  {

	@PK
	@Field(value="PK_TEMP",id=KeyId.UUID)
    private String pkTemp;

	@Field(value="PK_TEMPCATE")
    private String pkTempcate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="PROBLEM")
    private String problem;

	@Field(value="PRESENT")
    private String present;

	@Field(value="HISTORY")
    private String history;

	@Field(value="PSNHIST")
    private String psnhist;
	
	@Field(value="MRHIST")
    private String mrhist;
	
	@Field(value="FMYHIST")
    private String fmyhist;
	
	
	@Field(value="ALLERGY")
    private String allergy;

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

    @Field(value="COURSE")
    private String course;
    
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    //备注
    @Field(value="NOTE")
    private String note;
    //观察记录
    @Field(value = "OBS_RECORD")
    private String obsRecord;


    public String getPkTemp(){
        return this.pkTemp;
    }
    public void setPkTemp(String pkTemp){
        this.pkTemp = pkTemp;
    }

    public String getPkTempcate(){
        return this.pkTempcate;
    }
    public void setPkTempcate(String pkTempcate){
        this.pkTempcate = pkTempcate;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
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

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getObsRecord() {
        return obsRecord;
    }

    public void setObsRecord(String obsRecord) {
        this.obsRecord = obsRecord;
    }
	public String getExamSpec() {
		return examSpec;
	}
	public void setExamSpec(String examSpec) {
		this.examSpec = examSpec;
	}
	public String getPsnhist() {
		return psnhist;
	}
	public void setPsnhist(String psnhist) {
		this.psnhist = psnhist;
	}
	public String getMrhist() {
		return mrhist;
	}
	public void setMrhist(String mrhist) {
		this.mrhist = mrhist;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getFmyhist() {
		return fmyhist;
	}
	public void setFmyhist(String fmyhist) {
		this.fmyhist = fmyhist;
	}
    
    
}