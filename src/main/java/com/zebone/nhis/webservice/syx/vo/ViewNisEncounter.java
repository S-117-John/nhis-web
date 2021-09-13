package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "VIEW_NIS_ENCOUNTER")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewNisEncounter {
	@XmlElement(name = "encounter_id")
	private String encounterId;
	@XmlElement(name = "patient_id")
	private String patientId;
	@XmlElement(name = "admission_date")
	private String admissionDate;
	@XmlElement(name = "seqNoText")
	private String seq_no_text;
	@XmlElement(name = "admiss_times")
	private String admissTimes;
	@XmlElement(name = "sickbed_id")
	private String sickbedId;
	@XmlElement(name = "display_name")
	private String displayName;
	@XmlElement(name = "date_of_birth")
	private String dateOfBirth;
	@XmlElement(name = "native_place")
	private String nativePlace;
	@XmlElement(name = "gender_flag")
	private String genderFlag;
	@XmlElement(name = "marital_flag")
	private String maritalFlag;
	@XmlElement(name = "patient_type_id")
	private String patientTypeId;
	@XmlElement(name = "admit_way_flag")
	private String admitWayFlag;
	@XmlElement(name = "admission_org_id")
	private String admissionOrgId;
	@XmlElement(name = "admission_state_flag")
	private String admissionStateFlag;
	@XmlElement(name = "attending_doctor")
	private String attendingDoctor;
	@XmlElement(name = "residency_doctor")
	private String residencyDoctor;
	@XmlElement(name = "discharge_treatment_flag")
	private String dischargeTreatmentFlag;
	@XmlElement(name = "discharge_date")
	private String dischargeDate;
	@XmlElement(name = "discharged_by")
	private String dischargedBy;
	@XmlElement(name = "owning_org_id")
	private String owningOrgId;
	@XmlElement(name = "illness_state_flag")
	private String illnessStateFlag;
	@XmlElement(name = "dietetic_state_flag")
	private String dieteticStateFlag;
	@XmlElement(name = "nursing_degree_flag")
	private String nursingDegreeFlag;
	@XmlElement(name = "contact_name")
	private String contactName;
	@XmlElement(name = "contact_relation")
	private String contactRelation;
	@XmlElement(name = "contact_address")
	private String contactAddress;
	@XmlElement(name = "allergy_flag")
	private String allergyFlag;
	@XmlElement(name = "contact_tel")
	private String contactTel;
	@XmlElement(name = "baby_ordinal")
	private String babyOrdinal;
	@XmlElement(name = "security_level_flag")
	private String securityLevelFlag;
	@XmlElement(name = "status_flag")
	private String statusFlag;
	@XmlElement(name = "folk")
	private String folk;
	@XmlElement(name = "nationality")
	private String nationality;
	@XmlElement(name = "native_address")
	private String nativeAddress;
	@XmlElement(name = "telephone")
	private String telephone;
	@XmlElement(name = "guardian_name")
	private String guardianName;
	@XmlElement(name = "living_address")
	private String livingAddress;
	@XmlElement(name = "is_deceased")
	private String isDeceased;
	@XmlElement(name = "deceased_date")
	private String deceasedDate;
	@XmlElement(name = "academic")
	private String academic;
	@XmlElement(name = "religious")
	private String religious;
	@XmlElement(name = "profession")
	private String profession;
	@XmlElement(name = "is_deleted")
	private String isDeleted;
	@XmlElement(name = "mother_encounter_key")
	private String motherEncounterKey;
	@XmlElement(name = "in_diagnosis")
	private String inDiagnosis;
	public String getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(String encounterId) {
		this.encounterId = encounterId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	public String getSeq_no_text() {
		return seq_no_text;
	}
	public void setSeq_no_text(String seq_no_text) {
		this.seq_no_text = seq_no_text;
	}
	public String getAdmissTimes() {
		return admissTimes;
	}
	public void setAdmissTimes(String admissTimes) {
		this.admissTimes = admissTimes;
	}
	public String getSickbedId() {
		return sickbedId;
	}
	public void setSickbedId(String sickbedId) {
		this.sickbedId = sickbedId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getGenderFlag() {
		return genderFlag;
	}
	public void setGenderFlag(String genderFlag) {
		this.genderFlag = genderFlag;
	}
	public String getMaritalFlag() {
		return maritalFlag;
	}
	public void setMaritalFlag(String maritalFlag) {
		this.maritalFlag = maritalFlag;
	}
	public String getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(String patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public String getAdmitWayFlag() {
		return admitWayFlag;
	}
	public void setAdmitWayFlag(String admitWayFlag) {
		this.admitWayFlag = admitWayFlag;
	}
	public String getAdmissionOrgId() {
		return admissionOrgId;
	}
	public void setAdmissionOrgId(String admissionOrgId) {
		this.admissionOrgId = admissionOrgId;
	}
	public String getAdmissionStateFlag() {
		return admissionStateFlag;
	}
	public void setAdmissionStateFlag(String admissionStateFlag) {
		this.admissionStateFlag = admissionStateFlag;
	}
	public String getAttendingDoctor() {
		return attendingDoctor;
	}
	public void setAttendingDoctor(String attendingDoctor) {
		this.attendingDoctor = attendingDoctor;
	}
	public String getResidencyDoctor() {
		return residencyDoctor;
	}
	public void setResidencyDoctor(String residencyDoctor) {
		this.residencyDoctor = residencyDoctor;
	}
	public String getDischargeTreatmentFlag() {
		return dischargeTreatmentFlag;
	}
	public void setDischargeTreatmentFlag(String dischargeTreatmentFlag) {
		this.dischargeTreatmentFlag = dischargeTreatmentFlag;
	}
	public String getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	public String getDischargedBy() {
		return dischargedBy;
	}
	public void setDischargedBy(String dischargedBy) {
		this.dischargedBy = dischargedBy;
	}
	public String getOwningOrgId() {
		return owningOrgId;
	}
	public void setOwningOrgId(String owningOrgId) {
		this.owningOrgId = owningOrgId;
	}
	public String getIllnessStateFlag() {
		return illnessStateFlag;
	}
	public void setIllnessStateFlag(String illnessStateFlag) {
		this.illnessStateFlag = illnessStateFlag;
	}
	public String getDieteticStateFlag() {
		return dieteticStateFlag;
	}
	public void setDieteticStateFlag(String dieteticStateFlag) {
		this.dieteticStateFlag = dieteticStateFlag;
	}
	public String getNursingDegreeFlag() {
		return nursingDegreeFlag;
	}
	public void setNursingDegreeFlag(String nursingDegreeFlag) {
		this.nursingDegreeFlag = nursingDegreeFlag;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactRelation() {
		return contactRelation;
	}
	public void setContactRelation(String contactRelation) {
		this.contactRelation = contactRelation;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getAllergyFlag() {
		return allergyFlag;
	}
	public void setAllergyFlag(String allergyFlag) {
		this.allergyFlag = allergyFlag;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getBabyOrdinal() {
		return babyOrdinal;
	}
	public void setBabyOrdinal(String babyOrdinal) {
		this.babyOrdinal = babyOrdinal;
	}
	public String getSecurityLevelFlag() {
		return securityLevelFlag;
	}
	public void setSecurityLevelFlag(String securityLevelFlag) {
		this.securityLevelFlag = securityLevelFlag;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getFolk() {
		return folk;
	}
	public void setFolk(String folk) {
		this.folk = folk;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNativeAddress() {
		return nativeAddress;
	}
	public void setNativeAddress(String nativeAddress) {
		this.nativeAddress = nativeAddress;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getLivingAddress() {
		return livingAddress;
	}
	public void setLivingAddress(String livingAddress) {
		this.livingAddress = livingAddress;
	}
	public String getIsDeceased() {
		return isDeceased;
	}
	public void setIsDeceased(String isDeceased) {
		this.isDeceased = isDeceased;
	}
	public String getDeceasedDate() {
		return deceasedDate;
	}
	public void setDeceasedDate(String deceasedDate) {
		this.deceasedDate = deceasedDate;
	}
	public String getAcademic() {
		return academic;
	}
	public void setAcademic(String academic) {
		this.academic = academic;
	}
	public String getReligious() {
		return religious;
	}
	public void setReligious(String religious) {
		this.religious = religious;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getMotherEncounterKey() {
		return motherEncounterKey;
	}
	public void setMotherEncounterKey(String motherEncounterKey) {
		this.motherEncounterKey = motherEncounterKey;
	}
	public String getInDiagnosis() {
		return inDiagnosis;
	}
	public void setInDiagnosis(String inDiagnosis) {
		this.inDiagnosis = inDiagnosis;
	}
	
	
}
