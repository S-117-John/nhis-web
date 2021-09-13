package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * @author liH
 * @version 1.0
 * @date 2021/1/11 18:22
 * @description
 * @currentMinute zebone_CZ
 */
@Table(value="EMR_ADMIT_REC_TJ")
public class EmrAdmitRecTj  extends BaseModule {
	@PK
	@Field(value="PK_REC",id= KeyId.UUID)
	private String pkRec;

	@Field(value="PATIENT_ID")
	private String patientId;

	@Field(value="TIMES")
	private String times;

	@Field(value="CHIEF_COMPLAINT")
	private String chiefComplaint;

	@Field(value="MEDICAL_HISTORY")
	private String medicalHistory;

	@Field(value="SURGERY_HISTORY")
	private String surgeryHistory;

	@Field(value="BLOOD_TRANS_HISTORY")
	private String bloodTransHistory;

	@Field(value="DOC_NAME")
	private String docName;

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
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

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
