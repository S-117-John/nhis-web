
package com.zebone.nhis.ma.pub.lb.vo.referral;
import java.util.List;

public class VisitReferralMsg {

    private PatientInfo patientInfo;
    private ProviderOrg providerOrg;
    private DoctorInfo doctorInfo;
    private String mainSuit;
    private String illnessHistory;
    private String previousHistory;
    private String irritabilityHistory;
    private PhysicalExamination physicalExamination;
    private LaboratoryExamination laboratoryExamination;
    private TreatmentPlan treatmentPlan;
    private MedicalAdvice medicalAdvice;
    private BackupInfo backupInfo;
    private List<DiagList> diagList;
    public void setPatientInfo(PatientInfo patientInfo) {
         this.patientInfo = patientInfo;
     }
     public PatientInfo getPatientInfo() {
         return patientInfo;
     }

    public void setProviderOrg(ProviderOrg providerOrg) {
         this.providerOrg = providerOrg;
     }
     public ProviderOrg getProviderOrg() {
         return providerOrg;
     }

    public void setDoctorInfo(DoctorInfo doctorInfo) {
         this.doctorInfo = doctorInfo;
     }
     public DoctorInfo getDoctorInfo() {
         return doctorInfo;
     }

    public void setMainSuit(String mainSuit) {
         this.mainSuit = mainSuit;
     }
     public String getMainSuit() {
         return mainSuit;
     }

    public void setIllnessHistory(String illnessHistory) {
         this.illnessHistory = illnessHistory;
     }
     public String getIllnessHistory() {
         return illnessHistory;
     }

    public void setPreviousHistory(String previousHistory) {
         this.previousHistory = previousHistory;
     }
     public String getPreviousHistory() {
         return previousHistory;
     }

    public void setIrritabilityHistory(String irritabilityHistory) {
         this.irritabilityHistory = irritabilityHistory;
     }
     public String getIrritabilityHistory() {
         return irritabilityHistory;
     }

    public void setPhysicalExamination(PhysicalExamination physicalExamination) {
         this.physicalExamination = physicalExamination;
     }
     public PhysicalExamination getPhysicalExamination() {
         return physicalExamination;
     }

    public void setLaboratoryExamination(LaboratoryExamination laboratoryExamination) {
         this.laboratoryExamination = laboratoryExamination;
     }
     public LaboratoryExamination getLaboratoryExamination() {
         return laboratoryExamination;
     }

    public void setTreatmentPlan(TreatmentPlan treatmentPlan) {
         this.treatmentPlan = treatmentPlan;
     }
     public TreatmentPlan getTreatmentPlan() {
         return treatmentPlan;
     }

    public void setMedicalAdvice(MedicalAdvice medicalAdvice) {
         this.medicalAdvice = medicalAdvice;
     }
     public MedicalAdvice getMedicalAdvice() {
         return medicalAdvice;
     }

    public void setBackupInfo(BackupInfo backupInfo) {
         this.backupInfo = backupInfo;
     }
     public BackupInfo getBackupInfo() {
         return backupInfo;
     }

    public void setDiagList(List<DiagList> diagList) {
         this.diagList = diagList;
     }
     public List<DiagList> getDiagList() {
         return diagList;
     }

}