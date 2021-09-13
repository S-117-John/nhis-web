
package com.zebone.nhis.ma.pub.lb.vo.referral;


import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientInfo {

    private String outpatientId;
    private String hisMedicalId;
    private String patientId;
    private String patientName;
    private String sexCode;
    private String birthday;
    private String age;
    private String certificateId;
    private String certificateTypeCode;
    private String healthCardId;
    private String phone;
    private String residentialAddress;
    private String insuranceCardNo;
    private String insuranceType;
    private String outpatientTime;
    public void setOutpatientId(String outpatientId) {
         this.outpatientId = outpatientId;
     }
     public String getOutpatientId() {
         return outpatientId;
     }

    public void setHisMedicalId(String hisMedicalId) {
         this.hisMedicalId = hisMedicalId;
     }
     public String getHisMedicalId() {
         return hisMedicalId;
     }

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

    public void setSexCode(String sexCode) {
         this.sexCode = sexCode;
     }
     public String getSexCode() {
         return sexCode;
     }

    public void setBirthday(String birthday) {
         this.birthday = birthday;
     }
     public String getBirthday() {
         return birthday;
     }

    public void setAge(String age) {
         this.age = age;
     }
     public String getAge() {
         return age;
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

    public void setPhone(String phone) {
         this.phone = phone;
     }
     public String getPhone() {
         return phone;
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

    public String getOutpatientTime() {
        if (null == this.outpatientTime){
            SimpleDateFormat sf = new SimpleDateFormat ("yyyyyMMddHHmmss");
            return  sf.format(new Date());
        }else {
            return  this.outpatientTime;
        }
    }

    public void setOutpatientTime(String outpatientTime) {
        this.outpatientTime = outpatientTime;
    }
}