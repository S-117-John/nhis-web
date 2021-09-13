
package com.zebone.nhis.ma.pub.lb.vo.referral;

public class MedicalAdviceLeave {

    private String leaveAdvice;
    private String decoctionWay;
    private String medicationWay;
    private String fourDiagnostic;
    public void setLeaveAdvice(String leaveAdvice) {
        this.leaveAdvice = leaveAdvice;
    }
    public String getLeaveAdvice() {
        return leaveAdvice;
    }

    public void setDecoctionWay(String decoctionWay) {
        this.decoctionWay = decoctionWay;
    }
    public String getDecoctionWay() {
        return decoctionWay;
    }

    public void setMedicationWay(String medicationWay) {
        this.medicationWay = medicationWay;
    }
    public String getMedicationWay() {
        return medicationWay;
    }

    public void setFourDiagnostic(String fourDiagnostic) {
        this.fourDiagnostic = fourDiagnostic;
    }
    public String getFourDiagnostic() {
        return fourDiagnostic;
    }

}