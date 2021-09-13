
package com.zebone.nhis.ma.pub.lb.vo.referral;


public class DoctorInfo {

    private String doctorId;
    private String doctorName;
    private String referralDoctorId;
    private String referralDoctorName;
    public void setDoctorId(String doctorId) {
         this.doctorId = doctorId;
     }
     public String getDoctorId() {
         return doctorId;
     }

    public void setDoctorName(String doctorName) {
         this.doctorName = doctorName;
     }
     public String getDoctorName() {
         return doctorName;
     }

    public void setReferralDoctorId(String referralDoctorId) {
         this.referralDoctorId = referralDoctorId;
     }
     public String getReferralDoctorId() {
         return referralDoctorId;
     }

    public void setReferralDoctorName(String referralDoctorName) {
         this.referralDoctorName = referralDoctorName;
     }
     public String getReferralDoctorName() {
         return referralDoctorName;
     }

}