
package com.zebone.nhis.ma.pub.lb.vo.referral;


public class HospitalizationProcess {

    private String treatmentResultsCode;
    private String generalPracticeDesc;
    public void setTreatmentResultsCode(String treatmentResultsCode) {
         this.treatmentResultsCode = treatmentResultsCode;
     }
     public String getTreatmentResultsCode() {
         return treatmentResultsCode;
     }

    public void setGeneralPracticeDesc(String generalPracticeDesc) {
         this.generalPracticeDesc = generalPracticeDesc;
     }
     public String getGeneralPracticeDesc() {
         return generalPracticeDesc;
     }

}