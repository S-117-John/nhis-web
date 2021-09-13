package com.zebone.nhis.ma.pub.lb.dao;

import com.zebone.nhis.ma.pub.lb.vo.referral.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LbXFReferralMapper {
    //门诊
    PatientInfo qryPatientInfo(Map<String,Object> param);
    DoctorInfo qryDoctorInfo(Map<String, Object> param);
    ProviderOrg qryOrgInfo(Map<String, Object> param);
    Map<String, Object> qryElse(Map<String, Object> param);
    PhysicalExamination qryPhysicalExamination(Map<String, Object> param);
    List<DiagList> qryDiagList(Map<String, Object> param);
    //出院
    PatientInfoleave qryPatientLeave(Map<String,Object> param);
    DoctorInfoleave qryDoctorleave(Map<String, Object> param);
    AdmissionDiagnosis qryAdmissionDiagnosis(Map<String, Object> param);
    TreatmentPlanning qryTreatmentPlanning(Map<String, Object> param);
    LeaveDiagnosis qryLeaveDiagnosis(Map<String, Object> param);
    MainHealthIssues qryMainhealthIssues(Map<String,Object> param);
    MedicalAdviceLeave qryMedicalAdviceLeave(Map<String,Object> param);
    Operation qryOperation(Map<String,Object> param);
}
