
package com.zebone.nhis.ma.pub.lb.vo.referral;


public class PatientInfoleave {

    private String patientId;
    private String patientName;
    private String patientPhone;
    private String certificateId;
    private String certificateTypeCode;
    private String sexCode;
    private String age;
    private String maritalStatus;
    private String jobTypeCode;
    private String healthCardId;
    private String residentialAddress;
    private String insuranceCardNo;
    private String insuranceType;
    public void setPatientId(String patientId) {
         this.patientId = patientId;
     }
     public String getPatientId() {
         return patientId;
     }

    public void setPatientName(String patientName) {
         this.patientName = patientName;
     }
     public String getPatientName() {
         return patientName;
     }

    public void setPatientPhone(String patientPhone) {
         this.patientPhone = patientPhone;
     }
     public String getPatientPhone() {
         return patientPhone;
     }

    public void setCertificateId(String certificateId) {
         this.certificateId = certificateId;
     }
     public String getCertificateId() {
         return certificateId;
     }

    public void setCertificateTypeCode(String certificateTypeCode) {
         this.certificateTypeCode = certificateTypeCode;
     }
     public String getCertificateTypeCode() {
         return certificateTypeCode;
     }

    public void setSexCode(String sexCode) {
         this.sexCode = sexCode;
     }
     public String getSexCode() {
         return sexCode;
     }

    public void setAge(String age) {
         this.age = age;
     }
     public String getAge() {
         return age;
     }

    public void setMaritalStatus(String maritalStatus) {
         this.maritalStatus = maritalStatus;
     }
     public String getMaritalStatus() {
         return maritalStatus;
     }

    public void setJobTypeCode(String jobTypeCode) {
         this.jobTypeCode = jobTypeCode;
     }
     public String getJobTypeCode() {
         return jobTypeCode;
     }

    public void setHealthCardId(String healthCardId) {
         this.healthCardId = healthCardId;
     }
     public String getHealthCardId() {
         if (null == this.healthCardId){
             return  "";
         }else {
             return  this.healthCardId;
         }
     }

    public void setResidentialAddress(String residentialAddress) {
         this.residentialAddress = residentialAddress;
     }
     public String getResidentialAddress() {
         return residentialAddress;
     }

    public void setInsuranceCardNo(String insuranceCardNo) {
         this.insuranceCardNo = insuranceCardNo;
     }
     public String getInsuranceCardNo() {
         return insuranceCardNo;
     }

    public void setInsuranceType(String insuranceType) {
         this.insuranceType = insuranceType;
     }
     public String getInsuranceType() {
         return insuranceType;
     }

}