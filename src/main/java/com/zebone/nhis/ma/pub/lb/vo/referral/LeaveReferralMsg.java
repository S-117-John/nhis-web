
package com.zebone.nhis.ma.pub.lb.vo.referral;

import com.alibaba.fastjson.annotation.JSONField;

public class LeaveReferralMsg {

    private PatientInfoleave patientInfo;
    private DoctorInfoleave doctorInfo;
    private ProviderOrg providerOrg;
    private MainHealthIssues mainHealthIssues;
    private AdmissionDiagnosis admissionDiagnosis;
    private LeaveDiagnosis leaveDiagnosis;
    private Operation operation;
    private TreatmentPlanning treatmentPlanning;
    private HospitalizationProcess hospitalizationProcess;
    private MedicalAdviceLeave medicalAdvice;
    @JSONField(name = "laboratoryExamination")
    private Checkup checkup;
    private BackupInfo backupInfo;

    public PatientInfoleave getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoleave patientInfo) {
        this.patientInfo = patientInfo;
    }

    public DoctorInfoleave getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(DoctorInfoleave doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public ProviderOrg getProviderOrg() {
        return providerOrg;
    }

    public void setProviderOrg(ProviderOrg providerOrg) {
        this.providerOrg = providerOrg;
    }

    public void setMainHealthIssues(MainHealthIssues mainHealthIssues) {
         this.mainHealthIssues = mainHealthIssues;
     }
     public MainHealthIssues getMainHealthIssues() {
         return mainHealthIssues;
     }

    public void setAdmissionDiagnosis(AdmissionDiagnosis admissionDiagnosis) {
         this.admissionDiagnosis = admissionDiagnosis;
     }
     public AdmissionDiagnosis getAdmissionDiagnosis() {
         return admissionDiagnosis;
     }

    public void setLeaveDiagnosis(LeaveDiagnosis leaveDiagnosis) {
         this.leaveDiagnosis = leaveDiagnosis;
     }
     public LeaveDiagnosis getLeaveDiagnosis() {
         return leaveDiagnosis;
     }

    public void setOperation(Operation operation) {
         this.operation = operation;
     }
     public Operation getOperation() {
         return operation;
     }

    public void setTreatmentPlanning(TreatmentPlanning treatmentPlanning) {
         this.treatmentPlanning = treatmentPlanning;
     }
     public TreatmentPlanning getTreatmentPlanning() {
         return treatmentPlanning;
     }

    public void setHospitalizationProcess(HospitalizationProcess hospitalizationProcess) {
         this.hospitalizationProcess = hospitalizationProcess;
     }
     public HospitalizationProcess getHospitalizationProcess() {
         return hospitalizationProcess;
     }

    public MedicalAdviceLeave getMedicalAdvice() {
        return medicalAdvice;
    }

    public void setMedicalAdvice(MedicalAdviceLeave medicalAdvice) {
        this.medicalAdvice = medicalAdvice;
    }

    public void setCheckup(Checkup checkup) {
         this.checkup = checkup;
     }
     public Checkup getCheckup() {
         return checkup;
     }

    public BackupInfo getBackupInfo() {
        return backupInfo;
    }

    public void setBackupInfo(BackupInfo backupInfo) {
        this.backupInfo = backupInfo;
    }
}