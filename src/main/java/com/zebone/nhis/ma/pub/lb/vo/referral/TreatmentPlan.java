
package com.zebone.nhis.ma.pub.lb.vo.referral;


public class TreatmentPlan {

    private String dialecticalAnalysis;
    private String treatmentSuggestion;
    private String opinionByDct;
    public void setDialecticalAnalysis(String dialecticalAnalysis) {
         this.dialecticalAnalysis = dialecticalAnalysis;
     }
     public String getDialecticalAnalysis() {
         return dialecticalAnalysis;
     }

    public void setTreatmentSuggestion(String treatmentSuggestion) {
         this.treatmentSuggestion = treatmentSuggestion;
     }
     public String getTreatmentSuggestion() {
         return treatmentSuggestion;
     }

    public void setOpinionByDct(String opinionByDct) {
         this.opinionByDct = opinionByDct;
     }
     public String getOpinionByDct() {
         return opinionByDct;
     }

}